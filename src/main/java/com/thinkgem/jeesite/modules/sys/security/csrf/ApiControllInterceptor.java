package com.thinkgem.jeesite.modules.sys.security.csrf;

import java.sql.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mapper.ResponseObject;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 
 * @author lsp
 *
 */
public class ApiControllInterceptor implements HandlerInterceptor {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String errorPage;

	@Autowired
	private CSRFTokenService csrfTokenService;

	/**
	 * 失败跳转页
	 */
	public void setErrorPage(String errorPage) {
		if (errorPage != null && !errorPage.startsWith("/")) {
			throw new IllegalArgumentException("errorPage must begin with '/'");
		}
		this.errorPage = errorPage;
	}

	public void setCsrfTokenService(CSRFTokenService csrfTokenService) {
		this.csrfTokenService = csrfTokenService;
	}

	//验证和删除token
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			ApiControll apiControll = handlerMethod.getMethodAnnotation(ApiControll.class);
			if (apiControll == null) {
				apiControll = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), ApiControll.class);
			}
			// 如果加了注解
			if (apiControll != null) {

				if (apiControll.needSetbackToken() && !"OPTIONS".equalsIgnoreCase(request.getMethod())) {
					String newToken = csrfTokenService.generateToken(getUserId(request));
					logger.debug("newToken ------------> {}", newToken);
					request.setAttribute(csrfTokenService.getCsrfTokenParameterName(), newToken);// 在controller中获取request.getAttribute("Csrf-Token")，在页面中获取 ${requestScope['Csrf-Token']}
					response.addHeader(csrfTokenService.getCsrfTokenHeaderName(), newToken);// 在controller中获取response.getHeader("Csrf-Token")，在页面中获取 ${pageContext.response.getHeader('Csrf-Token')}
				}

				boolean isSupportedMethod = isSupportedMethod(request, apiControll);
				//logger.debug("isSupportedMethod ---> {} , request.getMethod() ---> {}", isSupportedMethod, request.getMethod());
				if (!isSupportedMethod) {
					return true;
				}

				// 检查结果
				boolean flag = true;

				// 检查请求方法
				boolean checkExpectedMethods = checkExpectedMethods(request, apiControll.expectedMethods());
				logger.debug("checkExpectedMethods ---> {}", checkExpectedMethods);
				flag = checkExpectedMethods;

				/* */
				// TODO 测试时去掉检查
				// 检查HTTP头部
				if (flag) {
					boolean checkExpectedHeaders = checkExpectedHeaders(request, apiControll.expectedHeaders());
					logger.debug("checkExpectedHeaders ---> {}", checkExpectedHeaders);
					//flag = checkExpectedHeaders;
				}

				// 检查csrf token
				if (flag) {
					if (apiControll.needVerifyToken()) {
						String requstCSRFToken = csrfTokenService.getRequstCSRFToken(request);
						logger.debug("提交 requstCSRFToken -----------> {}", requstCSRFToken);
						String userId = getUserId(request);
						//logger.debug("userId -----------> {}", userId);
						flag = csrfTokenService.verifyToken(userId, requstCSRFToken);
						logger.debug("verifyToken success ? -----------> {}", flag);
					}
				}

				if (!flag) {

					// response.sendError(403, "CSRF token error !");

					if (!response.isCommitted()) {

						if (Servlets.isAjaxRequest(request)) {

							ResponseObject responseObject = ResponseObject.failure().msg("禁止访问");
							String jsonString = JsonMapper.toJsonString(responseObject);
							//logger.debug("jsonString------------>{}", jsonString);
							response.setContentType("application/json;charset=UTF-8");
							response.getWriter().write(jsonString);

							response.setStatus(403);

						} else {

							if (errorPage != null) {
								response.setStatus(HttpStatus.FORBIDDEN.value());
								RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
								dispatcher.forward(request, response);
							} else {
								response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
							}

						}
					}

				}

				return flag;

			}
		}
		return true;
	}

	private boolean checkExpectedMethods(HttpServletRequest request, RequestMethod[] expectedMethods) {
		logger.debug("expectedMethods==>{}, request.getMethod()==>{}", StringUtils.join(expectedMethods, ","), request.getMethod());
		if (expectedMethods == null || expectedMethods.length < 1) {
			return true;
		}
		return containsMethod(expectedMethods, request.getMethod());
	}

	private boolean containsMethod(RequestMethod[] methods, String method) {
		if (methods != null && methods.length > 0) {
			for (RequestMethod requestMethod : methods) {
				if (requestMethod.name().equalsIgnoreCase(method)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查HTTP头
	 */
	private boolean checkExpectedHeaders(HttpServletRequest request, String[] expectedHeaders) {
		if (expectedHeaders == null || expectedHeaders.length < 1) {
			return true;
		}

		// 
		Map<String, String> map = new HashMap<>();
		map.put("basePath", Servlets.getBasePath());

		// {"a=b","c=","d"}
		for (String expectedHeader : expectedHeaders) {
			expectedHeader = StringUtils.trimToEmpty(expectedHeader);
			if (StringUtils.isBlank(expectedHeader)) {
				continue;
			}
			// a=b
			String[] items = expectedHeader.split("=");
			String headerName = items[0]; // a
			logger.debug("检查headerName ------->> {}", headerName);
			Enumeration<String> headerValueEnumeration = request.getHeaders(headerName);
			List<String> headerValueList = headerValueEnumerationToList(headerValueEnumeration);
			logger.debug("headerName对应值 : {} <------> {}", headerName, StringUtils.join(headerValueList, " , "));
			// headerName必须存在
			if (Collections3.isEmpty(headerValueList)) {

				// ajax跨域时，服务器端获取不到http头“X-Requested-With”
				// TODO 测试的时候不检查
				if (isApiTest()) {
					if ("X-Requested-With".equals(headerName)) {
						continue;
					}
				}

				logger.warn("headerValueEnumeration 不存在[headerName = {}],X-Requested-With--->{}", headerName, request.getHeader("X-Requested-With"));
				return false;
			}
			// 如果要求提供value，进行比较
			if (items.length > 1 && StringUtils.isNotBlank(items[1])) {
				String definedHeaderValue = items[1];
				logger.debug("definedHeaderValue ------->> {}", definedHeaderValue);
				definedHeaderValue = replaceProperties(definedHeaderValue, map);
				logger.debug("replaceProperties definedHeaderValue ------->> {}", definedHeaderValue);
				boolean flag = false;
				for (String hv : headerValueList) {
					logger.debug("hv ------->> {}", hv);
					if (StringUtils.isNotBlank(hv)) {
						flag = compare(headerName, hv, definedHeaderValue, request);
						if (flag) {
							break;
						}
					}
				}
				if (!flag) {
					return false;
				}
			}
		}
		return true;
	}

	private List<String> headerValueEnumerationToList(Enumeration<String> headerValueEnumeration) {
		List<String> list = Lists.newArrayList();
		if (headerValueEnumeration == null) {
			return list;
		}
		while (headerValueEnumeration.hasMoreElements()) {
			list.add(headerValueEnumeration.nextElement());
		}
		return list;
	}

	/**
	 * 
	 */
	private boolean checkRequestedTime(HttpServletRequest request, String headerValue) {

		logger.debug("requestedTime ----------> {}", headerValue);

		try {
			long time = Long.valueOf(headerValue);

			Date date = new Date(time);

			logger.debug("date ----------> {}", DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));

			if (System.currentTimeMillis() < time) {
				return false;
			}

		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	private boolean compare(String headerName, String headerValue, String definedHeaderValue, HttpServletRequest request) {

		logger.debug(" headerName ---> {}, headerValue ---> {}, definedHeaderValue ---> {}", headerName, headerValue, definedHeaderValue);

		boolean localhost = localhost(request);

		if ("Host".equals(headerName)) {

			//
			if (localhost) {
				return true;
			}

			if (!request.getRemoteHost().equals(headerValue)) {
				return false;
			}

		} else if ("Referer".equals(headerName)) {

			//
			if (localhost) {
				return true;
			}

			// TODO 测试时，不限
			if (isApiTest()) {
				return true;
			}

		} else if ("X-Requested-Time".equals(headerName)) {

			//
			logger.debug("X-Requested-Time -----------> {}", headerValue);

		} else if ("X-Requested-With".equals(headerName)) {

			//
			logger.debug("X-Requested-With -----------> {}", headerValue);

		}

		return headerValue.equals(definedHeaderValue);
	}

	private boolean localhost(HttpServletRequest request) {
		String remoteHost = request.getRemoteHost();
		if ("0:0:0:0:0:0:0:1".equals(remoteHost) || "127.0.0.1".equals(remoteHost) && "localhost".equals(remoteHost) || remoteHost != null && remoteHost.equals(request.getLocalAddr())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是api调试
	 */
	private boolean isApiTest() {
		return SettingsUtils.getSysConfig("sys.isApiTest", false);
	}

	/**
	 * 支持“${”的后面或者“}”的前面有空格，如：${ custmor.code} 、 ${custmor.code }和 ${ custmor.code }
	 */
	private String replaceProperties(String str, Map<String, String> map) {
		//logger.debug("str ---------> {}", str);
		String open = "${";
		String close = "}";
		if (!str.contains(open) || !str.contains(close)) {
			return str;
		}
		String[] strings = StringUtils.substringsBetween(str, open, close);
		if (strings != null && strings.length > 0) {
			for (String s : strings) {
				String r = open + s + close; // 整个为待替换部分 
				String k = s.trim(); // key ，待替换为值
				String v = null;
				if (map != null) {
					v = map.get(k);
				}
				if (v == null) {
					v = SettingsUtils.getSysConfig(k);
				}
				if (v != null) {
					str = str.replace(r, v);
				}
			}
		}
		return str;
	}

	/**
	 * 当前请求方法是否被支持
	 */
	private boolean isSupportedMethod(HttpServletRequest request, ApiControll verifyCSRFToken) {
		return containsMethod(verifyCSRFToken.supportedMethods(), request.getMethod());
	}

	/**
	 * 获取当前用户标识，session id
	 */
	private String getUserId(HttpServletRequest request) {
		return request.getSession(true).getId();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
