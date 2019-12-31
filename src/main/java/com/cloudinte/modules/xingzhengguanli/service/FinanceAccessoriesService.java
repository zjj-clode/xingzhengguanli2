package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.FinanceAccessories;
import com.cloudinte.modules.xingzhengguanli.dao.FinanceAccessoriesDao;

/**
 * 附件管理Service
 * @author dcl
 * @version 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class FinanceAccessoriesService extends CrudService<FinanceAccessoriesDao, FinanceAccessories> {
	
	public Page<FinanceAccessories> findPage(Page<FinanceAccessories> page, FinanceAccessories financeAccessories) {
		page.setCount(dao.findCount(financeAccessories));
		return super.findPage(page, financeAccessories);
	}
	
	@Transactional(readOnly = false)
	public void disable(FinanceAccessories financeAccessories) {
		dao.disable(financeAccessories);
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
	public void deleteByIds(FinanceAccessories financeAccessories) {
		if (financeAccessories == null || financeAccessories.getIds() == null || financeAccessories.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(financeAccessories);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<FinanceAccessories> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<FinanceAccessories> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<FinanceAccessories> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void updateDownloadTimes(FinanceAccessories financeAccessories){
		dao.updateDownloadTimes(financeAccessories);
	}
	
}