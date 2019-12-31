/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.modules.log.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.modules.log.entity.BusinessLog;
import com.cloudinte.modules.log.service.BusinessLogService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/common/businessLog")
public class BusinessLogController extends BaseController {

	@Autowired
	private BusinessLogService businessLogService;

	@ModelAttribute("businessLog")
	public BusinessLog get(@RequestParam(required = false) String id) {
		BusinessLog entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = businessLogService.get(id);
		}
		if (entity == null) {
			entity = new BusinessLog();
		}
		return entity;
	}

	@RequiresPermissions("common:businessLog:view")
	@RequestMapping(value = { "list", "" })
	public String list(BusinessLog businessLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BusinessLog> page = businessLogService.findPage(new Page<BusinessLog>(request, response), businessLog);
		model.addAttribute("page", page);
		model.addAttribute("ename", "businessLog");
		setBase64EncodedQueryStringToEntity(request, businessLog);
		return "modules/businesslog/businessLogList";
	}

	@RequiresPermissions("common:businessLog:delete")
	@RequestMapping(value = "delete")
	public String delete(BusinessLog businessLog, RedirectAttributes redirectAttributes) {
		businessLogService.delete(businessLog);
		addMessage(redirectAttributes, "删除业务操作日志成功");
		return "redirect:" + adminPath + "/common/businessLog/?repage&"
				+ StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(businessLog));
	}

	@RequiresPermissions("common:businessLog:delete")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(BusinessLog businessLog, RedirectAttributes redirectAttributes) {
		String[] ids = businessLog.getIds();
		if (ObjectUtils.isEmpty(ids)) {
			addMessage(redirectAttributes, "批量删除业务操作日志失败，请勾选数据后再执行删除操作！");
		} else {
			businessLogService.deleteBatch(businessLog);
			addMessage(redirectAttributes, "批量删除业务操作日志成功");
		}
		return "redirect:" + adminPath + "/common/businessLog/?repage&"
				+ StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(businessLog));
	}
}
