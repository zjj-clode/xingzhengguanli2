package com.thinkgem.jeesite.modules.sys.security.csrf;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * use this contract for example as part of a solution to provide data integrity, confidentiality, protection against cross-site request forgery (CSRF), and others or for other
 * tasks such as automatically adding a hidden field to all forms and URLs.
 */
@Component(value = "requestDataValueProcessor")
public class CSRFRequestDataValueProcessor implements RequestDataValueProcessor {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CSRFTokenService csrfTokenService;

	/**
	 * 额外给表单增加隐藏域
	 */
	@Override
	public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
		//此处是当使用spring的taglib标签<form:form>创建表单时候，增加的隐藏域参数
		Map<String, String> hiddenFields = new HashMap<>();

		// 新版
		String csrfTokenParameterName = csrfTokenService.getCsrfTokenParameterName();
		String token = (String) request.getAttribute(csrfTokenParameterName);
		logger.debug(" request.getAttribute(\"" + csrfTokenParameterName + "\") -------> {}", token);
		if (StringUtils.isBlank(token)) {
			token = csrfTokenService.generateToken(request.getSession().getId());
		}
		hiddenFields.put(csrfTokenParameterName, token);
		//

		// 兼容旧版
		hiddenFields.put(CSRFTokenManager.CSRF_PARAM_NAME, token);

		return hiddenFields;
	}

	/**
	 * 处理action，不管
	 */
	@Override
	public String processAction(HttpServletRequest request, String action, String httpMethod) {
		return action;
	}

	/**
	 * 处理表单字段，不管
	 */
	@Override
	public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
		return value;
	}

	/**
	 * 处理url，不管
	 */
	@Override
	public String processUrl(HttpServletRequest request, String url) {
		return url;
	}

}
