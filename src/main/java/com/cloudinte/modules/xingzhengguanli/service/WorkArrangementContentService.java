package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementContent;
import com.cloudinte.modules.xingzhengguanli.dao.WorkArrangementContentDao;

/**
 * 固定值记录Service
 * @author dcl
 * @version 2019-12-11
 */
@Service
@Transactional(readOnly = true)
public class WorkArrangementContentService extends CrudService<WorkArrangementContentDao, WorkArrangementContent> {
	
	public Page<WorkArrangementContent> findPage(Page<WorkArrangementContent> page, WorkArrangementContent workArrangementContent) {
		page.setCount(dao.findCount(workArrangementContent));
		return super.findPage(page, workArrangementContent);
	}
	
	@Transactional(readOnly = false)
	public void disable(WorkArrangementContent workArrangementContent) {
		dao.disable(workArrangementContent);
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
	public void deleteByIds(WorkArrangementContent workArrangementContent) {
		if (workArrangementContent == null || workArrangementContent.getIds() == null || workArrangementContent.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(workArrangementContent);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<WorkArrangementContent> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<WorkArrangementContent> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<WorkArrangementContent> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}