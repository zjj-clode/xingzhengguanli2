package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.WorkAttendance;
import com.cloudinte.modules.xingzhengguanli.dao.WorkAttendanceDao;

/**
 * 工作考勤Service
 * @author dcl
 * @version 2019-12-07
 */
@Service
@Transactional(readOnly = true)
public class WorkAttendanceService extends CrudService<WorkAttendanceDao, WorkAttendance> {
	
	public Page<WorkAttendance> findPage(Page<WorkAttendance> page, WorkAttendance workAttendance) {
		page.setCount(dao.findCount(workAttendance));
		return super.findPage(page, workAttendance);
	}
	
	@Transactional(readOnly = false)
	public void disable(WorkAttendance workAttendance) {
		dao.disable(workAttendance);
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
	public void deleteByIds(WorkAttendance workAttendance) {
		if (workAttendance == null || workAttendance.getIds() == null || workAttendance.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(workAttendance);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<WorkAttendance> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<WorkAttendance> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<WorkAttendance> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}