package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectInfo;
import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectInfoDao;

/**
 * 教育教学项目基本信息Service
 * @author dcl
 * @version 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectInfoService extends CrudService<EducationProjectInfoDao, EducationProjectInfo> {
	
	public Page<EducationProjectInfo> findPage(Page<EducationProjectInfo> page, EducationProjectInfo educationProjectInfo) {
		page.setCount(dao.findCount(educationProjectInfo));
		return super.findPage(page, educationProjectInfo);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectInfo educationProjectInfo) {
		dao.disable(educationProjectInfo);
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
	public void deleteByIds(EducationProjectInfo educationProjectInfo) {
		if (educationProjectInfo == null || educationProjectInfo.getIds() == null || educationProjectInfo.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectInfo);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectInfo> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectInfo> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectInfo> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}