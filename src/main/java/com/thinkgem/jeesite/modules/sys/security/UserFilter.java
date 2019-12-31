package com.thinkgem.jeesite.modules.sys.security;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mapper.ResponseObject;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;

/**
 * 重写redirectToLogin，当Ajax请求时，返回json提示要求登录，而不是重定向到登录URL。<br>
 * 此Filter拦截未登录的请求（subject.getPrincipal() == null），对应配置文件里类似：
 *
 * <pre>
 * ${adminPath}/** = user
 * </pre>
 *
 * @author 廖水平
 *
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		logger.debug("redirectToLogin()......");

		HttpServletRequest req = (HttpServletRequest) request;
		// ajax请求，返回json
		if (Servlets.isAjaxRequest(req)) {
			try {
				response.reset();
				response.setContentType("application/json");
				response.setCharacterEncoding("utf-8");
				String msg = JsonMapper.toJsonString(new ResponseObject(ResponseObject.STATE_REQUIRES_USER, ResponseObject.MSG_REQUIRES_USER));
				response.getWriter().print(msg);

				//
				String referer = req.getHeader("Referer");
				if (StringUtils.isNotBlank(referer)) {
					logger.debug("需要登录，登录后跳转url=Referer={}", referer);
					Servlets.setRedirectUrlToSession(req, referer);
				}
				//

				logger.debug("redirectToLogin()...{}", msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 普通请求，返回页面
		} else {

			try {

				//// 如果是GET请求，将请求路径保存起来，待登录后再跳转到此路径。
				String method = req.getMethod();
				boolean isGetRequest = "GET".equalsIgnoreCase(method);
				String requestUrlWithOutContextPath = null; //
				if (isGetRequest) {
					String requestURI = req.getRequestURI();
					String queryString = req.getQueryString();
					logger.debug("method ---> {}, requestURI ---> {}, queryString ---> {}", method, requestURI, queryString);
					if (!StringUtils.isBlank(req.getContextPath())) {
						requestURI = requestURI.substring(req.getContextPath().length());//去掉contextPath部分
					}
					requestUrlWithOutContextPath = requestURI;
					StringBuilder requestUrl = new StringBuilder(requestURI);
					if (queryString != null) {
						requestUrl.append("?").append(queryString);
					}
					String url = requestUrl.toString();
					logger.debug("需要登录，请求url ---> {}", url);
					Servlets.setRedirectUrlToSession(req, url);

				}

				// POST请求，登录后跳转到Referer
				else if ("POST".equalsIgnoreCase(method)) {
					//
					String referer = req.getHeader("Referer");
					if (StringUtils.isNotBlank(referer)) {
						logger.debug("需要登录，登录后跳转url=Referer={}", referer);
						Servlets.setRedirectUrlToSession(req, referer);
						//
						requestUrlWithOutContextPath = fromReferer(req, referer);
					}
					//
				}
				////

				// 根据请求url取得跳转的登录页面，前台还是后台
				logger.debug("requestUrlWithOutContextPath ---------> {}", requestUrlWithOutContextPath);
				if (StringUtils.isNotBlank(requestUrlWithOutContextPath)) {
					logger.debug("自定义登录页配置urlLoginUrlMap ---------> {}", urlLoginUrlMap);
					if (urlLoginUrlMap != null && !urlLoginUrlMap.isEmpty()) {
						for (Entry<String, String> entry : urlLoginUrlMap.entrySet()) {
							if (entry != null && StringUtils.isNotBlank(entry.getKey())) {
								if (antPathMatcher.match(entry.getKey(), requestUrlWithOutContextPath)) {
									String loginUrl = entry.getValue();
									logger.debug("跳转到自定义登录页 loginUrl ---------> {}", loginUrl);
									WebUtils.issueRedirect(request, response, loginUrl);
									return;
								}
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// 默认登录页面
			super.redirectToLogin(request, response);

		}
	}

	private String fromReferer(HttpServletRequest request, String referer) {
		return StringUtils.replace(referer, Servlets.getBasePath(request), "");
	}

	private LinkedHashMap<String, String> urlLoginUrlMap;

	/**
	 * 请求url <-----> 如果没有登录，跳转到哪个登录url
	 */
	public void setUrlLoginUrlMap(LinkedHashMap<String, String> urlLoginUrlMap) {
		this.urlLoginUrlMap = urlLoginUrlMap;
	}

	private AntPathMatcher antPathMatcher = new AntPathMatcher();

}
