package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.CmsArticleMovie;
import com.thinkgem.jeesite.modules.cms.dao.CmsArticleMovieDao;

/**
 * 文章视频Service
 * @author gff
 * @version 2018-05-18
 */
@Service
@Transactional(readOnly = true)
public class CmsArticleMovieService extends CrudService<CmsArticleMovieDao, CmsArticleMovie> {
	
	public Page<CmsArticleMovie> findPage(Page<CmsArticleMovie> page, CmsArticleMovie cmsArticleMovie) {
		page.setCount(dao.findCount(cmsArticleMovie));
		return super.findPage(page, cmsArticleMovie);
	}
	
	@Transactional(readOnly = false)
	public void disable(CmsArticleMovie cmsArticleMovie) {
		dao.disable(cmsArticleMovie);
	}
	
	/*
	@Transactional(readOnly = false)
	public void deleteByIds(String[] ids) {
		if (ids == null || ids.length < 1) {
			return;
		}
		dao.deleteByIds(ids);
	}
	*/
	
	@Transactional(readOnly = false)
	public void deleteByIds(CmsArticleMovie cmsArticleMovie) {
		if (cmsArticleMovie == null || cmsArticleMovie.getIds() == null || cmsArticleMovie.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(cmsArticleMovie);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<CmsArticleMovie> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<CmsArticleMovie> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<CmsArticleMovie> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}