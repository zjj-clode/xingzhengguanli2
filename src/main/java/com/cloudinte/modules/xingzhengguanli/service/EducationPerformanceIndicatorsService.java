package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationPerformanceIndicators;
import com.cloudinte.modules.xingzhengguanli.dao.EducationPerformanceIndicatorsDao;

/**
 * 绩效指标信息Service
 * @author dcl
 * @version 2019-12-16
 */
@Service
@Transactional(readOnly = true)
public class EducationPerformanceIndicatorsService extends CrudService<EducationPerformanceIndicatorsDao, EducationPerformanceIndicators> {
	
	public Page<EducationPerformanceIndicators> findPage(Page<EducationPerformanceIndicators> page, EducationPerformanceIndicators educationPerformanceIndicators) {
		page.setCount(dao.findCount(educationPerformanceIndicators));
		return super.findPage(page, educationPerformanceIndicators);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationPerformanceIndicators educationPerformanceIndicators) {
		dao.disable(educationPerformanceIndicators);
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
	public void deleteByIds(EducationPerformanceIndicators educationPerformanceIndicators) {
		if (educationPerformanceIndicators == null || educationPerformanceIndicators.getIds() == null || educationPerformanceIndicators.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationPerformanceIndicators);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationPerformanceIndicators> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationPerformanceIndicators> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationPerformanceIndicators> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}