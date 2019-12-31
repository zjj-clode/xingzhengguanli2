/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;

/**
 * 内容管理Controller
 *
 * @author ThinkGem
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsController extends BaseController {

	@Autowired
	private CategoryService categoryService;

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "")
	public String index(String module, @RequestParam(value = "category.id", required = false) String categoryId, Model model, HttpServletRequest request) {
		if (!StringUtils.isEmpty(module)) {
			model.addAttribute("ename", module);
		}
		List<Category> categories = categoryService.findByUser(true, module);

		model.addAttribute("categoryId", categoryId);
		model.addAttribute("categoryList", categories);

		// [{"id":"0f8d91b6515647e59dbedc9c5db059ed","pId":"1","name":"关于我们","liUrl":"/cxcy/a/cms/article/?category.id=0f8d91b6515647e59dbedc9c5db059ed"},
		List<Map<String, String>> list = Lists.newArrayList();
		for (Category category : categories) {
			Map<String, String> map = Maps.newLinkedHashMap();
			map.put("id", category.getId());
			map.put("pId", category.getParent() == null ? "0" : category.getParent().getId());
			map.put("name", category.getName());
			if (StringUtils.isBlank(category.getModule())) {
				map.put("liUrl", "");
			} else {
				map.put("liUrl", request.getContextPath() + adminPath + "/cms/" + category.getModule() + "/?category.id=" + category.getId());
			}
			list.add(map);
		}
		String zNodes = JsonMapper.toJsonString(list);
		logger.debug("zNodes ------>{}",zNodes);
		model.addAttribute("zNodes", zNodes);

		if (!StringUtils.isEmpty(categoryId)) {
			String url = "";
			if (!StringUtils.isEmpty(CmsUtils.getContextPath())) {
				url = CmsUtils.getContextPath() + "/";
			}
			url += adminPath + "/cms/" + module + "/?category.id=" + categoryId;
			model.addAttribute("frameUrl", url);
		}
		return "modules/cms/cmsIndex";
	}

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "tree")
	public String tree(Model model) {
		model.addAttribute("categoryList", categoryService.findByUser(true, null));
		return "modules/cms/cmsTree";
	}

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none")
	public String none() {
		return "modules/cms/cmsNone";
	}

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "form")
	public String form(String module, @RequestParam(value = "category.id", required = false) String categoryId, String id, Model model) {
		if (!StringUtils.isEmpty(module)) {
			model.addAttribute("ename", module);
		}
		List<Category> categories = categoryService.findByUser(true, module);

		model.addAttribute("categoryId", categoryId);
		model.addAttribute("categoryList", categories);
		if (!StringUtils.isEmpty(categoryId)) {
			String url = "";
			if (!StringUtils.isEmpty(CmsUtils.getContextPath())) {
				url = CmsUtils.getContextPath() + "/";
			}
			url += adminPath + "/cms/" + module + "/form?id=" + id;
			model.addAttribute("frameUrl", url);
		}
		return "modules/cms/cmsIndex";
	}
}
