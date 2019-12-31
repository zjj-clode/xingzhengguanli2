package com.thinkgem.jeesite.modules.sys.security;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.web.Servlets;

public class CookieFilter implements Filter {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	String sessionCookieName = Global.getConfig("custmor.code") + ".zhaosheng.session.id";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getServletPath();
		if (Servlets.isStaticFile(uri)) {
			chain.doFilter(req, resp);
			return;
		}

		//resp.setHeader("X-Frame-Options", "SAMEORIGIN");//同域
		//resp.setHeader("X-XSS-Protection", "1;mode=block");//XSS
		//resp.setHeader("X-Content-Type-Options", "nosniff");//禁用浏览器内容嗅探行为

		Cookie[] cookies = req.getCookies();

		if (cookies != null && cookies.length > 0) {
			Cookie cookie = cookies[0];
			if (cookie != null) {
				/*cookie.setMaxAge(3600);
				cookie.setSecure(true);
				resp.addCookie(cookie);*/

				logger.debug("uri-------->{}", uri);

				String name = cookie.getName();
				String value = cookie.getValue();
				logger.debug("cookie.getName()----> {}，cookie.getValue()----> {}", name, value);
				if (name != null && !name.equals(sessionCookieName)) {
					StringBuilder builder = new StringBuilder();
					builder.append(name + "=" + value + "; ");

					if ("https".equalsIgnoreCase(request.getScheme())) {
						builder.append("Secure; ");
					}

					builder.append("HttpOnly; ");
					Calendar cal = Calendar.getInstance();
					//cal.add(Calendar.HOUR, 1);
					cal.add(Calendar.SECOND, Integer.valueOf(Global.getConfig("session.sessionTimeout")) / 1000);
					Date date = cal.getTime();
					Locale locale = Locale.CHINA;
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
					builder.append("Expires=" + sdf.format(date));
					//
					resp.setHeader("Set-Cookie", builder.toString());
				}
			}
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

		SimpleCookie cookie = SpringContextHolder.getBean("sessionIdCookie");
		if (cookie != null) {
			sessionCookieName = cookie.getName();
			logger.debug("sessionCookieName ---> {}", sessionCookieName);
		}

	}
}
