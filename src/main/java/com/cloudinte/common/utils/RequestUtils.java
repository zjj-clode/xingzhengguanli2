package com.cloudinte.common.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtils {
	private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	/**
	 * 查看request中的所有参数
	 *
	 * @param request
	 */
	public static void showParams(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		Enumeration<?> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map.put(paramName, paramValue);
				}
			}
		}
		Set<Map.Entry<String, String>> set = map.entrySet();
		logger.debug("------------------------------");
		for (Map.Entry<String, String> entry : set) {
			logger.debug(entry.getKey() + ":" + entry.getValue());
		}
		logger.debug("------------------------------");
	}

	/**
	 * 返回request中的所有参数
	 *
	 * @param request
	 *
	 * @deprecated 只适合没有重名的参数名的情况
	 */
	@Deprecated
	public static Map<String, String> getParamsMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map.put(paramName, paramValue);
				}
			}
		}
		Set<Map.Entry<String, String>> set = map.entrySet();
		logger.debug("------------------------------");
		for (Map.Entry<String, String> entry : set) {
			logger.debug(entry.getKey() + ":" + entry.getValue());
		}
		logger.debug("------------------------------");
		return map;
	}

	/**
	 * 返回request中的所有参数
	 *
	 * @param request
	 */
	public static Map<String, String[]> getParamsMapNew(HttpServletRequest request) {
		Map<String, String[]> map = new HashMap<String, String[]>();
		Enumeration<?> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			map.put(paramName, paramValues);
		}
		Set<Map.Entry<String, String[]>> set = map.entrySet();
		logger.debug("------------------------------");
		for (Map.Entry<String, String[]> entry : set) {
			logger.debug(entry.getKey() + ":" + entry.getValue());
		}
		logger.debug("------------------------------");
		return map;
	}
}
