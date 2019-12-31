package com.thinkgem.jeesite.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

import com.thinkgem.jeesite.common.config.Global;

/**
 * HttpServletRequest帮助类
 */
public class RequestUtils {
	private static final Logger log = LoggerFactory.getLogger(RequestUtils.class);

	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的， 那么将通过HttpServletRequest#getParameter获取。
	 *
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static String getQueryParam(HttpServletRequest request, String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		if (request.getMethod().equalsIgnoreCase("POST")) {
			return request.getParameter(name);
		}
		String s = request.getQueryString();
		if (StringUtils.isBlank(s)) {
			return null;
		}
		try {
			s = URLDecoder.decode(s, "UTF8");
		} catch (UnsupportedEncodingException e) {
			log.error("encoding UTF8 not support?", e);
		}
		String[] values = parseQueryString(s).get(name);
		if (values != null && values.length > 0) {
			return values[values.length - 1];
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getQueryParams(HttpServletRequest request) {
		Map<String, String[]> map;
		if (request.getMethod().equalsIgnoreCase("POST")) {
			map = request.getParameterMap();
		} else {
			String s = request.getQueryString();
			if (StringUtils.isBlank(s)) {
				return new HashMap<String, Object>();
			}
			try {
				s = URLDecoder.decode(s, "UTF8");
			} catch (UnsupportedEncodingException e) {
				log.error("encoding UTF8 not support?", e);
			}
			map = parseQueryString(s);
		}

		Map<String, Object> params = new HashMap<String, Object>(map.size());
		int len;
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			len = entry.getValue().length;
			if (len == 1) {
				params.put(entry.getKey(), entry.getValue()[0]);
			} else if (len > 1) {
				params.put(entry.getKey(), entry.getValue());
			}
		}
		return params;
	}

	public static Map<String, String[]> parseQueryString(String queryString) {
		if (queryString == null) {
			throw new IllegalArgumentException();
		}
		String valArray[] = null;
		Map<String, String[]> map = new HashMap<String, String[]>();
		String[] pairs = StringUtils.split(queryString, "&");
		for (String pair : pairs) {
			int pos = pair.indexOf('=');
			if (pos == -1) {
				continue;
			}
			String key = pair.substring(0, pos);
			String val = pair.substring(pos + 1, pair.length());
			if (map.containsKey(key)) {
				String oldVals[] = map.get(key);
				valArray = new String[oldVals.length + 1];
				for (int i = 0; i < oldVals.length; i++) {
					valArray[i] = oldVals[i];
				}
				valArray[oldVals.length] = val;
			} else {
				valArray = new String[1];
				valArray[0] = val;
			}
			map.put(key, valArray);
		}
		return map;
	}

	public static void main(String[] args) {
		String s = "o=&a=1&c=4&b=2,3&c=3&u=中文";
		Map<String, String[]> map = parseQueryString(s);
		for (Entry<String, String[]> entry : map.entrySet()) {
			System.out.println("entry.getKey()--->" + entry.getKey());
			for (String string : entry.getValue()) {
				System.out.println("entry.getValue()--->" + string);
			}
		}
	}

	/**
	 * 获取含有特定前缀的请求参数名称和值
	 *
	 * @param prefix
	 *            前缀 attr_
	 * @return key不包括参数名称的前缀 map.put("id", "1")、map.put("name", "张三")、map.put("age", "33")<br/>
	 *         参数值多个以英文逗号隔开：map.put("id", "1,2,3")
	 */
	public static Map<String, String> getRequestMap(HttpServletRequest request, String prefix) {
		return getRequestMap(request, prefix, false);
	}

	/**
	 * 获取含有特定前缀的请求参数名称和值
	 *
	 * @param prefix
	 *            前缀 attr_
	 * @return key包括参数名称的前缀 map.put("attr_id", "1")、map.put("attr_name", "张三")、map.put("attr_age", "33")<br/>
	 *         参数值多个以英文逗号隔开：map.put("attr_id", "1,2,3")
	 */
	public static Map<String, String> getRequestMapWithPrefix(HttpServletRequest request, String prefix) {
		return getRequestMap(request, prefix, true);
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getRequestMap(HttpServletRequest request, String prefix, boolean nameWithPrefix) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		String name, key, value;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name.startsWith(prefix)) {
				key = nameWithPrefix ? name : name.substring(prefix.length());
				value = StringUtils.join(request.getParameterValues(name), ',');
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 获取访问者IP
	 *
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 *
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)， 如果还不存在则调用Request .getRemoteAddr()。
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			if (ip.contains("../") || ip.contains("..\\")) {
				return "";
			}
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				ip = ip.substring(0, index);
			}
			if (ip.contains("../") || ip.contains("..\\")) {
				return "";
			}
			return ip;
		} else {
			ip = request.getRemoteAddr();
			if (ip.contains("../") || ip.contains("..\\")) {
				return "";
			}
			if (ip.equals("0:0:0:0:0:0:0:1")) {
				ip = "127.0.0.1";
			}
			return ip;
		}

	}

	/**
	 * 获得当的访问路径
	 *
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 *
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		StringBuffer buff = request.getRequestURL();
		String uri = request.getRequestURI();
		String origUri = helper.getOriginatingRequestUri(request);
		buff.replace(buff.length() - uri.length(), buff.length(), origUri);
		String queryString = helper.getOriginatingQueryString(request);
		if (queryString != null) {
			buff.append("?").append(queryString);
		}
		return buff.toString();
	}

	/**
	 * 获得请求的session id，但是HttpServletRequest#getRequestedSessionId()方法有一些问题。 当存在部署路径的时候，会获取到根路径下的jsessionid。
	 *
	 * @see HttpServletRequest#getRequestedSessionId()
	 *
	 * @param request
	 * @return
	 */
	public static String getRequestedSessionId(HttpServletRequest request) {
		String sid = request.getRequestedSessionId();
		String ctx = request.getContextPath();
		// 如果session id是从url中获取，或者部署路径为空，那么是在正确的。
		if (request.isRequestedSessionIdFromURL() || StringUtils.isBlank(ctx)) {
			return sid;
		} else {
			// 手动从cookie获取
			String cookie = CookieUtils.getCookie(request, Global.getConfig("custmor.code") + ".zhaosheng.session.id");
			if (cookie != null) {
				return cookie;
			} else {
				return request.getSession().getId();
			}
		}
	}

}
