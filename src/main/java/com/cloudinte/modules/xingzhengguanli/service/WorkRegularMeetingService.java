package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeeting;
import com.cloudinte.modules.xingzhengguanli.dao.WorkRegularMeetingDao;

/**
 * 工作例会征集Service
 * @author dcl
 * @version 2019-12-11
 */
@Service
@Transactional(readOnly = true)
public class WorkRegularMeetingService extends CrudService<WorkRegularMeetingDao, WorkRegularMeeting> {
	
	public Page<WorkRegularMeeting> findPage(Page<WorkRegularMeeting> page, WorkRegularMeeting workRegularMeeting) {
		page.setCount(dao.findCount(workRegularMeeting));
		return super.findPage(page, workRegularMeeting);
	}
	
	@Transactional(readOnly = false)
	public void disable(WorkRegularMeeting workRegularMeeting) {
		dao.disable(workRegularMeeting);
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
	public void deleteByIds(WorkRegularMeeting workRegularMeeting) {
		if (workRegularMeeting == null || workRegularMeeting.getIds() == null || workRegularMeeting.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(workRegularMeeting);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<WorkRegularMeeting> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<WorkRegularMeeting> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<WorkRegularMeeting> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}