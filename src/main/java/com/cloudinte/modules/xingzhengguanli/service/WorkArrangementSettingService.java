package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementSetting;
import com.cloudinte.modules.xingzhengguanli.dao.WorkArrangementSettingDao;

/**
 * 工作安排设置Service
 * @author dcl
 * @version 2019-12-06
 */
@Service
@Transactional(readOnly = true)
public class WorkArrangementSettingService extends CrudService<WorkArrangementSettingDao, WorkArrangementSetting> {
	
	public Page<WorkArrangementSetting> findPage(Page<WorkArrangementSetting> page, WorkArrangementSetting workArrangementSetting) {
		page.setCount(dao.findCount(workArrangementSetting));
		return super.findPage(page, workArrangementSetting);
	}
	
	@Transactional(readOnly = false)
	public void disable(WorkArrangementSetting workArrangementSetting) {
		dao.disable(workArrangementSetting);
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
	public void deleteByIds(WorkArrangementSetting workArrangementSetting) {
		if (workArrangementSetting == null || workArrangementSetting.getIds() == null || workArrangementSetting.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(workArrangementSetting);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<WorkArrangementSetting> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<WorkArrangementSetting> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<WorkArrangementSetting> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}