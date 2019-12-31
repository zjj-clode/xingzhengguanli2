package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.User;
/**
 * 例会通报工作事项Entity
 * @author dcl
 * @version 2019-12-11
 */
public class WorkRegularMeetingContent extends DataEntity<WorkRegularMeetingContent> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 被征集人
	private WorkRegularMeeting regularMeeting;		// 工作例会
	private String content;		// 例会通报工作事项
	
	
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
	 *  例会通报工作事项
	 */
	public WorkRegularMeetingContent() {
		super();
	}

	/**
	 *  例会通报工作事项
	 * @param id
	 */
	public WorkRegularMeetingContent(String id){
		super(id);
	}

	@ExcelField(title="被征集人", align=2, sort=1 )
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

	
	public WorkRegularMeeting getRegularMeeting() {
		return regularMeeting;
	}

	public void setRegularMeeting(WorkRegularMeeting regularMeeting) {
		this.regularMeeting = regularMeeting;
	}

	@ExcelField(title="例会通报工作事项", align=2, sort=3 )
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}