package com.thinkgem.jeesite.modules.sys.security.csrf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 接口调用控制
 * 
 * @author lsp
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiControll {

	/**
	 * 支持的HTTP方法
	 */
	RequestMethod[] supportedMethods() default { RequestMethod.POST, RequestMethod.GET };

	/**
	 * 是否需要验证csrf token，默认为true
	 */
	boolean needVerifyToken() default true;

	/**
	 * 是否需要在响应头中返回csrf token，默认为true
	 */
	boolean needSetbackToken() default true;

	/**
	 * 期望的HTTP头。支持配置参数。HTTP头使用key=value形式，如果没有value，只需提供key，不检查value<br/>
	 * 
	 * <pre class="code">
	 * &#64;DoCsrf(expectedHeaders = { "Referer=${basePath}/${custmor.code}/html/zsjh.html", "X-Requested-Time", "X-Requested-With=XMLHttpRequest", "User-Agent", "Cookie", "Host" })
	 * </pre>
	 */
	String[] expectedHeaders() default {};

	/**
	 * 限制访问本接口的HTTP方法
	 */
	RequestMethod[] expectedMethods() default {};
}
