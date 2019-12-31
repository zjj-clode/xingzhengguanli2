package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.WorkAttendanceSetting;
import com.cloudinte.modules.xingzhengguanli.dao.WorkAttendanceSettingDao;

/**
 * 加班天数设置Service
 * @author dcl
 * @version 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class WorkAttendanceSettingService extends CrudService<WorkAttendanceSettingDao, WorkAttendanceSetting> {
	
	public Page<WorkAttendanceSetting> findPage(Page<WorkAttendanceSetting> page, WorkAttendanceSetting workAttendanceSetting) {
		page.setCount(dao.findCount(workAttendanceSetting));
		return super.findPage(page, workAttendanceSetting);
	}
	
	@Transactional(readOnly = false)
	public void disable(WorkAttendanceSetting workAttendanceSetting) {
		dao.disable(workAttendanceSetting);
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
	public void deleteByIds(WorkAttendanceSetting workAttendanceSetting) {
		if (workAttendanceSetting == null || workAttendanceSetting.getIds() == null || workAttendanceSetting.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(workAttendanceSetting);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<WorkAttendanceSetting> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<WorkAttendanceSetting> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<WorkAttendanceSetting> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}