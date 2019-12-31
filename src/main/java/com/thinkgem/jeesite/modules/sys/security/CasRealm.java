package com.thinkgem.jeesite.modules.sys.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.AbstractUrlBasedTicketValidator;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * cas realm , 只处理 CasToken
 *
 * @author 廖水平
 */
@Service
public class CasRealm extends org.apache.shiro.cas.CasRealm {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private SystemService systemService;

	@Autowired
	private CasAddUserService casAddUserService;

	private Collection<OtherAuthorizingLogic> otherAuthorizingLogics;

	public void setOtherAuthorizingLogics(Collection<OtherAuthorizingLogic> otherAuthorizingLogics) {
		this.otherAuthorizingLogics = otherAuthorizingLogics;
	}

	/**
	 * 根据CAS统一身份认证服务器返回的认证数据在本系统中创建新的用户<br/>
	 * 默认使用这个类： {@link DefaultCasAddUserService}
	 */
	public void setCasAddUserService(CasAddUserService casAddUserService) {
		this.casAddUserService = casAddUserService;
	}

	public CasRealm() {
		setAuthenticationTokenClass(CasToken.class);
	}

	/**
	 * <pre>
	 * 	1、	cas登录链接，.p携带base64save编码的参数，其原始内容可以是登录后要跳转的链接或者是一个查询字符串。注：service指定的url最多只能携带一个参数，所以如果有多个参数，需要对多个参数进行编码成一个串。
	 * 	GET  http://authserver.cau.edu.cn/authserver/login?service=http://202.205.88.66/cnomp/a/casLogin?.p=cGFnZU5vPTEmcGFnZVNpemU9MjAmb3JkZXJCeT0mcm9vbS5idWlsZGluZy5jYW1wdXMuaWQ9NDZlY2M5Y2M4ZmI4NGEyMGIxMTcxNTI3MWQxYWE3Nzcmcm9vbS5idWlsZGluZy5pZD1mMjc4YzdhOGFhOGQ0MGVjYjE1ZTUxZTUxZGJjMDc0MSZyb29tLmlkPTE3MzJjMDc3YTJjNzQ4OWRiNjMwNGUxYTVmMmRmZmE1JnNjaG9vbHllYXI9MjAxNSZzdHVkZW50LmRlcC5pZD0yJnN0dWRlbnQuc3R1dHlwZT0yJnN0dWRlbnQuZ3JhZGU9MjAxNiZzdHVkZW50Lmxlbmd0aE9mU2Nob29saW5nPTMmc3R1ZGVudC5zZXg9MSZzdHVkZW50LnN0dW5hbWU9ZyZzdHVkZW50LnN0dWlkPTIxNTA4MDIwMDM
	 * 	2、	没有登录时重定向到cas登录
	 * 	POST http://authserver.cau.edu.cn/authserver/login?service=http://202.205.88.66/cnomp/a/casLogin?.p=cGFnZU5vPTEmcGFnZVNpemU9MjAmb3JkZXJCeT0mcm9vbS5idWlsZGluZy5jYW1wdXMuaWQ9NDZlY2M5Y2M4ZmI4NGEyMGIxMTcxNTI3MWQxYWE3Nzcmcm9vbS5idWlsZGluZy5pZD1mMjc4YzdhOGFhOGQ0MGVjYjE1ZTUxZTUxZGJjMDc0MSZyb29tLmlkPTE3MzJjMDc3YTJjNzQ4OWRiNjMwNGUxYTVmMmRmZmE1JnNjaG9vbHllYXI9MjAxNSZzdHVkZW50LmRlcC5pZD0yJnN0dWRlbnQuc3R1dHlwZT0yJnN0dWRlbnQuZ3JhZGU9MjAxNiZzdHVkZW50Lmxlbmd0aE9mU2Nob29saW5nPTMmc3R1ZGVudC5zZXg9MSZzdHVkZW50LnN0dW5hbWU9ZyZzdHVkZW50LnN0dWlkPTIxNTA4MDIwMDM
	 * 	3、   登录成功后跳转地址，.p参数仍然被传递过来了，自己拿到后进行转码，得到原始数据后可以进一步处理了。
	 * 	GET  http://202.205.88.66/cnomp/a/casLogin?.p=cGFnZU5vPTEmcGFnZVNpemU9MjAmb3JkZXJCeT0mcm9vbS5idWlsZGluZy5jYW1wdXMuaWQ9NDZlY2M5Y2M4ZmI4NGEyMGIxMTcxNTI3MWQxYWE3Nzcmcm9vbS5idWlsZGluZy5pZD1mMjc4YzdhOGFhOGQ0MGVjYjE1ZTUxZTUxZGJjMDc0MSZyb29tLmlkPTE3MzJjMDc3YTJjNzQ4OWRiNjMwNGUxYTVmMmRmZmE1JnNjaG9vbHllYXI9MjAxNSZzdHVkZW50LmRlcC5pZD0yJnN0dWRlbnQuc3R1dHlwZT0yJnN0dWRlbnQuZ3JhZGU9MjAxNiZzdHVkZW50Lmxlbmd0aE9mU2Nob29saW5nPTMmc3R1ZGVudC5zZXg9MSZzdHVkZW50LnN0dW5hbWU9ZyZzdHVkZW50LnN0dWlkPTIxNTA4MDIwMDM&ticket=ST-85001-kJDPOJHjx6yFYTOfQJzB1498011798558-EyNB-cas
	 * 	4、   cas校验成功，跳转到已登录界面，此处可以利用.p中的参数值来做个性化的定制。
	 * 	GET  http://202.205.88.66/cnomp/a?login
	 * </pre>
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		////////////////////////////////////////////////////////////
		if (!Collections3.isEmpty(otherAuthorizingLogics)) {
			for (OtherAuthorizingLogic otherAuthorizingLogic : otherAuthorizingLogics) {
				if (otherAuthorizingLogic != null) {
					otherAuthorizingLogic.beforeDoGetAuthenticationInfo(token);
				}
			}
		}
		////////////////////////////////////////////////////////////

		CasToken casToken = (CasToken) token;
		if (token == null) {
			return null;
		}
		String ticket = (String) casToken.getCredentials();
		logger.debug("ticket-------->{}", ticket);

		logger.debug("casAddUserService ---> {}", casAddUserService.getClass().getName());

		if (StringUtils.isEmpty(ticket)) {
			return null;
		}

		/*if (1 == 1) {
			throw new UnknownAccountException("msg:测试cas认证错误信息");
		}*/

		TicketValidator ticketValidator = ensureTicketValidator();

		//ticketValidator通过HttpsURLConnection读取cas数据的，不属编码过滤器管理。
		//此处用于设置ticketValidator读取cas数据的时编码，解决中文乱码问题。
		if (ticketValidator instanceof AbstractUrlBasedTicketValidator) {
			AbstractUrlBasedTicketValidator urlBasedTicketValidator = (AbstractUrlBasedTicketValidator) ticketValidator;
			urlBasedTicketValidator.setEncoding(encoding);
			ticketValidator = urlBasedTicketValidator;
		}

		try {
			//casService配置文件中是写死的，实际可能会携带参数，这样去校验的时候会失败（报错票根不符合目标服务）
			String casService = getCasService();
			String p = casToken.getP();
			logger.debug("Validate ticket , p : {}", p);
			if (!StringUtils.isBlank(p)) {
				casService += "?" + CasFilter.P_PARAMETER + "=" + p;
				UserUtils.getSession().setAttribute(CasFilter.P_PARAMETER, p);
			}

			// contact CAS server to validate service ticket
			logger.debug("Validate ticket , casService : {}", casService);
			Assertion casAssertion = ticketValidator.validate(ticket, casService);
			// get principal, user id and attributes
			AttributePrincipal casPrincipal = casAssertion.getPrincipal();
			String userId = casPrincipal.getName();
			logger.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}", new Object[] { ticket, getCasServerUrlPrefix(), userId });
			Map<String, Object> attributes = casPrincipal.getAttributes();
			for (Entry<String, Object> entry : attributes.entrySet()) {
				logger.debug("{}={}", entry.getKey(), entry.getValue());
			}

			// 通过了CAS验证，检查本地库是否存在此用户
			User user = getSystemService().getUserByLoginName(userId);

			// 本地库不存在时，添加用户到本地库。
			if (user == null) {
				try {
					logger.debug("addUser ......");
					user = casAddUserService.addUser(attributes, userId);
				} catch (Exception e) {
					throw new CasAuthenticationException("msg:" + e.getMessage(), e);
				}
			}

			// refresh authentication token (user id + remember me)
			casToken.setUserId(userId);
			String rememberMeAttributeName = getRememberMeAttributeName();
			String rememberMeStringValue = (String) attributes.get(rememberMeAttributeName);
			boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
			if (isRemembered) {
				casToken.setRememberMe(true);
			}
			// create simple authentication info
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(new SimplePrincipalCollection(new Principal(user, false, null), getName()), ticket);

			////////////////////////////////////////////////////////////
			if (!Collections3.isEmpty(otherAuthorizingLogics)) {
				for (OtherAuthorizingLogic otherAuthorizingLogic : otherAuthorizingLogics) {
					if (otherAuthorizingLogic != null) {
						otherAuthorizingLogic.afterDoGetAuthenticationInfo(token, authenticationInfo, user);
					}
				}
			}
			////////////////////////////////////////////////////////////

			return authenticationInfo;

		} catch (TicketValidationException e) {
			//throw new CasAuthenticationException(new StringBuilder().append("Unable to validate ticket [").append(ticket).append("]").toString(), e);
			throw new CasAuthenticationException("msg:统一身份认证失败", e);
		}
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		logger.debug("principal----->{}", principal);
		// 获取当前已登录的用户
		if (!SettingsUtils.getSysConfig("sys.login.multiAccountLogin", true)) {
			Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
			for (Session session : sessions) {
				logger.debug("账号id为{}的session，id={}，principals_session_key={}", principal, session.getId(), session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
			}
			if (sessions.size() > 0) {
				// 如果是登录进来的，则踢出已在线用户
				if (UserUtils.getSubject().isAuthenticated()) {
					for (Session session : sessions) {
						logger.debug("删除 session.id={},session.ip={}", session.getId(), session.getHost());
						getSystemService().getSessionDao().delete(session);
					}
				}
				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
				else {
					logger.debug("账号已在其它地方登录，请重新登录。");
					UserUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
				}
			}
		}
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list) {
				if (StringUtils.isNotBlank(menu.getPermission())) {
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(), ",")) {
						info.addStringPermission(permission);
					}
				}
			}
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			for (Role role : user.getRoleList()) {
				info.addRole(role.getEnname());
			}
			// 更新登录IP和时间
			getSystemService().updateUserLoginInfo(user);
			// 记录登录日志
			LogUtils.saveLog(Servlets.getRequest(), "通过CAS登录");
			return info;
		} else {
			return null;
		}
	}

	protected List<String> split(String s) {
		String elements[] = StringUtils.split(s, ',');
		List<String> list = new ArrayList<>();
		if (elements != null && elements.length > 0) {
			for (String element : elements) {
				if (!StringUtils.isEmpty(element)) {
					list.add(element.trim());
				}
			}
		}
		return list;
	}

	protected void addRoles(SimpleAuthorizationInfo simpleAuthorizationInfo, List<String> roles) {
		for (String role : roles) {
			simpleAuthorizationInfo.addRole(role);
		}
	}

	protected void addPermissions(SimpleAuthorizationInfo simpleAuthorizationInfo, List<String> permissions) {
		for (String permission : permissions) {
			simpleAuthorizationInfo.addStringPermission(permission);
		}
	}

	public SystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}

	/**
	 * 默认UTF-8，可在xml文件中配置注入.
	 */
	private String encoding = "UTF-8";

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(Principal principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

}
