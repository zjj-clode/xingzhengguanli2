package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.cloudinte.modules.xingzhengguanli.entity.BjProject;
import com.cloudinte.modules.xingzhengguanli.dao.BjProjectDao;

/**
 * 北京市共建项目Service
 * @author dcl
 * @version 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class BjProjectService extends CrudService<BjProjectDao, BjProject> {
	
	public Page<BjProject> findPage(Page<BjProject> page, BjProject bjProject) {
		page.setCount(dao.findCount(bjProject));
		return super.findPage(page, bjProject);
	}
	
	@Transactional(readOnly = false)
	public void disable(BjProject bjProject) {
		dao.disable(bjProject);
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
	public void deleteByIds(BjProject bjProject) {
		if (bjProject == null || bjProject.getIds() == null || bjProject.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(bjProject);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<BjProject> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<BjProject> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<BjProject> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
}