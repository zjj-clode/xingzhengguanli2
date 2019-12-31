package com.thinkgem.jeesite.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 拦截处理CAS认证请求
 *
 * @author 廖水平
 */
@Service
public class CasFilter extends org.apache.shiro.cas.CasFilter {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String DEFAULT_ERROR_KEY_ATTRIBUTE_NAME = "shiroLoginFailure";
	public static final String DEFAULT_MESSAGE_PARAM = "message";
	private static final String TICKET_PARAMETER = "ticket";
	/** 用于接收其他参数，可以将url整个编码成参数值 */
	public static final String P_PARAMETER = ".p";
	
	private String failureUrl;
	
	private String logoutParameterName = "logoutRequest";
	
	/**
	 * 登录成功回调
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		LogUtils.saveLog(Servlets.getRequest(), "统一身份认证登录");
		UserUtils.getSession().setAttribute("AuthenticatingFilter", getClass().getName());
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	/**
	 * 登录失败调用事件<br/>
	 * 注意：如果return false，错误信息message是传不到登录页面的(request、session都不行)，因为session id会变化。<br/>
	 * 可以改为return true，然后在LoginController参照loginFail方法写一个casLoginFail方法（响应${adminPath}/casLogin请求），这样错误信息就能传递过去了。
	 *
	 * @return 是否继续走过滤器，true是，false否
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		
		String className = e.getClass().getName(), message = "";
		
		logger.debug("登录失败： e.getClass()--->{}；e.getMessage()---->{}", className, e.getMessage());
		
		if (IncorrectCredentialsException.class.getName().equals(className) || UnknownAccountException.class.getName().equals(className)) {
			message = "用户或密码错误, 请重试.";
		} else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
			message = StringUtils.replace(e.getMessage(), "msg:", "");
		} else if (CasAuthenticationException.class.getName().equals(className)) {
			// message = "登录失败：统一身份认证失败";
			message = e.getMessage();
		} else if (AuthenticationException.class.getName().equals(className)) {
			message = "登录失败：可能系统不存在您的账号";
		} else {
			message = "登录失败：系统出现点问题，请稍后再试！";
			e.printStackTrace(); // 输出到控制台
		}
		
		logger.debug("message--->{}", message);
		
		request.setAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, className);
		request.setAttribute(DEFAULT_MESSAGE_PARAM, message);
		
		logger.debug("session.getId()--->{}", Servlets.getRequest().getSession().getId());
		
		// 不重定向
		return true;
	}
	
	/**
	 * <pre>
	 * 退出登录步骤：
	 * 1、本机logout：http://202.205.88.66/akmi/a/logout
	 * logoutFilter 配置了 redirectUrl： http://authserver.cau.edu.cn/authserver/logout?service=http://202.205.88.66/akmi/a/login
	 * 2、重定向cas服务器logout，附带service参数，用于返回本机url：http://authserver.cau.edu.cn/authserver/logout?service=http://202.205.88.66/akmi/a/login
	 * cas服务器执行登出
	 * 3、重定向本机login：http://202.205.88.66/akmi/a/login
	 * 此情况下，在cas服务器执行登出时，cas服务器还会使用httpclient给本地服务器发送一个POST请求，通知本机也执行登出，类似如：
	 * POST /akmi/a/cas?logoutSessionKey=ST-984341-wUq0jzq6edwbqyfpkoMM1488416951375-eqXb-cas
	 * 因为本机已经执行过退出操作，这里将这个请求忽略掉，否则的话会报异常：没有realm处理这个请求。
	 * 参照 org.jasig.cas.client.session.SingleSignOutFilter 、org.jasig.cas.client.session.SingleSignOutHandler
	 *
	 *
	 * 统一登出的另外一个实现思路是：改造SingleSignOutFilter和SingleSignOutHandler，把处理http Session改为处理shiro Session，
	 * 因为本机采用的是shiro Session
	 * </pre>
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		
		String p = request.getParameter(P_PARAMETER);
		
		HttpServletRequest req = (HttpServletRequest) request;
		//get 请求 http://202.205.88.66/super-weapon/a/cas ，不带ticket参数时不验证(防止报错)，直接跳转到failureUrl
		
		boolean isGetMethod = "GET".equalsIgnoreCase(req.getMethod());
		boolean notHasTicketParameter = StringUtils.isBlank(req.getQueryString()) || req.getQueryString().indexOf(TICKET_PARAMETER) == -1;
		
		if (isGetMethod && notHasTicketParameter) {
			
			if (!StringUtils.isBlank(p)) {
				WebUtils.issueRedirect(request, response, getLoginUrl() + "?" + P_PARAMETER + "=" + p);
			} else {
				WebUtils.issueRedirect(request, response, getLoginUrl());
			}
			
			return false;
		}
		//
		if (isLogoutRequest(req)) {
			return false;
		}
		
		return super.preHandle(request, response);
	}
	
	private boolean isLogoutRequest(HttpServletRequest request) {
		return "POST".equals(request.getMethod()) && !isMultipartRequest(request) && CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, getLogoutParameterName()));
	}
	
	private boolean isMultipartRequest(final HttpServletRequest request) {
		return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
	}
	
	public String getLogoutParameterName() {
		return logoutParameterName;
	}
	
	public void setLogoutParameterName(String logoutParameterName) {
		this.logoutParameterName = logoutParameterName;
	}
	
	public String getFailureUrl() {
		return failureUrl;
	}
	
	@Override
	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
		super.setFailureUrl(failureUrl);
	}
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		//return super.createToken(request, response);
		// 重写，增加一个参数.p
		String ticket = request.getParameter(TICKET_PARAMETER);
		String p = request.getParameter(P_PARAMETER);
		return new CasToken(ticket, p);
	}
	
}
