package com.thinkgem.jeesite.modules.cms.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.cms.dao.CmsLinkLbtDao;
import com.thinkgem.jeesite.modules.cms.entity.CmsLinkLbt;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;

/**
 * 轮播图Service
 * 
 * @author gf
 * @version 2018-01-24
 */
@Service
@Transactional(readOnly = true)
public class CmsLinkLbtService extends CrudService<CmsLinkLbtDao, CmsLinkLbt> {

	@Override
	public Page<CmsLinkLbt> findPage(Page<CmsLinkLbt> page, CmsLinkLbt cmsLinkLbt) {
		page.setCount(dao.findCount(cmsLinkLbt));
		return super.findPage(page, cmsLinkLbt);
	}

	@Transactional(readOnly = false)
	public void disable(CmsLinkLbt cmsLinkLbt) {
		dao.disable(cmsLinkLbt);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void deleteByIds(CmsLinkLbt cmsLinkLbt) {
		if (cmsLinkLbt == null || cmsLinkLbt.getIds() == null || cmsLinkLbt.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(cmsLinkLbt);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void batchInsert(List<CmsLinkLbt> objlist) {
		dao.batchInsert(objlist);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void batchUpdate(List<CmsLinkLbt> objlist) {
		dao.batchUpdate(objlist);
		clearCache();
	}

	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<CmsLinkLbt> objlist) {
		dao.batchInsertUpdate(objlist);
		clearCache();
	}

	@Transactional(readOnly = false)
	@Override
	public void save(CmsLinkLbt entity) {
		super.save(entity);
		clearCache();
	}

	@Transactional(readOnly = false)
	@Override
	public void delete(CmsLinkLbt entity) {
		super.delete(entity);
		clearCache();
	}

	private void clearCache() {
		CmsUtils.clearCmsLinkLbtCache();
	}

	@Transactional(readOnly = false)
	public Page<CmsLinkLbt> findPage(Page<CmsLinkLbt> page, CmsLinkLbt cmsLinkLbt, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate = (Date) CacheUtils.get("updateExpiredWeightDateByLink");
		if (updateExpiredWeightDate == null || updateExpiredWeightDate != null && updateExpiredWeightDate.getTime() < new Date().getTime()) {
			dao.updateExpiredWeight(cmsLinkLbt);
			CacheUtils.put("updateExpiredWeightDateByLink", DateUtils.addHours(new Date(), 6));
			clearCache();
		}
		cmsLinkLbt.getSqlMap().put("dsf", dataScopeFilter(cmsLinkLbt.getCurrentUser(), "o", "u"));

		return super.findPage(page, cmsLinkLbt);
	}

}