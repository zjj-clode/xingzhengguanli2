/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Settings;
import com.thinkgem.jeesite.modules.sys.service.SettingsService;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 运行参数Controller。
 * <p>
 * 超级管理员权限：查看、添加、修改、删除所有的配置数据 sys:settings:view,sys:settings:edit
 * </p>
 * <p>
 * 普通管理员权限：查看、修改非系统级配置数据 sys:settings:view,sys:settings:set
 * </p>
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/settings")
public class SettingsController extends BaseController {

	@Autowired
	private SettingsService settingsService;

	@ModelAttribute
	public Settings get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return settingsService.get(id);
		} else {
			return new Settings();
		}
	}

	@RequiresPermissions("sys:settings:view")
	@RequestMapping(value = { "list", "" })
	public String list(Settings settings, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 
		boolean isSystem = UserUtils.getSubject().isPermitted("sys:settings:edit");
		if (!isSystem) {
			settings.setSystemDefined(Global.NO);
		}
		Page<Settings> page = settingsService.findPage(new Page<Settings>(request, response), settings);
		model.addAttribute("page", page);
		model.addAttribute("ename", "settings");
		setBase64EncodedQueryStringToEntity(request, settings);
		return "modules/sys/settings/settingsList";
	}

	@RequiresPermissions("sys:settings:view")
	@RequestMapping(value = "form")
	public String form(Settings settings, Model model) {
		model.addAttribute("settings", settings);
		model.addAttribute("ename", "settings");
		return "modules/sys/settings/settingsForm";
	}

	@RequestMapping(value = "save")
	public String save(Settings settings, Model model, RedirectAttributes redirectAttributes) {

		// save方法要求权限： sys:settings:edit 或者 sys:settings:set 
		boolean isSystem = UserUtils.getSubject().isPermitted("sys:settings:edit");
		if (!isSystem) {
			UserUtils.getSubject().checkPermission("sys:settings:set");

			// 没有sys:settings:edit权限不能操作systemDefined=1系统级的数据
			if (Global.YES.equals(settings.getSystemDefined())) {
				addMessage(redirectAttributes, "保存配置参数失败，非法权限");
				return "redirect:" + adminPath + "/sys/settings/?repage&queryString=" + getBase64DecodedQueryStringFromEntity(settings);
			}
		}

		if (!beanValidator(model, settings)) {
			return form(settings, model);
		}

		String message = check(settings);
		if (!StringUtils.isBlank(message)) {
			addMessage(model, "保存配置参数失败，" + message);
			return form(settings, model);
		}

		Settings exitSettings = settingsService.getByKey(settings);
		if (exitSettings != null && !exitSettings.getId().equals(settings.getId())) {
			addMessage(model, "保存配置参数失败，已有键为【" + settings.getKey() + "】的配置项了。");
			return form(settings, model);
		}

		settingsService.save(settings);
		addMessage(redirectAttributes, "保存配置参数“ " + settings.getKey() + " ”成功");
		return "redirect:" + adminPath + "/sys/settings/?repage&queryString=" + getBase64DecodedQueryStringFromEntity(settings);
	}

	@RequiresPermissions("sys:settings:edit")
	@RequestMapping(value = "delete")
	public String delete(Settings settings, RedirectAttributes redirectAttributes) {
		settingsService.delete(settings);
		addMessage(redirectAttributes, "删除配置参数成功");
		return "redirect:" + adminPath + "/sys/settings/?repage&queryString=" + getBase64DecodedQueryStringFromEntity(settings);
	}

	private String check(Settings settings) {
		if (null == SettingsUtils.convertSettingsValueByDataType(settings)) {
			return "参数数据类型和参数值不相符合。";
		}
		return null;
	}

}
