package com.cloudinte.modules.xingzhengguanli.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangement;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementContent;
import com.cloudinte.modules.xingzhengguanli.dao.WorkArrangementDao;

/**
 * 工作安排Service
 * @author dcl
 * @version 2019-12-06
 */
@Service
@Transactional(readOnly = true)
public class WorkArrangementService extends CrudService<WorkArrangementDao, WorkArrangement> {
	
	@Autowired
	private WorkArrangementContentService workArrangementContentService;
	
	
	public Page<WorkArrangement> findPage(Page<WorkArrangement> page, WorkArrangement workArrangement) {
		page.setCount(dao.findCount(workArrangement));
		return super.findPage(page, workArrangement);
	}
	
	@Transactional(readOnly = false)
	public void disable(WorkArrangement workArrangement) {
		dao.disable(workArrangement);
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
	public void deleteByIds(WorkArrangement workArrangement) {
		if (workArrangement == null || workArrangement.getIds() == null || workArrangement.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(workArrangement);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<WorkArrangement> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<WorkArrangement> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<WorkArrangement> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
	
	@Transactional(readOnly = false)
	public void saveWorkArrangement(WorkArrangement workArrangement) {
		save(workArrangement);
		
		User opUser = UserUtils.getUser();
		WorkArrangementContent workArrangementContent = new WorkArrangementContent();
		workArrangementContent.setUser(opUser);
		List<WorkArrangementContent> arrangementContentList = workArrangementContentService.findList(workArrangementContent);
		if(arrangementContentList != null && arrangementContentList.size() != 0){
			
			workArrangementContent = arrangementContentList.get(0);
			if(StringUtils.isNotBlank(workArrangement.getOtherJob())){
				workArrangementContent.setOtherJob(workArrangement.getOtherJob());
				workArrangementContentService.save(workArrangementContent);
			}
		}else{
			workArrangementContent.setOtherJob(workArrangement.getOtherJob());
			workArrangementContentService.save(workArrangementContent);
		}
	}
	
}