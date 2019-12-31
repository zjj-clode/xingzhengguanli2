package com.thinkgem.jeesite.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询字符串获取、编码、解码。用到org.apache.commons.codec包
 *
 * @author 廖水平
 * @version 1.0 2016年5月6日 下午11:27:54
 */
public class QueryStringUtils {
	private static Logger logger = LoggerFactory.getLogger(QueryStringUtils.class);
	
	/**
	 * 获取查询字符串(忽略名称为“repage”的参数)
	 *
	 * @return 像这样的字符串：<br>
	 *         pageNo=1&pageSize=20&orderBy=&room.building.campus.id= d788dfffb66e414da01b8fb81814136d
	 *         &room.building.id=&room.id=&schoolyear=2017&student.dep.id=&student.stutype=&student.
	 *         grade=&student.lengthOfSchooling=&student.sex=&student.stuname=&student.stuid=
	 */
	@SuppressWarnings("unchecked")
	public static String getQueryString(HttpServletRequest request) {
		String queryString = "";
		StringBuilder sb = new StringBuilder();
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap.size() > 0) {
			for (Entry<String, String[]> entry : parameterMap.entrySet()) {
				String paramName = entry.getKey();
				if ("repage".equals(paramName)) {
					continue;
				}
				if ("CSRFToken".equals(paramName)) {
					continue;
				}
				String[] paramValues = entry.getValue();
				if (paramValues.length > 0) {
					for (String paramValue : paramValues) {
						//sb.append("&").append(paramName).append("=").append(paramValue);
						//url编码一下中文
						try {
							sb.append("&").append(URLEncoder.encode(paramName, "UTF-8")).append("=").append(URLEncoder.encode(paramValue, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							sb.append("&").append(paramName).append("=").append(paramValue);
						}
					}
				}
			}
		}
		if (sb.length() > 1) {
			queryString = sb.substring(1).toString();
		}
		logger.debug("查询字符串：" + queryString);
		return queryString;
	}
	
	/**
	 * 获取URL-safe Base64 编码的查询字符串
	 *
	 * @return 像这样的字符串：<br>
	 *         cGFnZU5vPTEmcGFnZVNpemU9MjAmb3JkZXJCeT0mcm9vbS5idWlsZGluZy5jYW1wdXMuaWQ9NDZlY2M5Y2M4ZmI4NGEyMGIxMTcxNTI3MWQxYWE3Nzcmcm9vbS5idWlsZGluZy5pZD1mMjc4YzdhOGFhOGQ0MGVjYjE1ZTUxZTUxZGJjMDc0MSZyb29tLmlkPTE3MzJjMDc3YTJjNzQ4OWRiNjMwNGUxYTVmMmRmZmE1JnNjaG9vbHllYXI9MjAxNSZzdHVkZW50LmRlcC5pZD0yJnN0dWRlbnQuc3R1dHlwZT0yJnN0dWRlbnQuZ3JhZGU9MjAxNiZzdHVkZW50Lmxlbmd0aE9mU2Nob29saW5nPTMmc3R1ZGVudC5zZXg9MSZzdHVkZW50LnN0dW5hbWU9ZyZzdHVkZW50LnN0dWlkPTIxNTA4MDIwMDM
	 */
	public static String getBase64EncodedQueryString(HttpServletRequest request) {
		String q = getBase64EncodedQueryString(getQueryString(request));
		logger.debug("getBase64EncodedQueryString--->{}", q);
		return q;
	}
	
	/**
	 * URL-safe Base64 编码字符串
	 */
	public static String getBase64EncodedQueryString(String queryString) {
		if (com.thinkgem.jeesite.common.utils.StringUtils.isBlank(queryString)) {
			return queryString;
		}
		return Base64.encodeBase64URLSafeString(queryString.getBytes());
	}
	
	/**
	 * URL-safe Base64 还原查询字符串
	 *
	 * @param encodedUrl
	 *            URL-safe Base64 编码的查询字符串
	 * @return 还原查询字符串
	 */
	public static String getBase64DecodedQueryString(String queryString) {
		return StringUtils.newStringUtf8(Base64.decodeBase64(queryString));
	}
	
	public static void main(String[] args) {
		String encryptTxt = "";
		String queryString = "pageNo=1&pageSize=20&orderBy=&room.building.campus.id=46ecc9cc8fb84a20b11715271d1aa777&room.building.id=f278c7a8aa8d40ecb15e51e51dbc0741&room.id=1732c077a2c7489db6304e1a5f2dffa5&schoolyear=2015&student.dep.id=2&student.stutype=2&student.grade=2016&student.lengthOfSchooling=3&student.sex=1&student.stuname=g&student.stuid=2150802003";
		try {
			logger.debug("原始queryString为：" + queryString);
			encryptTxt = getBase64EncodedQueryString(queryString);
			logger.debug("编码后为：" + encryptTxt);
			queryString = getBase64DecodedQueryString(encryptTxt);
			logger.debug("还原后为：" + queryString);
			logger.debug(getBase64DecodedQueryString("aHR0cDovL3Nucy5jYXUuZWR1LmNuL2dyb3VwL3Nob3cuZG8/Z3JvdXBJZD0xNDQ2NDcmc3lzdGVtSWQ9JnNjb3BlPTQ2MSZmb3JjZT0x"));
			logger.debug(getBase64DecodedQueryString("aHR0cDovL2hxLmJpc3UuZWR1LmNuOjgwL2Evd3gvYmFvWGl1L3Nob3VMaUZvcm0_aWQ9MjAxNjExMTcyOQ"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
