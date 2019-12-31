/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.web;

import java.io.File;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.common.utils.CmsIndexDao;
import com.cloudinte.common.utils.CreateHtml;
import com.cloudinte.modules.xingzhengguanli.utils.JspToPdf;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mapper.ResponseObject;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleData;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.ArticleDataService;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.service.FileTplService;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.cms.utils.TplUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 文章Controller
 *
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleDataService articleDataService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FileTplService fileTplService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private CmsIndexDao cmsIndexDao;

	@ModelAttribute
	public Article get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return articleService.get(id);
		} else {
			return new Article();
		}
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = { "list", "" })
	public String list(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			article.setCreateBy(opUser);
		}
		
		Page<Article> page = articleService.findPage(new Page<Article>(request, response), article, true);
		model.addAttribute("page", page);
		setBase64EncodedQueryStringToEntity(request, article);
		model.addAttribute("categoryId", article.getCategory() != null ? article.getCategory().getId() : "");
		model.addAttribute("ename", "article");
		return "modules/cms/articleList";
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(Article article, Model model) {

		// 关于页面初始化时，栏目选择框的勾选显示逻辑：
		// 1、修改文章的时候，勾选文章所属的栏目，有几个栏目就勾选几个。
		// 2、添加文章的时候，如果参数携带了栏目id，且携带的栏目id对应数据库中存在，则勾选这个栏目；如果未携带栏目id或携带的栏目id对应数据库中不存在这个栏目，不勾选任何栏目。

		Category category = article.getCategory();
		// 如果当前传参有子节点，则选择取消传参选择。即：只有叶子节点栏目下才能有文章
		if (category != null && StringUtils.isNotBlank(category.getId())) {
			List<Category> list = categoryService.findByParentId(category.getId(), Site.getCurrentSiteId());
			if (list.size() > 0) { // 非叶子节点栏目
				//article.setCategory(null);
				article.setCategory(new Category()); // 将栏目置空（无id无name）
			} else {
				category = categoryService.get(category); // 根据id获取更详细的栏目信息
				if (category == null) {// 此栏目不存在 
					category = new Category();
				}
				article.setCategory(category);
			}
		}

		// 文章所属栏目集合。
		List<Category> categoryList;
		// 添加
		if (StringUtils.isBlank(article.getId())) {
			// 如果指定了栏目，所属栏目集合即为此栏目
			if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getId())) {
				categoryList = Lists.newArrayList(article.getCategory());
			} else {
				categoryList = Lists.newArrayList();
			}
		}
		// 修改
		else {
			// 获取其所属栏目集合
			categoryList = categoryService.findCategoryByArticle(article.getId());
		}
		// 构建栏目选择框ztree的数据
		List<String> categoryIdList = Lists.newArrayList();
		List<String> categoryNameList = Lists.newArrayList();
		if (!Collections3.isEmpty(categoryList)) {
			for (Category c : categoryList) {
				categoryIdList.add(c.getId());
				categoryNameList.add(c.getName());
			}
		}
		// 文章所属栏目id，多个以英文隔开
		String categoryId = StringUtils.join(categoryIdList, ",");
		// 文章所属栏目name，多个以英文隔开
		String categoryName = StringUtils.join(categoryNameList, ",");
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("categoryName", categoryName);

		// 修改时，拉取文章副表内容
		if (StringUtils.isNotBlank(article.getId())) {
			article.setArticleData(articleDataService.get(article.getId()));
		}
		//
		model.addAttribute("contentViewList", getTplContent());
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("article", article);
		//CmsUtils.addViewConfigAttribute(model, article.getCategory());
		model.addAttribute("ename", "article");

		logger.debug("添加内容，栏目 article.getCategory().getId() ====>{}", article.getCategory());

		if(article.getCategory().getId().equals(SettingsUtils.getSysConfig("red.file.id", "1f3508110a4d449180f163f1ef3d979c"))){
			
			if(StringUtils.isBlank(article.getId())){
				long count = articleService.findCount(article);
				String code = String.valueOf(count+1);
				article.setCode(code);
			}
			
			return "modules/cms/redFileForm";
		}
		
		return "modules/cms/articleForm";
	}
	
	@RequestMapping(value = "showRedFile")
	public String showRedFile(Article article, Model model,HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isNotBlank(article.getId())) {
			article.setArticleData(articleDataService.get(article.getId()));
		}
		model.addAttribute("article", article);
		return "modules/cms/showRedFile";
	}
	@RequestMapping(value = "createRedFile")
	public String createRedFile(Article article, Model model,HttpServletRequest request,HttpServletResponse response) {
		
		
		// 修改时，拉取文章副表内容
		if (StringUtils.isNotBlank(article.getId())) {
			ArticleData articleDate = articleDataService.get(article.getId());
			
			if(articleDate != null && StringUtils.isNotBlank(articleDate.getContent())){
				String content = articleDate.getContent();
				articleDate.setContent(content.replace("黑体", "simhei"));
				article.setArticleData(articleDate);
			}
			//article.setArticleData(articleDataService.get(article.getId()));
		}
		
		String path1 = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + (80 == request.getServerPort() ? "" : ":" + request.getServerPort()) + path1;
		String image = basePath+ "/static/images/timg.jpg";
		Map<String, Object> map = Maps.newConcurrentMap();
		map.put("article", article);
		
		String templateFile = "/WEB-INF/classes/templates/modules/pdf/applyPdf.html";
		String filename = StringEscapeUtils.unescapeHtml4(article.getTitle());
		String encodedFilename = null;
		String userAgent = request.getHeader("User-Agent");
		boolean ifIE = userAgent.contains("MSIE") || userAgent.contains("Trident");
		// 针对IE或者以IE为内核的浏览器：
		try {
			if (ifIE) {
				encodedFilename = URLEncoder.encode(filename, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				encodedFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;filename=" + encodedFilename + ".pdf");
			
			 String path = request.getSession().getServletContext().getRealPath("/"); 
			 String filePath = path  +"upload"+File.separator +"redFile"+File.separator + filename + ".pdf";
			 File file = new File(filePath);
			 logger.debug("创建文件 ： {}", filePath);
			// 指定font
			String simsunfontPath = Servlets.getRealPath(request, "/WEB-INF/classes/fonts/simsun.ttf");
			String fasongfontPath = Servlets.getRealPath(request, "/WEB-INF/classes/fonts/仿宋_GB2312.ttf");
			String simheifontPath = Servlets.getRealPath(request, "/WEB-INF/classes/fonts/simhei.ttf");
			String calibrifontPath = Servlets.getRealPath(request, "/WEB-INF/classes/fonts/calibri.ttf");
			JspToPdf.fontPath = new String[] { simsunfontPath,fasongfontPath,simheifontPath,calibrifontPath };
			JspToPdf.exportToFile(filePath, Servlets.getRealPath(request, templateFile), map);
			// 输出文件
			logger.debug("输出文件 ： {}", filePath);
			FileUtils.copyFile(file, response.getOutputStream());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "save")
	public String save(Article article, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, article)) {
			return form(article, model);
		}
		List<String> categoryIdList = Arrays.asList(article.getCategory().getId().split(","));
		if (!Collections3.isEmpty(categoryIdList)) {
			article.setCategory(new Category(categoryIdList.get(0)));
			article.setCategoryIdList(categoryIdList);
			articleService.save(article);
			onArticleSaveOrUpdate(article);
		}
		//
		CacheUtils.removeAll("cmsCache");
		String categoryId = Collections3.isEmpty(categoryIdList) ? null : categoryIdList.get(0);
		addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
		return "redirect:" + adminPath + "/cms/?repage&module=article&category.id=" + (categoryId != null ? categoryId : "");
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	public String delete(Article article, String categoryId, @RequestParam(required = false) Boolean isRe, RedirectAttributes redirectAttributes) {
		// 如果没有审核权限，则不允许删除或发布。
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
			addMessage(redirectAttributes, "你没有删除或发布权限");
		}
		articleService.delete(article);
		onArticleDelete(article);
		CacheUtils.removeAll("cmsCache");
		addMessage(redirectAttributes, (isRe != null && isRe ? "发布" : "删除") + "文章成功");
		//return "redirect:" + adminPath + "/cms/article/?repage&category.id=" + (categoryId != null ? categoryId : "");
		return "redirect:" + adminPath + "/cms/article/?repage&" + getBase64DecodedQueryStringFromEntity(article);
	}

	private void onArticleSaveOrUpdate(final Article article) {
		final String basePathWithoutContextPath = Servlets.getBasePathWithoutContextPath();
		final String basePath = Servlets.getBasePath();
		final String baseDir = Servlets.getRealPath(SettingsUtils.getSysConfig("cms.static.baseDir", "/s"));

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 索引
					if (StringUtils.isBlank(article.getId())) {
						cmsIndexDao.save(article);
					} else {
						cmsIndexDao.update(article);
					}

					// 静态化:本文章、文章所在栏目、首页
					if (SettingsUtils.getSysConfig("cms.static", false)) {
						CreateHtml.staticArticle(article, basePathWithoutContextPath, baseDir);
						CreateHtml.staticCategory(categoryService.get(article.getCategory()), basePathWithoutContextPath, baseDir);
						CreateHtml.staticIndex(basePath, baseDir);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void onArticleDelete(final Article article) {
		final String basePathWithoutContextPath = Servlets.getBasePathWithoutContextPath();
		final String basePath = Servlets.getBasePath();
		final String baseDir = Servlets.getRealPath(SettingsUtils.getSysConfig("cms.static.baseDir", "/s"));

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 索引
					cmsIndexDao.delete(article.getId(), "article");

					// 静态化
					if (SettingsUtils.getSysConfig("cms.static", false)) {
						// 删除生成的html文件
						FileUtils.deleteFile(baseDir + File.separator + article.getCategory().getId() + File.separator + "view-" + article.getId() + ".html");
						CreateHtml.staticCategory(categoryService.get(article.getCategory()), basePathWithoutContextPath, baseDir);
						CreateHtml.staticIndex(basePath, baseDir);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 文章选择列表
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "selectList")
	public String selectList(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
		list(article, request, response, model);
		return "modules/cms/articleSelectList";
	}

	/**
	 * 通过编号获取文章标题
	 */
	@RequiresPermissions("cms:article:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids) {
		List<Object[]> list = articleService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
	}

	private List<String> getTplContent() {
		List<String> tplList = fileTplService.getNameListByPrefix(siteService.get(Site.getCurrentSiteId()).getSolutionPath());
		tplList = TplUtils.tplTrim(tplList, Article.DEFAULT_TEMPLATE, "");
		return tplList;
	}

	/**
	 * 生成指定文章的索引
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "doIndex")
	@ResponseBody
	public ResponseObject doIndex(Article article) {
		try {
			cmsIndexDao.update(article);
			return ResponseObject.success().msg("生成索引成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseObject.failure().msg("生成索引失败");
		}
	}

	/**
	 * 生成指定文章的索引
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "doIndexByIds")
	@ResponseBody
	public ResponseObject doIndexByIds(Article article) {
		List<Article> list = Lists.newArrayList();
		for (String id : article.getIds()) {
			list.add(articleService.get(id));
		}
		return index(list);
	}

	/**
	 * 生成栏目下文章的索引
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "doIndexByCatetory")
	@ResponseBody
	public ResponseObject doIndexByCatetory(Article article) {
		List<Article> list = null;
		Category category = article.getCategory();
		if (category != null && !StringUtils.isBlank(category.getId())) {
			list = articleService.findList(article);
		} else {
			list = Lists.newArrayList();
		}
		return index(list);
	}

	/**
	 * 生成全部文章的索引
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "doIndexAll")
	@ResponseBody
	public ResponseObject doIndexAll(Article article) {
		if (article.getCategory() == null) {
			article.setCategory(new Category());
		}
		List<Article> list = articleService.findList(article);
		return index(list);
	}

	private ResponseObject index(List<Article> list) {
		try {
			for (Article a : list) {
				cmsIndexDao.update(a);
			}
			return ResponseObject.success().msg("生成索引成功，共【" + (list == null ? 0 : list.size()) + "】篇文章。");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseObject.failure().msg("生成索引失败");
		}
	}

	/**
	 * 全部静态化
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "staticAll")
	@ResponseBody
	public ResponseObject staticAll(HttpServletRequest request) {
		try {
			//首页
			CreateHtml.staticIndex(request);
			//栏目
			Category category = new Category();
			//category.setInList(Global.SHOW);
			staticCategoryAndArticle(categoryService.findList(category), request);
			return ResponseObject.success().msg("全部静态化成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseObject.failure().msg("全部静态化失败");
		}
	}

	private void staticCategoryAndArticle(List<Category> categories, HttpServletRequest request) {
		for (Category category : categories) {
			//栏目
			CreateHtml.staticCategory(categoryService.get(category), request);
			//内容
			List<Article> articles = articleService.findList(new Article(category));
			for (Article article : articles) {
				if (StringUtils.isBlank(article.getLink())) {
					CreateHtml.staticArticle(article, request);
				}
			}
			// 是否还有子栏目
			List<Category> subCategories = categoryService.findByParentId(category.getId(), "1");
			if (!Collections3.isEmpty(subCategories)) {
				staticCategoryAndArticle(subCategories, request);
			}
		}
	}

	/**
	 * 首页静态化
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "staticIndex")
	@ResponseBody
	public ResponseObject staticIndex(HttpServletRequest request) {
		try {
			CreateHtml.staticIndex(request);
			return ResponseObject.success().msg("首页静态化成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseObject.failure().msg("首页静态化失败");
		}
	}

	/**
	 * 栏目页静态化
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "staticCategory")
	@ResponseBody
	public ResponseObject staticCategory(Article article, HttpServletRequest request) {
		try {
			Category category = categoryService.get(article.getCategory());
			if (category != null) {
				CreateHtml.staticCategory(category, request);
			}
			return ResponseObject.success().msg("栏目页静态化成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseObject.failure().msg("栏目页静态化失败");
	}

	/**
	 * 内容页静态化
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "staticArticle")
	@ResponseBody
	public ResponseObject staticArticle(Article article, HttpServletRequest request) {
		try {
			List<Article> list = articleService.findList(article);
			for (Article a : list) {
				CreateHtml.staticArticle(a, request);
			}
			return ResponseObject.success().msg("内容页静态化成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseObject.failure().msg("内容页静态化失败");
		}
	}

}
