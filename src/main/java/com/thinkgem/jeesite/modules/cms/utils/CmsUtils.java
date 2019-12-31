/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.CmsImage;
import com.thinkgem.jeesite.modules.cms.entity.CmsLinkLbt;
import com.thinkgem.jeesite.modules.cms.entity.Link;
import com.thinkgem.jeesite.modules.cms.entity.Movie;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.ArticleDataService;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.service.CmsImageService;
import com.thinkgem.jeesite.modules.cms.service.CmsLinkLbtService;
import com.thinkgem.jeesite.modules.cms.service.LinkService;
import com.thinkgem.jeesite.modules.cms.service.MovieService;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 内容管理工具类
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
@SuppressWarnings("unchecked")
public class CmsUtils {

	private static Logger logger = LoggerFactory.getLogger(CmsUtils.class);

	private static SiteService siteService = SpringContextHolder.getBean(SiteService.class);
	private static CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
	private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
	private static ArticleDataService articleDataService = SpringContextHolder.getBean(ArticleDataService.class);
	private static LinkService linkService = SpringContextHolder.getBean(LinkService.class);
	private static MovieService movieService = SpringContextHolder.getBean(MovieService.class);
	private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);
	private static CmsLinkLbtService cmsLinkLbtService = SpringContextHolder.getBean(CmsLinkLbtService.class);
	private static CmsImageService cmsImageService = SpringContextHolder.getBean(CmsImageService.class);

	private static final String CMS_CACHE = "cmsCache";

	//站点，全部取出存到缓存
	private static final String CMS_CACHE_KEY_SiteList = "CMS_CACHE_KEY_SiteList";

	private static final String Article_CACHE = "Article_CACHE";

	private static final String Category_CACHE = "Category_CACHE";//栏目，全部取出存到缓存

	//链接，全部取出存到缓存
	private static final String CMS_CACHE_KEY_LinkList = "CMS_CACHE_KEY_LinkList";

	//首页轮播图，全部取出存到缓存
	private static final String CMS_CACHE_KEY_CmsLinkLbtList = "CMS_CACHE_KEY_CmsLinkLbtList";

	//首页图片，全部取出存到缓存
	private static final String CMS_CACHE_KEY_CmsImageList = "CMS_CACHE_KEY_CmsImageList";

	//首页联系方式，全部取出存到缓存
	private static final String CMS_CACHE_KEY_InfoAndRemarkList = "CMS_CACHE_KEY_InfoAndRemarkList";

	/*
	 * 
	 */

	/**
	 * 获得站点列表
	 */
	public static List<Site> getSiteList() {
		List<Site> list = (List<Site>) CacheUtils.get(CMS_CACHE, CMS_CACHE_KEY_SiteList);
		if (list == null) {
			Page<Site> page = new Page<>(1, -1);
			page = siteService.findPage(page, new Site());
			list = page.getList();
			CacheUtils.put(CMS_CACHE, CMS_CACHE_KEY_SiteList, list);
		}
		return list;
	}

	/**
	 * 获得站点信息
	 *
	 * @param siteId 站点编号
	 */
	public static Site getSite(String siteId) {
		String id = "1";
		if (StringUtils.isNotBlank(siteId)) {
			id = siteId;
		}
		for (Site site : getSiteList()) {
			if (site.getId().equals(id)) {
				return site;
			}
		}
		return new Site(id);
	}

	/**
	 * 获取栏目
	 *
	 * @param categoryId 栏目编号
	 * @return
	 */
	public static Category getCategory(String categoryId) {
		// return categoryService.get(categoryId);
		String key = "category_id_" + categoryId;
		Category category = (Category) CacheUtils.get(CMS_CACHE, key);
		if (category == null) {
			category = categoryService.get(categoryId);
			CacheUtils.put(CMS_CACHE, key, category);
		}
		return category;
	}

	/**
	 * 获得栏目列表
	 *
	 * @param siteId   站点编号
	 * @param parentId 分类父编号
	 * @param number   获取数目
	 * @param param    预留参数，例： key1:'value1', key2:'value2' ...
	 */
	public static List<Category> getCategoryList(String siteId, String parentId, int number, String param) {
		String key = "getCategoryList_" + siteId + "_" + parentId + "_" + number + "_" + param;
		List<Category> list = (List<Category>) CacheUtils.get(CMS_CACHE, key);
		if (list == null) {
			Page<Category> page = new Page<>(1, number, -1);
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category(parentId));
			if (StringUtils.isNotBlank(param)) {
				@SuppressWarnings({ "unused", "rawtypes" })
				Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
			}
			page = categoryService.find(page, category);
			list = page.getList();
			CacheUtils.put(CMS_CACHE, key, list);
		}
		return list;
	}

	/**
	 * 获取栏目
	 *
	 * @param categoryIds 栏目编号
	 * @return
	 */
	public static List<Category> getCategoryListByIds(String categoryIds) {
		return categoryService.findByIds(categoryIds);
	}

	/**
	 * 获得主导航列表
	 *
	 * @param siteId 站点编号
	 */
	public static List<Category> getMainNavList(String siteId) {
		List<Category> mainNavList = (List<Category>) CacheUtils.get(CMS_CACHE, "mainNavList_" + siteId);
		if (mainNavList == null) {
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category("1"));
			category.setInMenu(Global.SHOW);
			Page<Category> page = new Page<>(1, -1);
			page = categoryService.find(page, category);
			mainNavList = page.getList();
			CacheUtils.put(CMS_CACHE, "mainNavList_" + siteId, mainNavList);
		}
		return mainNavList;
	}

	/**
	 * 获得主导航及子栏目列表
	 *
	 * @param siteId 站点编号
	 */
	public static List<Category> getMainNavListAndChild(String siteId) {
		List<Category> mainNavList = (List<Category>) CacheUtils.get(CMS_CACHE, "mainNavListAndChild_" + siteId);
		if (mainNavList == null) {
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category("1"));
			category.setInMenu(Global.SHOW);
			Page<Category> page = new Page<>(1, -1);
			page = categoryService.find(page, category);
			mainNavList = page.getList();

			/*
			List<Category> childList = null;
			Category entityp = new Category();
			for (Category category1 : mainNavList) {
				logger.debug("category1.getModule() --->{}", category1.getModule());
				// 如果是公共模型，读取其子栏目
				if (StringUtils.isBlank(category1.getModule())) {
					entityp.setParent(category1);
					entityp.setInMenu(Global.SHOW);
					// childList=categoryService.findList(entityp);会显示隐藏的
					childList = categoryService.findMenuList(entityp);
					category1.setChildList(childList);
				}
			}
			*/
			// 递归
			for (Category parent : mainNavList) {
				setCategoryChildList(parent);
			}

			CacheUtils.put(CMS_CACHE, "mainNavListAndChild_" + siteId, mainNavList);
		}
		return mainNavList;
	}

	/**
	 * 递归设置栏目下的子栏目
	 * 
	 * @param category
	 */
	private static void setCategoryChildList(Category category) {
		if (StringUtils.isBlank(category.getModule())) { // 如果是公共模型
			Category c = new Category();
			c.setParent(category);
			c.setInMenu(Global.SHOW); // 在菜单中显示
			List<Category> subCategories = categoryService.findMenuList(c);
			if (!Collections3.isEmpty(subCategories)) { // 如果有子栏目
				for (Category sub : subCategories) {
					setCategoryChildList(sub);
				}
				category.setChildList(subCategories);
			}
		}
	}

	/**
	 * 获得热点新闻
	 *
	 * @param siteId 站点编号
	 */
	public static List<Article> getTopList(int days, String isImage, int size) {
		List<Article> topList = (List<Article>) CacheUtils.get(CMS_CACHE, "topList");
		if (topList == null) {
			topList = articleService.findTopList(days, isImage, size);
			CacheUtils.put(CMS_CACHE, "topList", topList);
		}
		return topList;
	}

	/**
	 * 获得热点新闻
	 *
	 * @param siteId 站点编号
	 */
	public static List<Article> getTopImageList(Integer days, String isImage, int size) {
		String key = "getTopImageList_" + days + "_" + isImage + "_" + size;

		List<Article> list = (List<Article>) CacheUtils.get(CMS_CACHE, key);
		if (list == null) {
			list = articleService.findTopImageList(days, isImage, size);
			CacheUtils.put(CMS_CACHE, key, list);
		}
		return list;
	}

	/**
	 * 获得首页视频
	 *
	 * @param siteId 站点编号
	 */
	public static List<Movie> getTopMovieList(String categoryId) {
		String key = "getTopMovieList_" + categoryId;

		List<Movie> list = (List<Movie>) CacheUtils.get(CMS_CACHE, key);
		if (list == null) {
			Movie movie = new Movie(new Category(categoryId));
			movie.setPage(new Page<>(1, 1));
			list = movieService.findList(movie);
			CacheUtils.put(CMS_CACHE, key, list);
		}
		return list;
	}

	/**
	 * 获得视频列表
	 *
	 * @param categoryId 栏目编号
	 * @param number     获取数目
	 */
	public static List<Movie> getMovieList(String categoryId, int number) {
		Page<Movie> page = new Page<>(1, -1);
		page.setPageNo(1);
		page.setPageSize(number);
		Movie movie = new Movie();
		movie.setCategory(new Category(categoryId));
		List<Movie> movieList = movieService.findPage(page, movie).getList();
		CacheUtils.put(CMS_CACHE, "movieList", movieList);
		return movieList;
	}

	public static void UpdateCache() {
		CacheUtils.put(CMS_CACHE, "topImageList", null);
		CacheUtils.put(CMS_CACHE, "topList", null);
	}

	/**
	 * 获取文章
	 *
	 * @param articleId 文章编号
	 * @return
	 */
	public static Article getArticle(String articleId) {
		return articleService.get(articleId);
	}

	/**
	 * 获取文章
	 *
	 * @param articleId 文章编号
	 * @return
	 */
	public static Article getArticleContent(String categoryId) {

		// List<Category> categoryList = Lists.newArrayList();
		Category categpry = categoryService.get(categoryId);
		List<Article> articleList = articleService.findList(new Article(categpry));
		Article article = new Article();
		if (articleList != null && articleList.size() > 0) {
			article = articleList.get(0);
			articleService.updateHitsAddOne(article.getId());
		}
		article.setArticleData(articleDataService.get(article.getId()));

		return article;
	}

	/**
	 * 获取文章列表
	 *
	 * @param siteId     站点编号
	 * @param categoryId 分类编号
	 * @param number     获取数目
	 * @param param      预留参数，例： key1:'value1', key2:'value2' ... posid 推荐位（1：首页焦点图；2：栏目页文章推荐；） image 文章图片（1：有图片的文章） orderBy 排序字符串
	 * @return ${fnc:getArticleList(category.site.id, category.id, not empty pageSize?pageSize:8, 'posid:2, orderBy: \"hits desc\"')}"
	 */
	public static List<Article> getArticleList(String siteId, String categoryId, int number, String param) {
		String key = siteId + categoryId + number + param;

		Page<Article> page = (Page<Article>) getCache(key);
		if (page == null || Collections3.isEmpty(page.getList())) {
			page = new Page<>(1, number, -1);
			Category category = new Category(categoryId, new Site(siteId));
			category.setParentIds(categoryId);
			Article article = new Article(category);
			if (StringUtils.isNotBlank(param)) {
				@SuppressWarnings({ "rawtypes" })
				Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
				if (new Integer(1).equals(map.get("posid")) || new Integer(2).equals(map.get("posid"))) {
					article.setPosid(String.valueOf(map.get("posid")));
				}
				if (new Integer(1).equals(map.get("image"))) {
					article.setImage(Global.YES);
				}
				if (StringUtils.isNotBlank((String) map.get("orderBy"))) {
					page.setOrderBy((String) map.get("orderBy"));
				}
			}
			article.setDelFlag(Article.DEL_FLAG_NORMAL);
			page = articleService.findPage(page, article, false);
			if (!Collections3.isEmpty(page.getList())) {
				for (Article article1 : page.getList()) {
					article1.getCategory().setId(categoryId);
				}
			}
			putCache(key, page);
		}
		return page.getList();
	}

	/**
	 * 获取链接
	 *
	 * @param linkId 文章编号
	 * @return
	 */
	public static Link getLink(String linkId) {
		return linkService.get(linkId);
	}

	/**
	 * 获取链接列表
	 *
	 * @param siteId     站点编号
	 * @param categoryId 分类编号
	 * @param number     获取数目
	 * @param param      预留参数，例： key1:'value1', key2:'value2' ...
	 * @return
	 */
	public static List<Link> getLinkList(String siteId, String categoryId, int number, String param) {
		/*
		Page<Link> page = new Page<>(1, number, -1);
		Link link = new Link(new Category(categoryId, new Site(siteId)));
		if (StringUtils.isNotBlank(param)) {
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
		}
		link.setDelFlag(Link.DEL_FLAG_NORMAL);
		page = linkService.findPage(page, link, false);
		return page.getList();
		*/

		String key = "getLinkList_siteId_" + siteId + "_categoryId_" + categoryId + "_number_" + number + "_param" + param;

		List<Link> list = (List<Link>) CacheUtils.get(CMS_CACHE, key);
		if (list == null) {
			Page<Link> page = new Page<>(1, number, -1);
			Link link = new Link(new Category(categoryId, new Site(siteId)));
			if (StringUtils.isNotBlank(param)) {
				@SuppressWarnings({ "unused", "rawtypes" })
				Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
				//
			}
			link.setDelFlag(Link.DEL_FLAG_NORMAL);
			list = linkService.findPage(page, link, false).getList();
			logger.debug("list----------->{}", list);
			CacheUtils.put(CMS_CACHE, key, list);
		}
		return list;

	}

	/**
	 * 获取链接列表(已发布)
	 *
	 * @param siteId     站点编号
	 * @param categoryId 分类编号
	 * @param number     获取数目
	 * @param param      预留参数，例： key1:'value1', key2:'value2' ...
	 * @return
	 */
	public static List<Link> getLinkAllList(String siteId, String categoryId, int number, String param) {
		Page<Link> page = new Page<>(1, number, -1);
		Link link = new Link(new Category(categoryId, new Site(siteId)));
		if (StringUtils.isNotBlank(param)) {
			page.setOrderBy(param);
			//@SuppressWarnings({ "unused", "rawtypes" })
			//Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
		}
		link.setDelFlag(Link.DEL_FLAG_NORMAL);
		page = linkService.findAllPage(page, link, false);
		return page.getList();
	}

	// ============== Cms Cache ==============

	public static Object getCache(String key) {
		return CacheUtils.get(CMS_CACHE, key);
	}

	public static void putCache(String key, Object value) {
		CacheUtils.put(CMS_CACHE, key, value);
	}

	public static void removeCache(String key) {
		CacheUtils.remove(CMS_CACHE, key);
	}

	public static void removeAllCache() {
		CacheUtils.removeAll(CMS_CACHE);
	}

	/**
	 * 获得文章动态URL地址
	 */
	public static String getUrlDynamic(Article article) {
		// 外部链接
		if (StringUtils.isNotBlank(article.getLink())) {
			if (StringUtils.endsWithIgnoreCase(article.getLink(), ".pdf")) {
				return new StringBuilder().append(context.getContextPath()).append(Global.getFrontPath()).append("/viewPDF?addr=").append(article.getLink()).toString();
			}
			return article.getLink();
		}
		// 静态化链接
		if (SettingsUtils.getSysConfig("cms.static", false)) {
			// 格式： /html/栏目id/view-文章id.html
			// 访问地址类似： http://localhost/zhaosheng/html/9f8552614ac94310b32e18218e32b25f/view-f4cce922573546e891f00c2e7f5b6075.html
			return String.format("%1$s/%2$s/%3$s/view-%4$s.html", //
					context.getContextPath(), //
					StringUtils.removeEnd(StringUtils.removeStart(SettingsUtils.getSysConfig("cms.static.baseDir", "/s"), "/"), "/"), //
					article.getCategory().getId(), //
					article.getId()//
			);
		}
		// 默认的链接
		return getUrl(article);
	}

	public static String getUrl(Article article) {
		return new StringBuilder().append(context.getContextPath()).append(Global.getFrontPath()).append("/view-").append(article.getCategory().getId()).append("-").append(article.getId()).append(Global.getUrlSuffix()).toString();
	}

	/**
	 * 获得栏目动态URL地址
	 */
	public static String getUrlDynamic(Category category) {
		// 已配置链接
		if (StringUtils.isNotBlank(category.getHref())) {
			if (!category.getHref().contains("://")) {
				return context.getContextPath() + Global.getFrontPath() + category.getHref();
			} else {
				return category.getHref();
			}
		}
		// 静态化链接
		if (SettingsUtils.getSysConfig("cms.static", false)) {
			// 格式： /html/栏目id/list-页码.html
			// 访问地址类似： http://localhost/zhaosheng/html/9f8552614ac94310b32e18218e32b25f/list-1.html
			int pageNo = 1;
			return String.format("%1$s/%2$s/%3$s/list-%4$s.html", //
					context.getContextPath(), //
					StringUtils.removeEnd(StringUtils.removeStart(SettingsUtils.getSysConfig("cms.static.baseDir", "/s"), "/"), "/"), //
					category.getId(), //
					pageNo//
			);
		}
		return getUrl(category);
	}

	public static String getUrl(Category category) {
		if ("movie".equals(category.getModule())) {
			return new StringBuilder().append(context.getContextPath()).append(Global.getFrontPath()).append("/movielist-").append(category.getId()).append(Global.getUrlSuffix()).toString();
		}
		return new StringBuilder().append(context.getContextPath()).append(Global.getFrontPath()).append("/list-").append(category.getId()).append(Global.getUrlSuffix()).toString();

	}

	/**
	 * 获得快捷入口动态URL地址
	 */
	public static String getUrlDynamic(Link link) {
		if (StringUtils.isNotBlank(link.getHref())) {
			if (!link.getHref().contains("://")) {
				return context.getContextPath() + Global.getFrontPath() + link.getHref();
			} else {
				return link.getHref();
			}
		}
		StringBuilder str = new StringBuilder();
		str.append(context.getContextPath()).append(Global.getFrontPath());
		str.append("/list-").append(link.getId()).append(Global.getUrlSuffix());
		return str.toString();
	}

	/**
	 * 从图片地址中去除ContextPath地址
	 *
	 * @param src
	 * @return src
	 */
	public static String formatImageSrcToDb(String src) {
		if (StringUtils.isBlank(src)) {
			return src;
		}
		if (src.startsWith(context.getContextPath() + "/userfiles")) {
			return src.substring(context.getContextPath().length());
		} else {
			return src;
		}
	}

	/**
	 * 从图片地址中加入ContextPath地址
	 *
	 * @param src
	 * @return src
	 */
	public static String formatImageSrcToWeb(String src) {
		if (StringUtils.isBlank(src)) {
			return src;
		}
		if (src.startsWith(context.getContextPath() + "/userfiles")) {
			return src;
		} else {
			return context.getContextPath() + src;
		}
	}

	public static void addViewConfigAttribute(Model model, String param) {
		if (StringUtils.isNotBlank(param)) {
			@SuppressWarnings("rawtypes")
			Map map = JsonMapper.getInstance().fromJson(param, Map.class);
			if (map != null) {
				for (Object o : map.keySet()) {
					model.addAttribute("viewConfig_" + o.toString(), map.get(o));
				}
			}
		}
	}

	public static void addViewConfigAttribute(Model model, Category category) {
		List<Category> categoryList = Lists.newArrayList();
		Category c = category;
		boolean goon = true;
		do {
			if (c.getParent() == null || c.getParent().isRoot()) {
				goon = false;
			}
			categoryList.add(c);
			c = c.getParent();
		} while (goon);
		Collections.reverse(categoryList);
		for (Category ca : categoryList) {
			addViewConfigAttribute(model, ca.getViewConfig());
		}
	}

	/**
	 * 提取文章第一幅图片为缩略图
	 */
	public String getFirstImageFromContent(String content) {
		Document document = Jsoup.parse(content);
		Elements imgs = document.getElementsByTag("img");
		if (imgs.size() > 0) {
			for (Element img : imgs) {
				String src = img.attr("src");
				if (StringUtils.isNotBlank(src)) {
					return src;
				}
			}
		}
		return null;
	}

	public static String getContextPath() {
		return context.getContextPath();
	}

	/**
	 * 获得上一篇文章
	 *
	 * @param article
	 * @return
	 */
	public static Article getPreArticle(String articleid) {
		Article art = null;
		Article art1 = articleService.get(articleid);
		List<Article> artList = articleService.findPreList(art1);
		if (artList != null && artList.size() > 0) {
			art = artList.get(0);
		}

		return art;
	}

	/**
	 * 获得上一篇文章
	 *
	 * @param article
	 * @return
	 */
	public static Article getNextArticle(String articleid) {
		Article art = null;
		Article art1 = articleService.get(articleid);
		////System.out.println(art1);
		List<Article> artList = articleService.findNextList(art1);
		if (artList != null && artList.size() > 0) {
			art = artList.get(0);
		}
		return art;
	}

	public static long geArticleCountByCategoryId(String categoryId) {
		Article article = new Article(new Category(categoryId));
		return articleService.findCount(article);
	}

	/**
	 * 从缓存中获取首页轮播图
	 * 
	 * @param number 获取数目
	 * @Deprecated 改名称为getCmsLinkLbtList
	 */
	@Deprecated
	public static List<CmsLinkLbt> getImageList(int number) {
		return getCmsLinkLbtList(number);
	}

	/**
	 * 从缓存中获取首页轮播图
	 * 
	 * @param number 获取数目
	 * @return 只读的subList
	 */
	public static List<CmsLinkLbt> getCmsLinkLbtList(int number) {
		if (number < 1) {
			return Lists.newArrayList();
		}
		List<CmsLinkLbt> list = getCmsLinkLbtList();
		if (Collections3.isEmpty(list) || list.size() <= number) {
			return list;
		}
		return list.subList(0, number - 1);
	}

	/**
	 * 从缓存中获取首页轮播图
	 */
	public static List<CmsLinkLbt> getCmsLinkLbtList() {
		String key = CMS_CACHE_KEY_CmsLinkLbtList;

		List<CmsLinkLbt> list = (List<CmsLinkLbt>) CacheUtils.get(CMS_CACHE, key);
		if (list == null) {
			list = cmsLinkLbtService.findList(new CmsLinkLbt());
			CacheUtils.put(CMS_CACHE, key, list);
		}
		return list;
	}

	/**
	 * 从缓存中清除首页轮播图。
	 */
	public static void clearCmsLinkLbtCache() {
		CacheUtils.remove(CMS_CACHE, CMS_CACHE_KEY_CmsLinkLbtList);
	}

	/**
	 * 根据Id从缓存中获取首页图片。
	 */
	public static CmsImage getImage(String id) {
		/*CmsImage cmsImage = cmsImageService.get(id);
		return cmsImage;*/
		List<CmsImage> list = getCmsImageList();
		if (!Collections3.isEmpty(list)) {
			for (CmsImage cmsImage : list) {
				if (StringUtils.isNoneBlank(cmsImage.getId()) && cmsImage.getId().equals(id)) {
					return cmsImage;
				}
			}
		}
		return null;
	}

	/**
	 * 从缓存中获取首页图片。
	 */
	public static List<CmsImage> getCmsImageList() {
		String key = CMS_CACHE_KEY_CmsImageList;

		List<CmsImage> list = (List<CmsImage>) CacheUtils.get(CMS_CACHE, key);
		if (list == null) {
			list = cmsImageService.findList(new CmsImage());
			CacheUtils.put(CMS_CACHE, key, list);
		}
		return list;
	}

	/**
	 * 从缓存中清除首页图片。
	 */
	public static void clearCmsImageCache() {
		CacheUtils.remove(CMS_CACHE, CMS_CACHE_KEY_CmsImageList);
	}

	
	/**
	 * 从缓存中清除首页联系方式。
	 */
	public static void clearInfoAndRemarkCache() {
		CacheUtils.remove(CMS_CACHE, CMS_CACHE_KEY_InfoAndRemarkList);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//	获取文章年份，*暂未设置置在内存
	@Deprecated
	public static List<Integer> getNewsNfF(String categoryId, String movieLink) {
		Article article = new Article();
		article.setCategory(categoryService.get(categoryId));
		article.setMovieLink(movieLink);
		List<Integer> articleNfList = articleService.getNewsNfF(article);
		return articleNfList;
	}
}