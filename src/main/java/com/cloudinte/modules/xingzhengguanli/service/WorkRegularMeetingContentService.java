package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeetingContent;
import com.cloudinte.modules.xingzhengguanli.dao.WorkRegularMeetingContentDao;

/**
 * 例会通报工作事项Service
 * @author dcl
 * @version 2019-12-11
 */
@Service
@Transactional(readOnly = true)
public class WorkRegularMeetingContentService extends CrudService<WorkRegularMeetingContentDao, WorkRegularMeetingContent> {
	
	public Page<WorkRegularMeetingContent> findPage(Page<WorkRegularMeetingContent> page, WorkRegularMeetingContent workRegularMeetingContent) {
		page.setCount(dao.findCount(workRegularMeetingContent));
		return super.findPage(page, workRegularMeetingContent);
	}
	
	@Transactional(readOnly = false)
	public void disable(WorkRegularMeetingContent workRegularMeetingContent) {
		dao.disable(workRegularMeetingContent);
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
	public void deleteByIds(WorkRegularMeetingContent workRegularMeetingContent) {
		if (workRegularMeetingContent == null || workRegularMeetingContent.getIds() == null || workRegularMeetingContent.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(workRegularMeetingContent);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<WorkRegularMeetingContent> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<WorkRegularMeetingContent> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<WorkRegularMeetingContent> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}