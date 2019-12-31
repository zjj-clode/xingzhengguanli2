package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectImplementationPlan;
import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectImplementationPlanDao;

/**
 * 项目支出实施方案Service
 * @author dcl
 * @version 2019-12-13
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectImplementationPlanService extends CrudService<EducationProjectImplementationPlanDao, EducationProjectImplementationPlan> {
	
	public Page<EducationProjectImplementationPlan> findPage(Page<EducationProjectImplementationPlan> page, EducationProjectImplementationPlan educationProjectImplementationPlan) {
		page.setCount(dao.findCount(educationProjectImplementationPlan));
		return super.findPage(page, educationProjectImplementationPlan);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectImplementationPlan educationProjectImplementationPlan) {
		dao.disable(educationProjectImplementationPlan);
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
	public void deleteByIds(EducationProjectImplementationPlan educationProjectImplementationPlan) {
		if (educationProjectImplementationPlan == null || educationProjectImplementationPlan.getIds() == null || educationProjectImplementationPlan.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectImplementationPlan);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectImplementationPlan> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectImplementationPlan> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectImplementationPlan> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}