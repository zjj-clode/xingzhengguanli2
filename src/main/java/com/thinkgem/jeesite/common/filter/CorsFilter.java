package com.thinkgem.jeesite.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前后端分离接口测试用，部署时在web.xml文件中去掉其配置。
 * 
 * @author lsp
 *
 */
public class CorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));//不可设为*
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,HEAD,DELETE,PUT");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Authorization,Accept,Origin,User-Agent,Content-Range,Content-Disposition,Content-Description,Set-Cookie,Cookie,Csrf-Token,X-Requested-Time");
		response.setHeader("Access-Control-Expose-Headers", "X-Requested-With,Content-Type,Authorization,Accept,Origin,User-Agent,Content-Range,Content-Disposition,Content-Description,Set-Cookie,Cookie,Csrf-Token,X-Requested-Time");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "1800");//单位秒

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}
