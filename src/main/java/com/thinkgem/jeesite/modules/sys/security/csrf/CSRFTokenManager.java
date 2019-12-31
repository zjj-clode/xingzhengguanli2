package com.thinkgem.jeesite.modules.sys.security.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;

/**
 * CSRF Token 管理器
 * 
 * @deprecated 改用 {@link CSRFTokenService}
 */
@Deprecated
public final class CSRFTokenManager {

	private static CSRFTokenService csrfTokenService = SpringContextHolder.getBean(CSRFTokenService.class);

	private CSRFTokenManager() {
	};

	// 隐藏域CSRF Token参数名称
	public static final String CSRF_PARAM_NAME = "CSRFToken";

	// session中CSRF Token参数名称
	public static final String CSRF_TOKEN_FOR_SESSION_ATTR_NAME = CSRFTokenManager.class.getName() + ".tokenval";

	// 从session中获取（没有就创建）CSRF Token
	public static String getTokenForSession(HttpSession session) {
		String token = null;
		// 同步， 同一个session只允许生成一个token
		synchronized (session) {
			token = (String) session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
			if (null == token) {
				// token = UUID.randomUUID().toString();
				// 将token产生器改为使用csrfTokenService
				token = csrfTokenService.generateToken(session);
				session.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME, token);
			}
		}
		return token;
	}

	/**
	 * 从request请求参数中获取CSRF Token
	 */
	public static String getTokenFromRequest(HttpServletRequest request) {
		return request.getParameter(CSRF_PARAM_NAME);
	}

	public static boolean checkCSRFToken(HttpServletRequest request) {
		String sessionToken = getTokenForSession(request.getSession());
		String requestToken = getTokenFromRequest(request);
		if (sessionToken == null) {
			return true;
		} else {
			return sessionToken.equals(requestToken);
		}
	}
}
