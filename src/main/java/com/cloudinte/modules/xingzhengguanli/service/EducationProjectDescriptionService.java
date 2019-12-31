package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectDescription;
import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectDescriptionDao;

/**
 * 项目描述Service
 * @author dcl
 * @version 2019-12-14
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectDescriptionService extends CrudService<EducationProjectDescriptionDao, EducationProjectDescription> {
	
	public Page<EducationProjectDescription> findPage(Page<EducationProjectDescription> page, EducationProjectDescription educationProjectDescription) {
		page.setCount(dao.findCount(educationProjectDescription));
		return super.findPage(page, educationProjectDescription);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectDescription educationProjectDescription) {
		dao.disable(educationProjectDescription);
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
	public void deleteByIds(EducationProjectDescription educationProjectDescription) {
		if (educationProjectDescription == null || educationProjectDescription.getIds() == null || educationProjectDescription.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectDescription);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectDescription> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectDescription> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectDescription> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}