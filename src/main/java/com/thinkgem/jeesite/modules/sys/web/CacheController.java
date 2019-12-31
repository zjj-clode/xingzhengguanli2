/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * 管理系统中的缓存
 * 
 * @author lsp
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/cache")
public class CacheController extends BaseController {

	@RequiresRoles("administrator")
	@RequestMapping(value = { "list", "" })
	public String list(String cacheName, String key, HttpServletRequest request, HttpServletResponse response, Model model) {

		Map<String, Cache> cacheMap = Maps.newHashMap();

		CacheManager cacheManager = CacheUtils.getCacheManager();
		if (cacheManager != null) {
			String[] cacheNames = cacheManager.getCacheNames();
			for (String name : cacheNames) {
				Cache cache = cacheManager.getCache(name);
				cacheMap.put(name, cache);
			}
		}

		model.addAttribute("cacheMap", cacheMap);
		model.addAttribute("ename", "cache");
		return "modules/sys/cache/cacheList";
	}

	@RequiresRoles("administrator")
	@RequestMapping(value = "delete")
	public String delete(String cacheName, String key, RedirectAttributes redirectAttributes) {
		logger.debug("delete cacheName : {} , key : {}", cacheName, key);
		try {
			if (StringUtils.isNotBlank(cacheName)) {
				if (StringUtils.isNotBlank(key)) {
					CacheUtils.remove(cacheName, key);
				} else {
					Cache cache = CacheUtils.getCacheManager().getCache(cacheName);
					if (cache != null) {
						cache.removeAll();
					}
					CacheUtils.getCacheManager().removeCache(cacheName);
				}
			}
			addMessage(redirectAttributes, "删除缓存成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "删除缓存失败");
		}
		return "redirect:" + adminPath + "/sys/cache/";
	}

}
