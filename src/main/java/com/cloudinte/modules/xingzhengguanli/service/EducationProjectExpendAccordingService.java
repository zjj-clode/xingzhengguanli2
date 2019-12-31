package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpendAccording;
import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectExpendAccordingDao;

/**
 * 项目支出立项依据Service
 * @author dcl
 * @version 2019-12-13
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectExpendAccordingService extends CrudService<EducationProjectExpendAccordingDao, EducationProjectExpendAccording> {
	
	public Page<EducationProjectExpendAccording> findPage(Page<EducationProjectExpendAccording> page, EducationProjectExpendAccording educationProjectExpendAccording) {
		page.setCount(dao.findCount(educationProjectExpendAccording));
		return super.findPage(page, educationProjectExpendAccording);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectExpendAccording educationProjectExpendAccording) {
		dao.disable(educationProjectExpendAccording);
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
	public void deleteByIds(EducationProjectExpendAccording educationProjectExpendAccording) {
		if (educationProjectExpendAccording == null || educationProjectExpendAccording.getIds() == null || educationProjectExpendAccording.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectExpendAccording);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectExpendAccording> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectExpendAccording> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectExpendAccording> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}