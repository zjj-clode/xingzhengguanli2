/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.common.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudinte.modules.log.entity.BusinessLog;
import com.cloudinte.modules.log.service.BusinessLogService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.sys.entity.SiteAccess;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SiteAccessService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 首页Controller
 *
 * @author wtman
 * @version 2016-08-13
 */
@Controller
@RequestMapping(value = "${adminPath}/home")
public class HomeController extends BaseController {

	@Autowired
	private BusinessLogService businessLogService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SiteAccessService siteAccessService;
	@Autowired
	private ArticleService articleService;

	@RequestMapping(value = "")
	public String home(HttpServletRequest request, HttpServletResponse response, Model model) {

		//Page<BusinessLog> page = businessLogService.findPage(new Page<BusinessLog>(request, response, 5), new BusinessLog());
		//model.addAttribute("businessLogList", page.getList());

		BusinessLog businessLog = new BusinessLog();
		businessLog.setPage(new Page<BusinessLog>(1, 5));
		List<BusinessLog> businessLogs = businessLogService.findList(businessLog);
		model.addAttribute("businessLogList", businessLogs);

		
		//最新注册考生
		User u = new User();
		u.setUserType("3");
		Page<User> uList = systemService.findNewList(new Page<User>(request, response, 3), u);
		model.addAttribute("uList", uList.getList());

		//今日登录次数
		SiteAccess sa = new SiteAccess();
		Long saNum = siteAccessService.countSiteAccess(sa);
		model.addAttribute("saNum", saNum);

		//今日访问量
		SiteAccess saView = new SiteAccess();
		Long saViewMum = siteAccessService.countViewSiteAccess(saView);
		model.addAttribute("saViewMum", saViewMum);

		//今日资讯发布
		Article article = new Article();
		Long aCount = articleService.countArticle(article);
		model.addAttribute("aCount", aCount);

		return "modules/common/home/adminHome";

	}
}