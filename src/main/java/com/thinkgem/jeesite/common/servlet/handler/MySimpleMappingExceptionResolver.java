package com.thinkgem.jeesite.common.servlet.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 根据配置参数动态切换显示给用户的错误页面。
 */
public class MySimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private String productModeDir = "error/";
	private String developModeDir = "error-develop/";

	public String getProductModeDir() {
		return productModeDir;
	}

	/**
	 * 产品模式时，错误视图所在目录。默认为：error/
	 */
	public void setProductModeDir(String productModeDir) {
		this.productModeDir = productModeDir;
	}

	public String getDevelopModeDir() {
		return developModeDir;
	}

	/**
	 * 开发模式时，错误视图所在目录。默认为：error-develop/
	 */
	public void setDevelopModeDir(String developModeDir) {
		this.developModeDir = developModeDir;
	}

	/**
	 * 视图处理：开发模式时，返回视图error-develop/xxx；产品模式时，返回视图error/xxx。
	 */
	@Override
	protected String determineViewName(Exception ex, HttpServletRequest request) {
		String viewName = super.determineViewName(ex, request);
		logger.debug("异常exception--->" + ex.getClass().getName() + "，处理前视图viewName--->" + viewName);
		if (SettingsUtils.isDevMode()) {
			viewName = viewName.replace(productModeDir, developModeDir);
		} else {
			viewName = viewName.replace(developModeDir, productModeDir);
		}
		logger.debug("异常exception--->" + ex.getClass().getName() + "，处理后视图viewName--->" + viewName);
		return viewName;
	}

}