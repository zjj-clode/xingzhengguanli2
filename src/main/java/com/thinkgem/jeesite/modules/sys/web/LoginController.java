package com.thinkgem.jeesite.modules.sys.web;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;
import org.sword.lang.HttpUtils;

//import com.cloudinte.modules.wechat.service.WechatUtil;
import com.cloudinte.zhaosheng.modules.common.entity.APIResponseObject;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.QueryStringUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.UserAgentUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.CasRealm;
import com.thinkgem.jeesite.modules.sys.security.FormAuthenticationFilter;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.security.UsernamePasswordToken;
import com.thinkgem.jeesite.modules.sys.security.csrf.CSRFTokenService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.LoginUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.wechat.utils.WechatUtil;

import net.sf.json.JSONObject;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private SystemService systemService;

	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private CSRFTokenService csrfTokenService;

	/** Map<用户类型,登录成功跳转地址> 在spring-context-shiro.xml 文件中定义 */
	@Resource(name = "userTypeLoginSuccessUrlMap")
	private Map<String, String> userTypeLoginSuccessUrlMap;

	@RequestMapping("${frontPath}/toQQ")
	public String toQQ() {
		//url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + appid + "&redirect_uri=" + redirectURI + "&state=" + state
		String appid = Global.getConfig("QQappid");
		String redirectURI = Global.getConfig("QQredirect_uri");

		String url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + appid + "&redirect_uri=" + redirectURI;

		return "redirect:" + url;
	}

	@RequestMapping("${adminPath}/QQlogin")
	public String QQlogin(String code, Model model) {

		String appid = Global.getConfig("QQappid");
		String appkey = Global.getConfig("appkey");
		String redirectURI = Global.getConfig("QQredirect_uri");
		String accessTokenUrl = "https://graph.qq.com/oauth2.0/token? grant_type=authorization_code&client_id=" + appid + "&client_secret=" + appkey + "&redirect_uri=" + redirectURI + "&code=" + code;
		//获取access_token
		String accessTokenResult = HttpUtils.get(accessTokenUrl);
		JSONObject jobj = JSONObject.fromObject(accessTokenResult);
		String accessToken = "";
		if (jobj.containsKey("access_token")) {
			accessToken = jobj.getString("access_token");
		}
		logger.debug("accessToken-------->{}", accessToken);
		//获取openid
		String openIdUrl = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
		String openIdResult = HttpUtils.get(openIdUrl);
		logger.debug("openIdResult-------->{}", openIdResult);
		JSONObject jobj2 = JSONObject.fromObject(openIdResult);
		String openId = "";
		if (jobj2.containsKey("openid")) {
			openId = jobj2.getString("openid");
		}

		if (StringUtils.isBlank(openId)) {
			model.addAttribute("message", "QQ登录失败");
			return "redirect:" + adminPath + "/loginf";
		}

		String result = HttpUtils.get("https://graph.qq.com/user/get_user_info?access_token=" + accessToken + "&oauth_consumer_key=" + appid + "&openid=" + openId);
		logger.debug("result-------->{}", result);
		return null;
	}

	@RequestMapping("${frontPath}/toWechat")
	public String toWechat() {
		String appid = WechatUtil.getWexinAppid();
		String redirectUrl = WechatUtil.getWexinUrl();

		String takenUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		takenUrl = takenUrl.replace("APPID", appid);
		takenUrl = takenUrl.replace("REDIRECT_URI", redirectUrl);
		takenUrl = takenUrl.replace("SCOPE", "snsapi_login");
		return "redirect:" + takenUrl;
	}

	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		Principal principal = UserUtils.getPrincipal();

		// 如果用户已登录
		if (principal != null) {
			String from = request.getParameter("from");
			// 从微信过来的，跳转到url指定的地址
			if ("weixin".equals(from)) {
				String backUrl = request.getParameter("url");
				if (!StringUtils.isBlank(backUrl)) {
					// 还原编码的地址
					backUrl = QueryStringUtils.getBase64DecodedQueryString(backUrl);
					if (LoginUtils.isSecureUrl(request, backUrl)) {
						return "redirect:" + backUrl;
					}
				}
			}
			// 其他情况
			return "redirect:" + adminPath;
		}
		// 未登录

		String url = HtmlUtils.htmlEscape(request.getParameter("url"));
		if (!StringUtils.isBlank(url)) {
			model.addAttribute("url", url);
			Servlets.setRedirectUrlToSession(request, url);
		}

		return toLoginPage(request);
	}

	/**
	 * 登录的POST请求被{@link FormAuthenticationFilter} 拦截进行认证处理，当FormAuthenticationFilter认证失败时本方法才会执行。
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		logger.debug("loginFail()......");
		Principal principal = UserUtils.getPrincipal();
		// 如果用户已登录
		if (principal != null) {
			String from = request.getParameter("from");
			// 从微信过来的，跳转到url指定的地址
			if ("weixin".equals(from)) {
				String backUrl = request.getParameter("url");
				if (!StringUtils.isBlank(backUrl)) {
					// 还原编码的地址
					backUrl = QueryStringUtils.getBase64DecodedQueryString(backUrl);
					if (LoginUtils.isSecureUrl(request, backUrl)) {
						return "redirect:" + backUrl;
					}
				}
			}
			// 其他情况
			return "redirect:" + adminPath;
		}
		// 登录失败时，提示相关信息到登录界面
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		if (!username.matches("^[A-Za-z0-9]+$")) {
			username = null;
		}
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

		logger.debug("exception--->{},message--->{}", exception, message);

		if (StringUtils.isBlank(message) || "null".equals(message)) {
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

		if (logger.isDebugEnabled()) {
			logger.debug("login fail, active session size: {}, message: {}, exception: {}", sessionDAO.getActiveSessions(false).size(), message, exception);
		}

		// 非授权异常，登录失败次数 +1
		if (!UnauthorizedException.class.getName().equals(exception)) {
			model.addAttribute("isValidateCodeLogin", LoginUtils.isValidateCodeLogin(username, true, false));
		}

		// 登录失败，清空本次打开的验证码信息。
		request.getSession().setAttribute(SlideController.SESSION_KEY_SLIDE_VALID, null);

		return toLoginPage(request);
	}

	private String toLoginPage(HttpServletRequest request) {
		
		if (UserAgentUtils.isMobileOrTablet(request)) {
			return basicDir + "/user/sysFrontLogin";
		}
		
		return basicDir + "/user/sysLogin";
	}

	@RequiresPermissions("user")
	@RequestMapping(value = { "${adminPath}" }, method = { RequestMethod.GET })
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		LoginUtils.isValidateCodeLogin(principal.getLoginName(), false, true);

		User user = UserUtils.getUser();

		if (logger.isDebugEnabled()) {
			logger.debug("show index, active session size: {}", Integer.valueOf(sessionDAO.getActiveSessions(false).size()));
		}

		// 未激活的注册用户，强制退出登录
		if (principal != null && SettingsUtils.getSysConfig("sys.register.needActive", false) && Global.NO.equals(user.getActive())) {
			UserUtils.getSubject().logout();
			request.setAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, "登录失败：此账号还未激活");
			return toLoginPage(request);
		}

		return toSuccessUrl(request, user);
	}

	/**
	 * 登录成功后redirect跳转地址<br/>
	 * <p>
	 * 优先级：<br/>
	 * 1、通过Servlets.getAndClearRedirectUrlFromSession(request)获取到登录后跳转url<br/>
	 * 2、通过userTypeLoginSuccessUrlMap获取到登录后跳转url<br/>
	 * 3、默认登录后跳转url<br/>
	 * </p>
	 */
	private String toSuccessUrl(HttpServletRequest request, User user) {
		// 登录后跳转url = 登录前保存的url
		String url = Servlets.getAndClearRedirectUrlFromSession(request);
		logger.debug("getAndClearRedirectUrlFromSession url ---> {}", url);
		if (LoginUtils.isSecureUrl(request, url)) {
			return "redirect:" + url;
		}
		// 如果没有指定登录后的url，按照用户类型来显示不同的视图。
		String userType = StringUtils.trimToEmpty(user.getUserType());
		logger.debug("userType--->{}，userTypeLoginSuccessUrlMap--->{}", userType, userTypeLoginSuccessUrlMap);
		if (!StringUtils.isBlank(userType) && userTypeLoginSuccessUrlMap != null && !userTypeLoginSuccessUrlMap.isEmpty()) {
			String successUrl = userTypeLoginSuccessUrlMap.get(userType);
			logger.debug("successUrl--->{}", successUrl);
			if (!StringUtils.isBlank(successUrl)) {
				return "redirect:" + successUrl;
			}
		}
		// 默认
		return "redirect:" + adminPath + "/home";
	}

	@RequestMapping({ "${adminPath}/Index" })
	public String getSysIndex(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/sysAdminIndex";
	}

	@RequestMapping({ "/theme/{theme}" })
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotBlank(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
		} else {
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:" + HtmlUtils.htmlEscape(request.getParameter("url"));
	}

	public SystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}

	private void onLogin(Session session, HttpServletRequest request) {
		session.setAttribute("deviceType", UserAgentUtils.isMobileOrTablet(request) ? "4" : "1");
	}

	/**
	 * 登录页面检查是否需要验证码，需要根据页面上待提交的账号来判断。
	 */
	@ResponseBody
	@RequestMapping(value = "/login/needValidateCode")
	public boolean needValidateCode(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		if (!StringUtils.isBlank(username)) {
			return LoginUtils.isValidateCodeLogin(username, false, false);
		}
		return true;
	}

	/**
	 * 登录页面检查是否被锁定，需要根据页面上待提交的账号来判断。
	 */
	@ResponseBody
	@RequestMapping(value = "/login/isLocked")
	public boolean isLocked(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		if (!StringUtils.isBlank(username)) {
			return LoginUtils.isLockedLogin(username, false, false);
		}
		return true;
	}

	/**
	 * 是否被锁定，是否被锁定。
	 */
	@ResponseBody
	@RequestMapping(value = "/login/check")
	public Map<String, Boolean> needValidateCodeOrIsLocked(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		Map<String, Boolean> map = Maps.newHashMap();
		map.put("needValidateCode", LoginUtils.isValidateCodeLogin(username, false, false));
		map.put("isLocked", LoginUtils.isLockedLogin(username, false, false));
		return map;
	}

	/**
	 * 前台咨询登录界面
	 */
	@RequestMapping(value = "${adminPath}/loginf", method = RequestMethod.GET)
	public String loginf(HttpServletRequest request, Model model) {
		Principal principal = UserUtils.getPrincipal();
		//如果用户已登录，直接进入首页！
		if (principal != null) {
			return "redirect:" + adminPath+"/hf/apply";
		} else {
			String url = WebUtils.getCleanParam(request, "url"); // 登录后跳转url
			if (url != null && url.startsWith(frontPath)) {
				Servlets.setRedirectUrlToSession(request, url); // 放到session中，稍后使用
			}
			return this.basicDir + "/user/sysFrontLogin";
		}
	}

	/**
	 * 前台咨询登录。不用shiro认证filter： ${adminPath}/loginf = anon
	 */
	@RequestMapping(value = "${adminPath}/loginf", method = RequestMethod.POST)
	public String loginf(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttribute) {
		Principal principal = UserUtils.getPrincipal();
		// 如果用户已登录，直接进入首页！
		if (principal != null) {
			return "redirect:" + adminPath+"/hf/apply";
		}

		// csrf
		if (!csrfTokenService.verifyToken(request)) {
			logger.warn("verify token false");
			return "redirect:" + adminPath + "/loginf";
		}

		//
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		String password = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM);
		String validateCode = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_CAPTCHA_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		logger.debug("username :  {}， validateCode :  {}， rememberMe :  {}", username, validateCode, rememberMe);
		if (username != null) {
			username = HtmlUtils.htmlEscape(username);
			logger.debug("htmlEscape username :  {} ", username);
		}
		//
		boolean loiginsuccess = false; //登录是否成功
		String message = ""; // 登录失败提示信息
		boolean isValidcode = true; // 验证码是否正确

		// 1、 校验登录验证码
		if (!ValidateCodeServlet.validate(request, validateCode)) {
			message = "登录失败：验证码错误.";
			logger.debug("验证码错误，validateCode ： {} , session validate code : {} ", validateCode, request.getSession().getAttribute(ValidateCodeServlet.VALIDATE_CODE));
			isValidcode = false;
		}
		// 2、 校验账号密码 ， 根据本地库或者学校数字校园库
		if (isValidcode) {
			User user = UserUtils.getByLoginName(username); //从本地库中查找
			if (user != null) { //本地库中存在此用户
				if (StringUtils.isNotEmpty(password)) {
					/*if (StringUtils.isNotEmpty(user.getPassword())) {

						// 解密前台JS加密后的密码
						password = LoginUtils.decrypt(password);

						loiginsuccess = SystemService.validatePassword(password, user.getPassword()); //比对本地库密码
						logger.debug("SystemService.validatePassword-->{}", loiginsuccess);
					}*/
				    loiginsuccess = SystemService.validatePassword(password, user.getPassword()); //比对本地库密码
				}
				if (!loiginsuccess) {
					message = "账号或密码错误";
				} else {
					// 未激活的注册用户
					if (SettingsUtils.getSysConfig("sys.register.needActive", true) && Global.NO.equals(user.getActive())) {
						Subject subject = UserUtils.getSubject();
						if (subject != null) {
							subject.logout();
						}
						request.setAttribute("message", "登录失败：此账号还未激活.");
						return this.basicDir + "/user/sysFrontLogin";
					}
				}
			} else { //本地库中不存在此用户
				//message = "登录失败：账号不存在.";
				message = "账号或密码错误";
			}
		}

		if (loiginsuccess) {// 登录成功

			UsernamePasswordToken token = new UsernamePasswordToken();
			token.setUsername(username);
			token.setCaptcha(validateCode);
			token.setRememberMe(rememberMe);
			token.setIsdirect(true); // 直接登录，系统不再验证密码
			SecurityUtils.getSubject().login(token);
			onLogin(SecurityUtils.getSubject().getSession(), request);
			LoginUtils.isValidateCodeLogin(username, false, true); // 清除验证码错误记录

			// 获取并清除session中的url
			String url = Servlets.getAndClearRedirectUrlFromSession(request);

			User user = UserUtils.getUser();
			/*if (user.getProvince() == null || user.getKelei() == null || user.getMobile() == null || user.getName() == null) {
				redirectAttribute.addAttribute("userid", user.getId());
				return "redirect:" + frontPath + "/sys/user/perfectInfo";
			}
			if (!StringUtils.isBlank(url) && LoginUtils.isSecureUrl(request, url)) {
				return "redirect:" + url;
			} else {
				// return "redirect:" + frontPath;
				// 前台登录默认跳转到招生咨询
				return "redirect:" + frontPath + "/qa/question";
			}*/
			return "redirect:" + adminPath+"/hf/apply";
		} else { // 登录失败

			model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
			model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
			model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
			// 登录界面要验证码 ？
			model.addAttribute("isValidateCodeLogin", LoginUtils.isValidateCodeLogin(username, true, false));
			request.getSession().setAttribute("validateCode", IdGen.uuid()); // 废除session中的验证码
			return this.basicDir + "/user/sysFrontLogin";
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 身份切换：实现管理员使用别人账号登录效果。<br/>
	 * 此方法授权后可以随意使用别人身份登录，无需他的授权。<br/>
	 * 当前限制只有管理员（id=1）才能执行。<br/>
	 */
	//@RequiresPermissions("sys:user:userRunAs")
	@ResponseBody
	@RequestMapping("${adminPath}/sys/userRunAs")
	public APIResponseObject runAs(@RequestParam(required = false) String loginName, RedirectAttributes redirectAttributes) {
		User loginUser = UserUtils.getUser();
		if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
			return new APIResponseObject(APIResponseObject.STATE_FAILURE, "操作失败：请先登录。");
		}
		if (!loginUser.isAdmin()) {
			return new APIResponseObject(APIResponseObject.STATE_FAILURE, "操作失败：超级管理员才有权执行此操作。");
		}
		if (StringUtils.isBlank(loginName)) {
			return new APIResponseObject(APIResponseObject.STATE_FAILURE, "操作失败：请填写要登录的账号。");
		}
		if (loginUser.getLoginName().equals(loginName)) {
			return new APIResponseObject(APIResponseObject.STATE_FAILURE, "操作失败：自己不能切换到自己的身份。");
		}
		User switchToUser = systemService.getUserByLoginName(loginName);
		if (switchToUser == null) {
			return new APIResponseObject(APIResponseObject.STATE_FAILURE, "操作失败：账号【" + loginName + "】不存在。");
		}

		//
		process(switchToUser);

		return new APIResponseObject(APIResponseObject.STATE_SUCCESS, "您已成功以【" + loginName + "】账号登录。");
	}

	/**
	 * 处理身份切换，包括权限、缓存的处理。
	 */
	private void process(User switchToUser) {
		Principal principal = new Principal(switchToUser, false, null);
		Subject subject = SecurityUtils.getSubject();
		String realmName = subject.getPrincipals().getRealmNames().iterator().next();
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, realmName);
		subject.runAs(principals);
		//清除缓存中的权限，后续操作时会重新授权。
		RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		Collection<Realm> realms = realmSecurityManager.getRealms();
		Realm realm = realms.iterator().next();
		//切换用户，session并没有变化，跟session相关的缓存需要清理。
		//清除授权缓存
		if (realm instanceof SystemAuthorizingRealm) {
			SystemAuthorizingRealm authorizingRealm = (SystemAuthorizingRealm) realm;
			authorizingRealm.clearCachedAuthorizationInfo(principal);
		} else if (realm instanceof CasRealm) {
			CasRealm authorizingRealm = (CasRealm) realm;
			authorizingRealm.clearCachedAuthorizationInfo(principal);
		}
		//清除用户缓存
		UserUtils.clearCache();
	}
}