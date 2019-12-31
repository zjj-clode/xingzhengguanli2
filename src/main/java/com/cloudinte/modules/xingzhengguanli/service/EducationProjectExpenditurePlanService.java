package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectExpenditurePlanDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpenditurePlan;
import com.cloudinte.modules.xingzhengguanli.entity.ExportExpenditurePlan;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 项目支出计划Service
 * @author dcl
 * @version 2019-12-13
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectExpenditurePlanService extends CrudService<EducationProjectExpenditurePlanDao, EducationProjectExpenditurePlan> {
	
	public Page<EducationProjectExpenditurePlan> findPage(Page<EducationProjectExpenditurePlan> page, EducationProjectExpenditurePlan educationProjectExpenditurePlan) {
		page.setCount(dao.findCount(educationProjectExpenditurePlan));
		return super.findPage(page, educationProjectExpenditurePlan);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectExpenditurePlan educationProjectExpenditurePlan) {
		dao.disable(educationProjectExpenditurePlan);
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
	public void deleteByIds(EducationProjectExpenditurePlan educationProjectExpenditurePlan) {
		if (educationProjectExpenditurePlan == null || educationProjectExpenditurePlan.getIds() == null || educationProjectExpenditurePlan.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectExpenditurePlan);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectExpenditurePlan> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectExpenditurePlan> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectExpenditurePlan> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
	public List<ExportExpenditurePlan> findExpenditurePlanList(EducationProjectExpenditurePlan educationProjectExpenditurePlan){
		return dao.findExpenditurePlanList(educationProjectExpenditurePlan);
	}
	
}