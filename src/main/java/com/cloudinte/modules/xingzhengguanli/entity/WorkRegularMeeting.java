package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 工作例会征集Entity
 * @author dcl
 * @version 2019-12-11
 */
public class WorkRegularMeeting extends DataEntity<WorkRegularMeeting> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 例会标题
	private Date meetingDate;		// 时间
	private String meetingPlace;		// 地点
	private String meetingDepartment;		// 部门
	private Date beginMeetingDate;		// 开始 时间
	private Date endMeetingDate;		// 结束 时间
	
	private String meetingTime;
	
	List<WorkRegularMeetingContent> contentList = Lists.newArrayList();
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
	 *  工作例会征集
	 */
	public WorkRegularMeeting() {
		super();
	}

	/**
	 *  工作例会征集
	 * @param id
	 */
	public WorkRegularMeeting(String id){
		super(id);
	}

	@Length(min=0, max=255, message="例会标题长度必须介于 0 和 255 之间")
	@ExcelField(title="例会标题", align=2, sort=1 )
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="时间", align=2, sort=2 )
	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}
	
	@Length(min=0, max=255, message="地点长度必须介于 0 和 255 之间")
	@ExcelField(title="地点", align=2, sort=3 )
	public String getMeetingPlace() {
		return meetingPlace;
	}

	public void setMeetingPlace(String meetingPlace) {
		this.meetingPlace = meetingPlace;
	}
	
	@Length(min=0, max=255, message="部门长度必须介于 0 和 255 之间")
	@ExcelField(title="部门", align=2, sort=4 )
	public String getMeetingDepartment() {
		return meetingDepartment;
	}

	public void setMeetingDepartment(String meetingDepartment) {
		this.meetingDepartment = meetingDepartment;
	}
	
	public Date getBeginMeetingDate() {
		return beginMeetingDate;
	}

	public void setBeginMeetingDate(Date beginMeetingDate) {
		this.beginMeetingDate = beginMeetingDate;
	}
	
	public Date getEndMeetingDate() {
		return endMeetingDate;
	}

	public void setEndMeetingDate(Date endMeetingDate) {
		this.endMeetingDate = endMeetingDate;
	}

	public String getMeetingTime() {
		return meetingTime;
	}

	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
	}

	public List<WorkRegularMeetingContent> getContentList() {
		return contentList;
	}

	public void setContentList(List<WorkRegularMeetingContent> contentList) {
		this.contentList = contentList;
	}
		
}