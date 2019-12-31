/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.UserAgentUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 客户端浏览器拦截器。拦截指定的浏览器{@link eu.bitwalker.useragentutils.Browser}，主要用于提示用户本系统不支持IE低版本浏览器。
 */
public class UserAgentInterceptor extends BaseService implements HandlerInterceptor {

	private static final String FAIL_PAGE = "/alert/kill-ie.jsp";
	private String failPage = FAIL_PAGE;

	/**
	 * 浏览器被拒时显示的页面，默认为 /alert/kill-ie.jsp
	 */
	public void setFailPage(String failPage) {
		this.failPage = failPage;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if ("GET".equals(request.getMethod().toUpperCase())) {
			UserAgent userAgent = UserAgentUtils.getUserAgent(request);
			logger.debug("userAgent---> {}", userAgent);
			Browser browser = userAgent.getBrowser();
			logger.debug("browser---> {}", browser);

			// 获取配置中过滤的浏览器。
			String[] filteBrowsers = SettingsUtils.getSysConfig("sys.filte.browsers", "IE6,IE7,IE8").toUpperCase().split(",");
			if (filteBrowsers != null && filteBrowsers.length > 0) {
				Set<String> filteBrowsersSet = new HashSet<String>();
				for (String s : filteBrowsers) { // 去重复，去空格
					if (!StringUtils.isBlank(s)) {
						filteBrowsersSet.add(StringUtils.trimToEmpty(s));
					}
				}
				//
				if (filteBrowsersSet.contains(browser.toString())) {
					if (StringUtils.isBlank(failPage)) {
						failPage = FAIL_PAGE;
					} else {
						if (!failPage.startsWith("/")) {
							failPage = "/" + failPage;
						}
					}
					//response.sendRedirect("/".equals(request.getContextPath()) ? "" : request.getContextPath() + failPage);
					request.getRequestDispatcher(failPage).forward(request, response);
					return false;
				}
			}
		}
		//
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
