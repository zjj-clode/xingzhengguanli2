/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.common.utils.CmsIndexDao;
import com.cloudinte.common.utils.LuceneUtils;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.ArticleDao;
import com.thinkgem.jeesite.modules.cms.dao.ArticleDataDao;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleData;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 文章Service
 *
 * @author ThinkGem
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleDao, Article> implements InitializingBean {
	
	@Autowired
	private ArticleDataDao articleDataDao;
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private CmsIndexDao cmsIndexDao;
	
	@Transactional(readOnly = false)
	public Page<Article> findPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate = (Date) CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || updateExpiredWeightDate != null && updateExpiredWeightDate.getTime() < new Date().getTime()) {
			dao.updateExpiredWeight(article);
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		//		DetachedCriteria dc = dao.createDetachedCriteria();
		//		dc.createAlias("category", "category");
		//		dc.createAlias("category.site", "category.site");
		if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())) {
			Category category = categoryDao.get(article.getCategory().getId());
			if (category == null) {
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			article.setCategory(category);
		} else {
			article.setCategory(new Category());
		}
		Page<Article> page1 = super.findPage(page, article);
		if (!Collections3.isEmpty(page1.getList())) {
		    setCategoryListInArticle(page1.getList());
        }
		//		if (StringUtils.isBlank(page.getOrderBy())){
		//			page.setOrderBy("a.weight,a.update_date desc");
		//		}
		//		return dao.find(page, dc);
		//	article.getSqlMap().put("dsf", dataScopeFilter(article.getCurrentUser(), "o", "u"));
		return page1;
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(Article article) {
		if (article.getArticleData().getContent() != null) {
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(article.getArticleData().getContent()));
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
			article.setDelFlag(Article.DEL_FLAG_AUDIT);
		}
		// 如果栏目不需要审核，则将该内容设为发布状态
		/*if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getId())) {
			Category category = categoryDao.get(article.getCategory().getId());
			if (!Global.YES.equals(category.getIsAudit())) {
				article.setDelFlag(Article.DEL_FLAG_NORMAL);
			}
		}*/
		if (StringUtils.isNotBlank(article.getViewConfig())) {
			article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
		}
		//如果新闻简介内容为空，设置新闻简介信息
		if (StringUtils.isBlank(article.getDescription())) {
			article.setDescription(StringUtils.substring(StringUtils.replaceHtml(article.getArticleData().getContent()), 0, 200));
		}
		ArticleData articleData = new ArticleData();
		if (StringUtils.isBlank(article.getId())) {
			if (article.getReleaseDate() == null) {
				article.setReleaseDate(new Date());
			}
			article.preInsert();
			//
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			//给appImage赋值，默认为image属性的值，如果image属性为空，从articleData抽取第一张图片。简单处理，未进行缩放等操作。
			addAppImg(article, articleData);
			//
			dao.insert(article);
			//插入栏目文章关系
			dao.insertCategoryArticle(article);
			articleDataDao.insert(articleData);
		} else {
			article.preUpdate();
			//
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			//给appImage赋值，默认为image属性的值，如果image属性为空，从articleData抽取第一张图片。简单处理，未进行缩放等操作。
			addAppImg(article, articleData);
			//
			dao.update(article);
			//更新栏目文章关系
			dao.deleteCategoryArticle(article);
			if (!Collections3.isEmpty(article.getCategoryIdList())) {
                dao.insertCategoryArticle(article);
            }else {
                throw new ServiceException(article.getTitle() + "没有设置栏目！");
            }
			
			articleDataDao.update(article.getArticleData());
		}
	}
	
	@Override
	@Transactional(readOnly = false)
    public void delete(Article article) {
        dao.delete(article);
        dao.deleteCategoryArticle(article);
    }

    /**
	 * 获取首图
	 */
	private void addAppImg(Article article, ArticleData articleData) {
		String appImage = article.getImage();
		if (StringUtils.isBlank(appImage)) {
			Document document = Jsoup.parse(articleData.getContent());
			Elements imgs = document.getElementsByTag("img");
			if (!Collections3.isEmpty(imgs)) {
				for (Element imgElement : imgs) {
					String src = imgElement.attr("src");
					if (!StringUtils.isBlank(src)) {
						if (src.toLowerCase().startsWith("http") || src.toLowerCase().startsWith("/")) {
							appImage = src;
							break;
						}
					}
				}
			}
		}
		article.setAppImage(appImage);
	}
	
	@Transactional(readOnly = false)
	public void delete(Article article, Boolean isRe) {
		//		dao.updateDelFlag(id, isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// 使用下面方法，以便更新索引。
		//Article article = dao.get(id);
		//article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		//dao.insert(article);
		super.delete(article);
	}
	
	/**
	 * 通过编号获取内容标题
	 *
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if (ids == null) {
			return new ArrayList<Object[]>();
		}
		List<Object[]> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids, ",");
		Article e = null;
		for (int i = 0; idss.length - i > 0; i++) {
			e = dao.get(idss[i]);
			list.add(new Object[] { e.getCategory().getId(), e.getId(), StringUtils.abbr(e.getTitle(), 50) });
		}
		return list;
	}
	
	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}
	
	/**
	 * 更新索引
	 */
	public void createIndex() {
		//dao.createIndex();
	}
	
	/**
	 * 全文检索
	 */
	//FIXME 暂不提供检索功能
	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate) {
		
		// 设置查询条件
		//		BooleanQuery query = dao.getFullTextQuery(q, "title","keywords","description","articleData.content");
		//
		//		// 设置过滤条件
		//		List<BooleanClause> bcList = Lists.newArrayList();
		//
		//		bcList.add(new BooleanClause(new TermQuery(new Term(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL)), Occur.MUST));
		//		if (StringUtils.isNotBlank(categoryId)){
		//			bcList.add(new BooleanClause(new TermQuery(new Term("category.ids", categoryId)), Occur.MUST));
		//		}
		//
		//		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
		//			bcList.add(new BooleanClause(new TermRangeQuery("updateDate", beginDate.replaceAll("-", ""),
		//					endDate.replaceAll("-", ""), true, true), Occur.MUST));
		//		}
		
		//BooleanQuery queryFilter = dao.getFullTextQuery((BooleanClause[])bcList.toArray(new BooleanClause[bcList.size()]));
		
		//		//System.out.println(queryFilter);
		
		// 设置排序（默认相识度排序）
		//FIXME 暂时不提供lucene检索
		//Sort sort = null;//new Sort(new SortField("updateDate", SortField.DOC, true));
		// 全文检索
		//dao.search(page, query, queryFilter, sort);
		// 关键字高亮
		//dao.keywordsHighlight(query, page.getList(), 30, "title");
		//dao.keywordsHighlight(query, page.getList(), 130, "description","articleData.content");
		
		return page;
	}
	
	///////
	public List<Article> searchList(Article article) {
		return dao.searchList(article);
	}
	
	public long searchCount(Article article) {
		return dao.searchCount(article);
	}
	
	public Page<Article> searchPage(Page<Article> page, Article article) {
		page.setCount(searchCount(article));
		article.setPage(page);
		page.setList(searchList(article));
		return page;
	}
	
	/**
	 * 前一篇、后一篇，按发布时间排序确定位置。
	 */
	public Article[] findLastAndNextOne(Article article) {
		List<Article> articles = dao.findLastAndNextOne(article);
		return articles.toArray(new Article[] {});
	}
	
	public List<Article> findPreList(Article article) {
		return dao.findPreList(article);
	}
	
	@Scheduled(cron = "0 0 * * * ?")
	public void updateCachetask() {
		CmsUtils.UpdateCache();
		
	}
	
	public List<Article> findTopList(int days, String isImage, int size) {
		Date updateDate = DateUtils.addDays(new Date(), -1 * days);
		return dao.findTopList(updateDate, isImage, size);
	}
	
	public List<Article> findTopImageList(Integer days, String isImage, int size) {
		Date updateDate = null;
		if (days != null) {
			updateDate = DateUtils.addDays(new Date(), -1 * days);
		}
		return dao.findTopImageList(updateDate, isImage, size);
	}
	
	public List<Article> findNextList(Article article) {
		return dao.findNextList(article);
	}
	
	public long findCount(Article article) {
		return dao.findCount(article);
	}
	
	public Long countArticle(Article article) {
		return dao.countArticle(article);
	}
	
	public List<Integer> getNewsNfF(Article article) {
		
		return dao.getNewsNfF(article);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			if (LuceneUtils.getQaDirectory().listAll().length == 0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						reIndex();
					}
				}).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional(readOnly = false)
	public void reIndex() {
		// TODO
		Article article = new Article();
		article.setDelFlag(Article.DEL_FLAG_NORMAL);
		List<Article> list = findList(article);
		for (Article a : list) {
			cmsIndexDao.update(a);
		}
	}
	
	private void setCategoryListInArticle(Article article) {
	    List<Category> categoryList = categoryDao.findCategoryByArticle(article.getId());
	    article.setCategoryList(categoryList);
	}
	
	private void setCategoryListInArticle(List<Article> articleList) {
	    for (Article article : articleList) {
	        List<Category> categoryList = categoryDao.findCategoryByArticle(article.getId());
	        article.setCategoryList(categoryList);
        }
	}
	
}
