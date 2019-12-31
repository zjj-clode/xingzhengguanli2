/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;

/**
 * 文章DAO接口
 *
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleDao extends CrudDao<Article> {

    public long findCount(Article article);

	public List<Article> findByIdIn(String[] ids);

	public int updateHitsAddOne(String id);

	public int updateExpiredWeight(Article article);

	public List<Category> findStats(Category category);
	
	void insertCategoryArticle(Article article);
	
	void deleteCategoryArticle(Article article);

	//SQL搜索
	List<Article> searchList(Article article);

	long searchCount(Article article);

	List<Article> findLastAndNextOne(Article article);

	List<Article> findTopList(Date updatedate, String isImage, int size);

	List<Article> findTopImageList(Date updatedate, String isImage, int size);

	List<Article> findPreList(Article article);

	List<Article> findNextList(Article article);

	public Long countArticle(Article article);

	public List<Integer> getNewsNfF(Article article);

}
