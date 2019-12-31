/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinte.modules.log.entity.BusinessLog;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.security.csrf.CSRFTokenService;
import com.thinkgem.jeesite.modules.sys.utils.BusinessLogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 表单验证（包含验证码）过滤类
 *
 * @author ThinkGem
 * @version 2014-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CSRFTokenService csrfTokenService;

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
	public static final String DEFAULT_URL1_PARAM = "url1";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";
	private final String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private final String url1Param = DEFAULT_URL1_PARAM;
	private final String mobileLoginParam = DEFAULT_MOBILE_PARAM;
	private final String messageParam = DEFAULT_MESSAGE_PARAM;

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		if (password == null) {
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = StringUtils.getRemoteAddr((HttpServletRequest) request);
		String captcha = getCaptcha(request);
		String url1 = getUrl1(request);

		boolean mobile = isMobileLogin(request);
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, url1, mobile);
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	public String getUrl1Param() {
		return url1Param;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	public String getMobileLoginParam() {
		return mobileLoginParam;
	}

	protected boolean isMobileLogin(ServletRequest request) {
		return WebUtils.isTrue(request, getMobileLoginParam());
	}

	protected String getUrl1(ServletRequest request) {
		return WebUtils.getCleanParam(request, getUrl1Param());
	}

	public String getMessageParam() {
		return messageParam;
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		//		Principal p = UserUtils.getPrincipal();
		//		if (p != null && !p.isMobileLogin()){
		WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
		//		}else{
		//			super.issueSuccessRedirect(request, response);
		//		}
	}

	/**
	 * 登录成功时
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_GY_LOGIN, "登录系统");
		UserUtils.getSession().setAttribute("AuthenticatingFilter", getClass().getName());
		return super.onLoginSuccess(token, subject, request, response);
	}

	/**
	 * 登录失败
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		String className = e.getClass().getName(), message = "";
		logger.debug("登录失败： e.getClass()--->{}；e.getMessage()---->{}", className, e.getMessage());
		if (IncorrectCredentialsException.class.getName().equals(className) || UnknownAccountException.class.getName().equals(className)) {
			message = "用户或密码错误, 请重试.";
		} else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
			message = StringUtils.replace(e.getMessage(), "msg:", "");
		} else if (AuthenticationException.class.getName().equals(className)) {
			message = "登录失败：可能系统不存在您的账号";
		} else {
			message = "登录失败：系统出现点问题，请稍后再试！";
			e.printStackTrace(); // 输出到控制台
		}
		request.setAttribute(getFailureKeyAttribute(), className);
		request.setAttribute(getMessageParam(), message);
		return true;
	}

	/**
	 * 登录执行前
	 */
	@Override
	protected boolean preHandle(ServletRequest req, ServletResponse resp) throws Exception {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		/**
		 * 防止 CSRF
		 */
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			logger.debug("CSRF--------------->");
			if (!csrfTokenService.verifyToken(request)) {
				WebUtils.issueRedirect(request, response, getLoginUrl(), null, true);
				logger.debug("verify token false");
				return false;
			}
		}

		return super.preHandle(request, response);
	}

}