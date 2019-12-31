package com.thinkgem.jeesite.common.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * <pre>
 * 可关闭系统的服务端(/a)和客户端(/f)，重定向到指定页面（提示：系统维护中。。。）。
 * 开关在后台的参数配置中配置：sys.front.shutdown和sys.admin.shutdown
 * </pre>
 */
public class SecurityFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String A_PAGE_PARAM_NAME = "a";
	private static final String F_PAGE_PARAM_NAME = "f";

	private String aPage = "/WEB-INF/views/error/a.jsp";
	private String fPage = "/WEB-INF/views/error/f.jsp";

	private Map<String, Object> msgMap;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String servletPath = req.getServletPath();
		//logger.debug("servletPath ----->{}", servletPath);

		if (servletPath.startsWith(Global.getFrontPath())//
				// 留下验证码显示入口
				&& !servletPath.startsWith(Global.getFrontPath() + "/slide") //验证码
				&& !servletPath.startsWith("/servlet/validateCodeServlet") //验证码
		) {
			//logger.debug("servletPath ----->{}", servletPath);
			
			boolean shutdownFront = SettingsUtils.getSysConfig("sys.front.shutdown", false);
			//logger.debug("sys.front.shutdown ----->{}", shutdownFront);
			if (shutdownFront) {
				if (Servlets.isAjaxRequest(req)) {
					renderErrorStr(response);
				} else {
					req.getRequestDispatcher(fPage).forward(request, response);
				}
				return;
			}
		} else if (servletPath.startsWith(Global.getAdminPath())//
				// 留下登录系统和修改参数的入口
				&& !servletPath.startsWith(Global.getAdminPath() + "/login") //登录系统
				&& !servletPath.startsWith(Global.getAdminPath() + "/sys/settings") //修改参数
		) {
			//logger.debug("servletPath ----->{}", servletPath);

			boolean shutdownAdmin = SettingsUtils.getSysConfig("sys.admin.shutdown", false);
			//logger.debug("sys.admin.shutdown ----->{}", shutdownAdmin);
			if (shutdownAdmin) {
				if (Servlets.isAjaxRequest(req)) {
					renderErrorStr(response);
				} else {
					req.getRequestDispatcher(aPage).forward(request, response);
				}
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private void renderErrorStr(ServletResponse response) {
		if (msgMap == null) {
			msgMap = new HashMap<>();
			msgMap.put("success", false);
			msgMap.put("message", "系统维护中。。。");
		}
		//
		try {
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(JsonMapper.toJsonString(msgMap));
		} catch (IOException e) {
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String aPage = config.getInitParameter(A_PAGE_PARAM_NAME);
		if (!StringUtils.isBlank(aPage)) {
			this.aPage = aPage;
		}
		String fPage = config.getInitParameter(F_PAGE_PARAM_NAME);
		if (!StringUtils.isBlank(fPage)) {
			this.fPage = fPage;
		}
	}

	@Override
	public void destroy() {
	}

}