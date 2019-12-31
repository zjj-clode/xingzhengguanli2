package com.thinkgem.jeesite.modules.sys.security.csrf;

import java.util.ArrayDeque;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * The Class CSRFTokenService.
 *
 * @author lsp
 */
@Component
public class CSRFTokenService implements DisposableBean {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(CSRFTokenService.class);

	/** 单个session最多缓存多少个token */
	private static final int MAX_TOKEN_PER_USER = 50;

	/** The csrf token cache. */
	private String CSRF_TOKEN_CACHE = "csrf-token-cache";

	/** The csrf token cache id. */
	private String CSRF_TOKEN_CACHE_ID_ = "id_";

	/** The max token per user. */
	private int maxTokenPerUser = MAX_TOKEN_PER_USER;

	/** The csrf token parameter name. */
	private String csrfTokenParameterName = "Csrf-Token";

	/** The csrf token header name. */
	private String csrfTokenHeaderName = "Csrf-Token";

	/**
	 * Sets the csrf token parameter name.
	 *
	 * @param csrfTokenParameterName the new csrf token parameter name
	 */
	public void setCsrfTokenParameterName(String csrfTokenParameterName) {
		this.csrfTokenParameterName = csrfTokenParameterName;
	}

	/**
	 * Sets the csrf token header name.
	 *
	 * @param csrfTokenHeaderName the new csrf token header name
	 */
	public void setCsrfTokenHeaderName(String csrfTokenHeaderName) {
		this.csrfTokenHeaderName = csrfTokenHeaderName;
	}

	/**
	 * Gets the csrf token header name.
	 *
	 * @return the csrf token header name
	 */
	public String getCsrfTokenHeaderName() {
		return csrfTokenHeaderName;
	}

	/**
	 * Gets the csrf token parameter name.
	 *
	 * @return the csrf token parameter name
	 */
	public String getCsrfTokenParameterName() {
		return csrfTokenParameterName;
	}

	/**
	 * 每个用户最多产生多少个token，默认50个.
	 *
	 * @param maxTokenPerUser the new max token per user
	 */
	public void setMaxTokenPerUser(int maxTokenPerUser) {
		this.maxTokenPerUser = maxTokenPerUser;
	}

	/**
	 * 生成新token.
	 *
	 * @param request the request
	 * @return the new token
	 */
	public String generateToken(HttpServletRequest request) {
		return generateToken(request.getSession());
	}

	/**
	 * Generate token.
	 *
	 * @param session the session
	 * @return the new token
	 */
	public String generateToken(HttpSession session) {
		return generateToken(session.getId());
	}

	/**
	 * 生成新token.
	 *
	 * @param sessionId the session id
	 * @return the new token
	 */
	public String generateToken(String sessionId) {
		ArrayDeque<String> arrayDeque = getCache(sessionId);
		if (arrayDeque.size() >= maxTokenPerUser) {
			return arrayDeque.getFirst();
		}
		String token = UUID.randomUUID().toString();
		arrayDeque.add(token);
		logger.debug("new token : {} , arrayDeque --------> {}", token, arrayDeque);
		return token;
	}

	/**
	 * 验证token，验证通过的同时，删除token。<br/>
	 * 验证通过时，会删除已使用的token。注意：当按F5重复提交表单的时候，因为token已过期了，会403.
	 *
	 * @param sessionId the session id
	 * @param token     the token
	 * @return true, if successful
	 */
	public boolean verifyToken(String sessionId, String token) {
		return verifyToken(sessionId, token, true);
	}

	/**
	 * 验证token，验证通过的同时，删除token。.
	 *
	 * @param sessionId the session id
	 * @param token     the token
	 * @param refresh   验证通过时，是否删除已使用的token
	 * @return true, if successful
	 */
	public boolean verifyToken(String sessionId, String token, boolean refresh) {
		ArrayDeque<String> arrayDeque = getCache(sessionId);
		boolean contains = arrayDeque.contains(token);
		logger.debug("userId：{}, contains：{} ,arrayDeque --------> {}", sessionId, contains, arrayDeque);
		// 删除已通过验证的token
		if (contains && refresh) {
			arrayDeque.remove(token);
		}
		return contains;
	}

	/**
	 * 验证token，验证通过的同时，删除token。.
	 *
	 * @param request the request
	 * @param refresh 验证通过时，是否删除已使用的token
	 * @return true, if successful
	 */
	public boolean verifyToken(HttpServletRequest request, boolean refresh) {
		String sessionId = request.getSession().getId();
		String token = getRequstCSRFToken(request);
		return verifyToken(sessionId, token, refresh);
	}

	/**
	 * 验证token，验证通过的同时，删除token。<br/>
	 * 验证通过时，会删除已使用的token。注意：当按F5重复提交表单的时候，因为token已过期了，会403.
	 *
	 * @param request the request
	 * @return true, if successful
	 */
	public boolean verifyToken(HttpServletRequest request) {
		return verifyToken(request, true);
	}

	/**
	 * 获取客户端提交的token，从HTTP头部或者请求参数中获取，优先从参数中获取。
	 *
	 * @param request the request
	 * @return the requst CSRF token
	 */
	public String getRequstCSRFToken(HttpServletRequest request) {
		return getRequstCSRFToken(request, csrfTokenParameterName, csrfTokenHeaderName);
	}

	public String getRequstCSRFToken(HttpServletRequest request, String csrfTokenParameterName, String csrfTokenHeaderName) {
		String token = request.getParameter(csrfTokenParameterName);
		if (StringUtils.isBlank(token)) {
			token = request.getHeader(csrfTokenHeaderName);
		}
		return token;
	}

	/**
	 * 删除token.
	 *
	 * @param sessionId the session id
	 * @return the cache
	 */
	private ArrayDeque<String> getCache(String sessionId) {
		@SuppressWarnings("unchecked")
		ArrayDeque<String> arrayDeque = (ArrayDeque<String>) CacheUtils.get(CSRF_TOKEN_CACHE, CSRF_TOKEN_CACHE_ID_ + sessionId);
		if (arrayDeque == null) {
			arrayDeque = new ArrayDeque<>(maxTokenPerUser);
			putCache(sessionId, arrayDeque);
		}
		return arrayDeque;
	}

	/**
	 * Put cache.
	 *
	 * @param sessionId the session id
	 * @param value     the value
	 */
	private void putCache(String sessionId, ArrayDeque<String> value) {
		CacheUtils.put(CSRF_TOKEN_CACHE, CSRF_TOKEN_CACHE_ID_ + sessionId, value);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		CacheUtils.removeAll(CSRF_TOKEN_CACHE);
	}

}
