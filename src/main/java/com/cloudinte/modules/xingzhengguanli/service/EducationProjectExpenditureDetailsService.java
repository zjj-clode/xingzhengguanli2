package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpenditureDetails;
import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectExpenditureDetailsDao;

/**
 * 项目支出明细Service
 * @author dcl
 * @version 2019-12-13
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectExpenditureDetailsService extends CrudService<EducationProjectExpenditureDetailsDao, EducationProjectExpenditureDetails> {
	
	public Page<EducationProjectExpenditureDetails> findPage(Page<EducationProjectExpenditureDetails> page, EducationProjectExpenditureDetails educationProjectExpenditureDetails) {
		page.setCount(dao.findCount(educationProjectExpenditureDetails));
		return super.findPage(page, educationProjectExpenditureDetails);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectExpenditureDetails educationProjectExpenditureDetails) {
		dao.disable(educationProjectExpenditureDetails);
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
	public void deleteByIds(EducationProjectExpenditureDetails educationProjectExpenditureDetails) {
		if (educationProjectExpenditureDetails == null || educationProjectExpenditureDetails.getIds() == null || educationProjectExpenditureDetails.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectExpenditureDetails);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectExpenditureDetails> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectExpenditureDetails> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectExpenditureDetails> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}