package com.thinkgem.jeesite.modules.sys.security.csrf;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.NameableFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mapper.ResponseObject;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;

/**
 * CSRF 攻击检查 ，拦截所有的请求，处理POST请求。
 */
public class CsrfFilter extends NameableFilter {

	@Autowired
	private CSRFTokenService csrfTokenService;

	protected Logger logger = LoggerFactory.getLogger(CsrfFilter.class);

	private String redirectUrl = "/f/qa/question";

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		logger.debug("request.getRequestURI()--->{}", request.getRequestURI());

		if ("POST".equalsIgnoreCase(request.getMethod())) {

			String token = csrfTokenService.getRequstCSRFToken(request);

			if (token != null) { // Csrf-Token ，新版

				String sessionId = request.getSession().getId();

				if (!csrfTokenService.verifyToken(sessionId, token, false)) {

					logger.warn("verifyToken failure , sessionId = {} , token = {}", sessionId, token);

					if (Servlets.isAjaxRequest(request)) {
						doAjaxResponse(response);
					} else {
						response.sendError(403);
					}

					// 验证不通过
					return;

				} else {
					// 验证通过
				}

			} else { // CSRFToken ，旧版

				String sessionToken = CSRFTokenManager.getTokenForSession(request.getSession());
				String requestToken = CSRFTokenManager.getTokenFromRequest(request);

				logger.debug("requestToken--->{}, sessionToken--->{}", requestToken, sessionToken);

				if (StringUtils.isBlank(requestToken)) {

					logger.warn("requestToken is blank");

					if (Servlets.isAjaxRequest(request)) {
						doAjaxResponse(response);
						// 普通请求，返回页面
					} else {
						response.sendRedirect(request.getContextPath() + redirectUrl);
					}

					return;

				} else {

					if (!Objects.equals(sessionToken, requestToken)) {

						logger.warn("sessionToken[{}] does not equals requestToken[{}]", sessionToken, requestToken);

						if (Servlets.isAjaxRequest(request)) {
							doAjaxResponse(response);
						} else {
							response.sendError(403);
						}

						return;
					}

				}

			}
		}
		//
		chain.doFilter(request, response);
	}

	private void doAjaxResponse(HttpServletResponse response) {
		try {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			String msg = JsonMapper.toJsonString(ResponseObject.failure().msg("非法操作"));
			response.getWriter().print(msg);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
