package com.cloudinte.modules.xingzhengguanli.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 固定值记录Entity
 * @author dcl
 * @version 2019-12-11
 */
public class WorkArrangementContent extends DataEntity<WorkArrangementContent> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 记录人
	private String otherJob;		// 常规工作
	
	
	//
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public void processEmptyArrayParam() {
		if (ids != null && ids.length < 1) {
			ids = null;
		}
	}
	
	
	/**
	 *  固定值记录
	 */
	public WorkArrangementContent() {
		super();
	}

	/**
	 *  固定值记录
	 * @param id
	 */
	public WorkArrangementContent(String id){
		super(id);
	}

	@ExcelField(title="记录人", align=2, sort=1 )
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="常规工作", align=2, sort=2 )
	public String getOtherJob() {
		return otherJob;
	}

	public void setOtherJob(String otherJob) {
		this.otherJob = otherJob;
	}
	
}