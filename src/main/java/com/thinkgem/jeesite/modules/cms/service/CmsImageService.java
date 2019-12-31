package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.dao.CmsImageDao;
import com.thinkgem.jeesite.modules.cms.entity.CmsImage;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;

/**
 * 图片管理Service
 * 
 * @author G6
 * @version 2019-01-11
 */
@Service
@Transactional(readOnly = true)
public class CmsImageService extends CrudService<CmsImageDao, CmsImage> {

	@Override
	public Page<CmsImage> findPage(Page<CmsImage> page, CmsImage cmsImage) {
		page.setCount(dao.findCount(cmsImage));
		return super.findPage(page, cmsImage);
	}

	@Transactional(readOnly = false)
	public void disable(CmsImage cmsImage) {
		dao.disable(cmsImage);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void deleteByIds(CmsImage cmsImage) {
		if (cmsImage == null || cmsImage.getIds() == null || cmsImage.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(cmsImage);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void batchInsert(List<CmsImage> objlist) {
		dao.batchInsert(objlist);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void batchUpdate(List<CmsImage> objlist) {
		dao.batchUpdate(objlist);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<CmsImage> objlist) {
		dao.batchInsertUpdate(objlist);
		clearCache();
	}

	@Transactional(readOnly = false)
	@Override
	public void save(CmsImage entity) {
		super.save(entity);
		clearCache();
	}

	@Transactional(readOnly = false)
	@Override
	public void delete(CmsImage entity) {
		super.delete(entity);
		clearCache();
	}

	private void clearCache() {
		CmsUtils.clearCmsImageCache();
	}
}