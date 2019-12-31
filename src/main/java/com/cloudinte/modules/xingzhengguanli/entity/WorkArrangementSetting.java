package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 工作安排设置Entity
 * @author dcl
 * @version 2019-12-06
 */
public class WorkArrangementSetting extends DataEntity<WorkArrangementSetting> {
	
	private static final long serialVersionUID = 1L;
	private String department;		// 部门
	private Date startDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String title;		// 标题
	
	
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
	 *  工作安排设置
	 */
	public WorkArrangementSetting() {
		super();
	}

	/**
	 *  工作安排设置
	 * @param id
	 */
	public WorkArrangementSetting(String id){
		super(id);
	}

	@Length(min=0, max=255, message="部门长度必须介于 0 和 255 之间")
	@ExcelField(title="部门", align=2, sort=1 )
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=2 )
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=3 )
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Length(min=0, max=255, message="标题长度必须介于 0 和 255 之间")
	@ExcelField(title="标题", align=2, sort=10 )
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}