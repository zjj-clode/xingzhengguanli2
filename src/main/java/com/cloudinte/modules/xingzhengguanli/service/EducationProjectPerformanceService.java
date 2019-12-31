package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectPerformance;
import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectPerformanceDao;

/**
 * 项目支出绩效目标申报Service
 * @author dcl
 * @version 2019-12-16
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectPerformanceService extends CrudService<EducationProjectPerformanceDao, EducationProjectPerformance> {
	
	public Page<EducationProjectPerformance> findPage(Page<EducationProjectPerformance> page, EducationProjectPerformance educationProjectPerformance) {
		page.setCount(dao.findCount(educationProjectPerformance));
		return super.findPage(page, educationProjectPerformance);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectPerformance educationProjectPerformance) {
		dao.disable(educationProjectPerformance);
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
	public void deleteByIds(EducationProjectPerformance educationProjectPerformance) {
		if (educationProjectPerformance == null || educationProjectPerformance.getIds() == null || educationProjectPerformance.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectPerformance);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectPerformance> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectPerformance> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectPerformance> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}