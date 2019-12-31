package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.ImportantBigEvent;
import com.cloudinte.modules.xingzhengguanli.dao.ImportantBigEventDao;

/**
 * 三重一大事项Service
 * @author dcl
 * @version 2019-12-04
 */
@Service
@Transactional(readOnly = true)
public class ImportantBigEventService extends CrudService<ImportantBigEventDao, ImportantBigEvent> {
	
	public Page<ImportantBigEvent> findPage(Page<ImportantBigEvent> page, ImportantBigEvent importantBigEvent) {
		page.setCount(dao.findCount(importantBigEvent));
		return super.findPage(page, importantBigEvent);
	}
	
	@Transactional(readOnly = false)
	public void disable(ImportantBigEvent importantBigEvent) {
		dao.disable(importantBigEvent);
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
	public void deleteByIds(ImportantBigEvent importantBigEvent) {
		if (importantBigEvent == null || importantBigEvent.getIds() == null || importantBigEvent.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(importantBigEvent);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<ImportantBigEvent> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<ImportantBigEvent> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<ImportantBigEvent> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}