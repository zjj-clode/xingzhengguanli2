package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpend;
import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectExpendDao;

/**
 * 项目支出立项Service
 * @author dcl
 * @version 2019-12-13
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectExpendService extends CrudService<EducationProjectExpendDao, EducationProjectExpend> {
	
	public Page<EducationProjectExpend> findPage(Page<EducationProjectExpend> page, EducationProjectExpend educationProjectExpend) {
		page.setCount(dao.findCount(educationProjectExpend));
		return super.findPage(page, educationProjectExpend);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectExpend educationProjectExpend) {
		dao.disable(educationProjectExpend);
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
	public void deleteByIds(EducationProjectExpend educationProjectExpend) {
		if (educationProjectExpend == null || educationProjectExpend.getIds() == null || educationProjectExpend.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectExpend);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectExpend> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectExpend> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectExpend> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}