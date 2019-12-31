package com.thinkgem.jeesite.modules.cms.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.CmsArticleMovie;

/**
 * 文章视频DAO接口
 * @author gff
 * @version 2018-05-18
 */
@MyBatisDao
public interface CmsArticleMovieDao extends CrudDao<CmsArticleMovie> {
	long findCount(CmsArticleMovie cmsArticleMovie);
	void batchInsert(List<CmsArticleMovie> cmsArticleMovie);
	void batchUpdate(List<CmsArticleMovie> cmsArticleMovie);
	void batchInsertUpdate(List<CmsArticleMovie> cmsArticleMovie);
	void disable(CmsArticleMovie cmsArticleMovie);
	void deleteByIds(CmsArticleMovie cmsArticleMovie);
}