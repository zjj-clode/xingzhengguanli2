/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.web;

import java.beans.PropertyEditorSupport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.cloudinte.modules.log.entity.BusinessLog;
import com.cloudinte.zhaosheng.modules.common.entity.APIResponseObject;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mapper.ResponseObject;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.QueryStringUtils;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.BusinessLogUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 控制器支持类
 *
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;

	@Value("${photoPath}")
	protected String photoPath;

	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;

	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 *
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 *
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 *
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}

	/**
	 * 添加Model消息
	 *
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息
	 *
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		//判断操作类型add，check，del
		if (messages.length == 1) {
			if (messages[0].contains("保存")) {
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_ADD, messages[0]);
			} else if (messages[0].contains("删除")) {
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_DEL, messages[0]);
			} else if (messages[0].contains("修改")) {
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_UPDATE, messages[0]);
			} else if (messages[0].contains("审核")) {
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_CHECK, messages[0]);
			} else {
				//其他业务类型再处理
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_OTHER, messages[0]);
			}
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

	/**
	 * 客户端返回JSON字符串
	 *
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}

	/**
	 * 客户端返回字符串
	 *
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	protected APIResponseObject successMsg(String msg) {
		return new ResponseObject(ResponseObject.STATE_SUCCESS, msg);
	}

	protected APIResponseObject failureMsg(String msg) {
		return new ResponseObject(ResponseObject.STATE_FAILURE, msg);
	}

	/**
	 * 获取URL-safe Base64 编码的查询字符串，设置到entity中
	 */
	@SuppressWarnings("rawtypes")
	protected void setBase64EncodedQueryStringToEntity(HttpServletRequest request, BaseEntity entity) {
		entity.setQueryString(QueryStringUtils.getBase64EncodedQueryString(request));
	}

	/**
	 * 从entity中获取URL-safe Base64 还原查询字符串
	 */
	@SuppressWarnings("rawtypes")
	protected String getBase64DecodedQueryStringFromEntity(BaseEntity entity) {
		return entity == null ? "" : StringUtils.trimToEmpty(QueryStringUtils.getBase64DecodedQueryString(entity.getQueryString()));
	}

	/**
	 * 参数验证异常（web版直接抛出到SimpleMappingExceptionResolver统一处理）
	 */
	@ExceptionHandler({ ConstraintViolationException.class, ValidationException.class })
	public String validationException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
		String url = request.getRequestURI().substring(request.getContextPath().length());
		logger.debug("参数验证异常。请求url={}", url);
		e.printStackTrace();
		boolean isAjaxRequest = Servlets.isAjaxRequest(request);
		if (isAjaxRequest) {
			renderString(response, new ResponseObject(ResponseObject.STATE_FAILURE, e.getMessage()));
			return null;
		} else {
			throw e;
		}
	}

	/**
	 * 参数绑定异常（抛出的话，SimpleMappingExceptionResolver也处理不了 ？ ）
	 */
	@ExceptionHandler({ BindException.class })
	public String bindException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
		String url = request.getRequestURI().substring(request.getContextPath().length());
		logger.debug("参数绑定异常。请求url={}", url);
		e.printStackTrace();
		boolean isAjaxRequest = Servlets.isAjaxRequest(request);
		if (isAjaxRequest) {
			renderString(response, new ResponseObject(ResponseObject.STATE_FAILURE, e.getMessage()));
			return null;
		} else {
			//throw e;
			return SettingsUtils.isDevMode() ? "error-develop/404" : "error/404";
		}
	}

	/**
	 * 认证异常（web版直接抛出到SimpleMappingExceptionResolver统一处理）
	 */
	@ExceptionHandler({ AuthenticationException.class })
	public String authenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws AuthenticationException {
		String url = request.getRequestURI().substring(request.getContextPath().length());
		logger.debug("认证异常。请求url={}", url);
		boolean isAjaxRequest = Servlets.isAjaxRequest(request);
		if (isAjaxRequest) {
			renderString(response, new ResponseObject(ResponseObject.STATE_FAILURE, e.getMessage()));
			return null;
		} else {
			throw e;
		}
	}

	/**
	 * 授权异常 <br>
	 * 1、方法上有注解@RequiresRoles，用户未登录 或 已登录但<b>没有对应的角色</b> <br>
	 * 2、方法上有注解@RequiresPermissions，用户未登录 或 已登录但<b>没有对应的权限</b> <br>
	 * 3、方法不以/a开头，但方法上有@RequiresUser，<b>用户未登录</b>（包括通过“记住我”登录）<br>
	 * 4、方法不以/a开头，但方法上有@RequiresAuthentication，<b>用户未认证</b>（通过账号密码登录，不包括通过“记住我”登录）<br>
	 * 注：未登录，请求以/a开头的地址时，会被{@link com.thinkgem.jeesite.modules.sys.security.UserFilter}拦截处理。
	 */
	@ExceptionHandler({ AuthorizationException.class })
	public String authorizationException(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) throws AuthorizationException {
		String method = request.getMethod();
		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString();
		logger.debug("method ---> {}, requestURI ---> {}, queryString ---> {}", method, requestURI, queryString);
		if (!StringUtils.isBlank(request.getContextPath())) {
			requestURI = requestURI.substring(request.getContextPath().length()); //去掉contextPath部分
		}
		StringBuilder requestUrl = new StringBuilder(requestURI);
		if (queryString != null) {
			requestUrl.append("?").append(queryString);
		}
		String url = requestUrl.toString();
		logger.debug("授权异常 。请求url={}", url);

		boolean isGetRequest = "GET".equalsIgnoreCase(method);
		boolean isAjaxRequest = Servlets.isAjaxRequest(request);

		//
		if (isAjaxRequest) {
			if (UserUtils.getPrincipal() != null) { // 已登录，无权限。提示无权限
				renderString(response, new ResponseObject(ResponseObject.STATE_REQUIRES_PERMISSION, ResponseObject.MSG_REQUIRES_PERMISSION));
			} else { // 未登录。提示登录
				renderString(response, new ResponseObject(ResponseObject.STATE_REQUIRES_USER, ResponseObject.MSG_REQUIRES_USER));
			}
			return null;
		} else {
			if (UserUtils.getPrincipal() != null) { // 已登录，无权限。提示无权限
				throw e; // 页面提示无权限
			} else { // 未登录。跳转到登录页
				if (isGetRequest) { // GET方法时，保存要跳转页面URL
					Servlets.setRedirectUrlToSession(request, url);
				}
				return "redirect:" + adminPath + "/login";
			}
		}
	}

	/**
	 * 其他异常处理（web版直接抛出到页面处理）
	 */
	@ExceptionHandler({ Exception.class })
	public String exception(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
		String url = request.getRequestURI().substring(request.getContextPath().length());
		logger.debug("请求url={}，异常信息={}", url, e.getMessage());
		e.printStackTrace();
		boolean isAjaxRequest = Servlets.isAjaxRequest(request);
		if (isAjaxRequest) {
			renderString(response, new ResponseObject(ResponseObject.STATE_ERR, e.getMessage()));
			return null;
		} else {
			throw e;
		}
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
			//			@Override
			//			public String getAsText() {
			//				Object value = getValue();
			//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
			//			}
		});
	}

	/**
	 * 数据导入的时候，将提示信息形成导入报告，给客户端浏览器下载。<br/>
	 * 问题：因为没有刷新页面，页面上的loading就没法去掉了。
	 */
	protected String downloadImportMsg(RedirectAttributes redirectAttributes, HttpServletResponse response) {
		// redirectAttributes中的提示信息
		String message = (String) redirectAttributes.getFlashAttributes().get("message");
		if (StringUtils.isBlank(message)) {
			return null;
		}
		// 换行符
		@SuppressWarnings("restriction")
		String lineSeparator = System.getProperty("line.separator", java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator")));
		//String replaceTo = lineSeparator + "\t";
		String replaceTo = lineSeparator + "    "; //4个空格
		message = message.replaceAll("<br>", replaceTo).replaceAll("<br/>", replaceTo).replaceAll("<br />", replaceTo);
		message = HtmlUtils.htmlUnescape(message);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new ByteArrayInputStream(message.getBytes()); // 内存中导出下载
			out = response.getOutputStream();
			String attachmentName = "导入报告." + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".txt"; // 下载文件名称
			String filename = new String(attachmentName.getBytes(), "ISO-8859-1");
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			byte[] buffer = new byte[2048];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/** PC版前端视图文件总路径，front/${custmor.code}/${custmor.theme}/，例如：front/cau/basic/ */
	protected String basicDir = "front/" + SettingsUtils.getSysConfig("custmor.code", "yunzhi") + "/" + SettingsUtils.getSysConfig("custmor.theme", "basic") + "/";

	/** 移动版前端视图文件总路径，mobile/front/${custmor.code}/${custmor.theme}/，例如：mobile/front/cau/basic/ */
	protected String mobileBasicDir = "mobile/" + basicDir;

	/**
	 * 提取集合中的对象的属性名称和属性值(通过Getter函数), 组合成Map的List集合。用于输出指定字段的json
	 * 
	 * @param collection    对象集合
	 * @param propertyNames 属性名称
	 * @return
	 */
	protected List<Map<String, Object>> extractToMapListWithPropertyNames(final Collection<? extends BaseEntity<?>> collection, final Set<String> propertyNames) {
		List<Map<String, Object>> list = new ArrayList<>(collection.size());
		try {
			for (BaseEntity<?> obj : collection) {
				list.add(obj.toMapWithPropertyNames(propertyNames));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}
		return list;
	}
}
