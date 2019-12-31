package com.thinkgem.jeesite.modules.sys.security;

import java.util.Collection;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * @author 廖水平<br>
 *         处理任意 AuthenticationToken
 */
//暂未启用
//@Service
public class JndiLdapRealm extends org.apache.shiro.realm.ldap.JndiLdapRealm {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SystemService systemService;

	/*@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		AuthenticationInfo info;
		try {
			info = queryForAuthenticationInfo(token, getContextFactory());
		} catch (AuthenticationNotSupportedException e) {
			String msg = "Unsupported configured authentication mechanism";
			throw new UnsupportedAuthenticationMechanismException(msg, e);
		} catch (javax.naming.AuthenticationException e) {
			throw new AuthenticationException("LDAP authentication failed.", e);
		} catch (NamingException e) {
			String msg = "LDAP naming error while attempting to authenticate user.";
			throw new AuthenticationException(msg, e);
		}
		return info;
	}

	@Override
	protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken token,
			LdapContextFactory ldapContextFactory) throws NamingException {
		log.debug("token.getClass().getName()={}", token.getClass().getName());
		Object principal = token.getPrincipal();
		Object credentials = token.getCredentials();

		log.debug("Authenticating user '{}' through LDAP", principal);

		principal = getLdapPrincipal(token);

		log.debug("ldap principal={}", principal);

		LdapContext ctx = null;
		try {
			ctx = ldapContextFactory.getLdapContext(principal, credentials);
			//context was opened successfully, which means their credentials were valid.  Return the AuthenticationInfo:
			return createAuthenticationInfo(token, principal, credentials, ctx);
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}*/

	@Override
	protected AuthenticationInfo createAuthenticationInfo(AuthenticationToken token, Object ldapPrincipal, Object ldapCredentials, LdapContext ldapContext) throws NamingException {
		try {
			String loginName = token.getPrincipal().toString();
			log.debug("loginName={}", token.getPrincipal().toString());

			//用户的ldap数据
			Attributes attributes = ldapContext.getAttributes(ldapPrincipal.toString());
			NamingEnumeration<?> enumeration = attributes.getAll();
			while (enumeration.hasMoreElements()) {
				String item = String.valueOf(enumeration.nextElement());
				log.debug("item={}", item);
			}

			User user = systemService.getUserByLoginName(loginName);
			if (user == null) {//ldap库有，而本地库没有。
				user = insertToDb(attributes);
			}
			log.debug("——————————> {}", user);
			/*return new SimpleAuthenticationInfo(new SimplePrincipalCollection(new Principal(user, false, null),
					getName()), ldapCredentials);*/
			return new SimpleAuthenticationInfo(new Principal(user, false, null), token.getCredentials(), getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	private User insertToDb(Attributes attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub

		Principal principal = (Principal) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
			Collection<Session> sessions = systemService.getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
			if (sessions.size() > 0) {
				// 如果是登录进来的，则踢出已在线用户
				if (UserUtils.getSubject().isAuthenticated()) {
					for (Session session : sessions) {
						systemService.getSessionDao().delete(session);
					}
				}
				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
				else {
					UserUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
				}
			}
		}
		User user = systemService.getUserByLoginName(principal.getLoginName());
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
			systemService.updateUserLoginInfo(user);
			// 记录登录日志
			LogUtils.saveLog(Servlets.getRequest(), "通过LDAP登录");
			return info;
		} else {
			return null;
		}
	}
}
