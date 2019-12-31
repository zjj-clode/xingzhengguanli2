/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.web;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.net.HttpHeaders;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * Http与Servlet工具类.
 *
 * @author calvin/thinkgem
 * @version 2014-8-19
 */
public class Servlets {
	
	// -- 常用数值定义 --//
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;
	
	// 静态文件后缀
	private final static String[] staticFiles = StringUtils.split(Global.getConfig("web.staticFile"), ",");
	
	// 动态映射URL后缀
	private final static String urlSuffix = Global.getUrlSuffix();
	
	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header, set a time after now.
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}
	
	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}
	
	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}
	
	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader(HttpHeaders.ETAG, etag);
	}
	
	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 *
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 *
	 * @param lastModified
	 *            内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified) {
		long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if (ifModifiedSince != -1 && lastModified < ifModifiedSince + 1000) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}
	
	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 *
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 *
	 * @param etag
	 *            内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
				
				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}
			
			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader(HttpHeaders.ETAG, etag);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 *
	 * @param fileName
	 *            下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
			e.getMessage();
		}
	}
	
	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 *
	 * 返回的结果的Parameter名已去除前缀.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(pre) || paramName.startsWith(pre)) {
				String unprefixed = paramName.substring(pre.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					values = new String[] {};
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	
	/**
	 * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
	 *
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		StringBuilder queryStringBuilder = new StringBuilder();
		
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append("&");
			}
		}
		return queryStringBuilder.toString();
	}
	
	/**
	 * 客户端对Http Basic验证的 Header进行编码.
	 */
	public static String encodeHttpBasic(String userName, String password) {
		String encode = userName + ":" + password;
		return "Basic " + Encodes.encodeBase64(encode.getBytes());
	}
	
	/**
	 * 是否是Ajax异步请求
	 *
	 * @param request
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		
		String accept = request.getHeader("accept");
		String xRequestedWith = request.getHeader("X-Requested-With");
		Principal principal = UserUtils.getPrincipal();
		
		// 如果是异步请求或是手机端，则直接返回信息
		return accept != null && accept.indexOf("application/json") != -1 || xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1 || principal != null && principal.isMobileLogin();
	}
	
	/**
	 * 获取当前请求对象
	 *
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 判断访问URI是否是静态文件请求
	 *
	 * @throws Exception
	 */
	public static boolean isStaticFile(String uri) {
		if (staticFiles == null) {
			try {
				throw new Exception("检测到“app.properties”中没有配置“web.staticFile”属性。配置示例：\n#静态文件后缀\n" + "web.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//		if ((StringUtils.startsWith(uri, "/static/") || StringUtils.endsWithAny(uri, sfs))
		//				&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
		//			return true;
		//		}
		if (StringUtils.endsWithAny(uri, staticFiles) && !StringUtils.endsWithAny(uri, urlSuffix) && !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")) {
			return true;
		}
		return false;
	}
	
	////////////////////
	
	public static String getRealPath(HttpServletRequest request, String path) {
		if (request == null) {
			request = getRequest();
		}
		if (request == null) {
			return "";
		}
		ServletContext context = request.getSession().getServletContext();
		String realpath = context.getRealPath(path);
		//tomcat8.0获取不到真实路径，通过/获取路径
		if (realpath == null) {
			realpath = context.getRealPath("/") + path;
		}
		return realpath;
	}
	
	public static String getRealPath(String path) {
		return getRealPath(null, path);
	}
	
	/**
	 * http://localhost:8080/akmi <br/>
	 * http://localhost/akmi <br/>
	 * https://localhost/akmi <br/>
	 * 不以/结尾、http80和https443时省略端口号
	 */
	public static String getBasePath(HttpServletRequest request) {
		if (request == null) {
			request = getRequest();
		}
		if (request == null) {
			return "";
		}
		String basePathWithoutContextPath = getBasePathWithoutContextPath(request);
		if (StringUtils.isBlank(basePathWithoutContextPath)) {
			return basePathWithoutContextPath;
		}
		return basePathWithoutContextPath + request.getContextPath();
	}
	
	public static String getBasePathWithoutContextPath(HttpServletRequest request) {
		if (request == null) {
			request = getRequest();
		}
		if (request == null) {
			return "";
		}
		String scheme = request.getScheme();
		int serverPort = request.getServerPort();
		if ("http".equals(scheme) && 80 == serverPort || "https".equals(scheme) && 443 == serverPort) {
			return new StringBuilder().append(scheme).append("://").append(request.getServerName()).toString();
		} else {
			return new StringBuilder().append(scheme).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).toString();
		}
	}
	
	public static String getBasePathWithoutContextPath() {
		return getBasePathWithoutContextPath(null);
	}
	
	public static String getBasePath() {
		return getBasePath(null);
	}
	
	public static Object getAndClearParamValueFromSession(String name) {
		return getAndClearParamValueFromSession(null, name);
	}
	
	public static Object getAndClearParamValueFromSession(HttpServletRequest request, String name) {
		if (request == null) {
			request = getRequest();
		}
		if (request != null) {
			HttpSession session = request.getSession();
			Object value = session.getAttribute(name);
			session.removeAttribute(name);
			return value;
		}
		return null;
	}
	
	public static void setParamValueToSession(HttpServletRequest request, String name, Object value) {
		if (request == null) {
			request = getRequest();
		}
		HttpSession session = request.getSession();
		session.setAttribute(name, value);
	}
	
	/**
	 * 获取并删除HttpSession中的url属性值，用于指定登录成功后的跳转链接。request.getParameter("url")方式的改进。
	 */
	public static String getAndClearRedirectUrlFromSession(HttpServletRequest request) {
		return (String) getAndClearParamValueFromSession(request, "login_success_redirect_to_url");
	}
	
	/**
	 * 设置HttpSession中的url属性值，用于指定登录成功后的跳转链接。request.getParameter("url")方式的改进。
	 */
	public static void setRedirectUrlToSession(HttpServletRequest request, String redirectUrl) {
		setParamValueToSession(request, "login_success_redirect_to_url", redirectUrl);
	}
	
	/**
	 * 获取并删除HttpSession中的登录失败信息
	 */
	public static String getAndClearLoginErrorMessageFromSession(HttpServletRequest request) {
		return (String) getAndClearParamValueFromSession(request, "login_error_message");
	}
	
	/**
	 * 设置HttpSession中的登录失败信息
	 */
	public static void setLoginErrorMessageToSession(HttpServletRequest request, String loginErrorMessage) {
		setParamValueToSession(request, "login_error_message", loginErrorMessage);
	}
	
}
