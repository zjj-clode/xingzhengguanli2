package com.cloudinte.modules.xingzhengguanli.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 加班天数设置Entity
 * @author dcl
 * @version 2019-12-12
 */
public class WorkAttendanceSetting extends DataEntity<WorkAttendanceSetting> {
	
	private static final long serialVersionUID = 1L;
	private Integer overtimeDays;		// 加班天数
	private Date yearMon;		// 月份
	private Date beginYearMon;		// 开始 月份
	private Date endYearMon;		// 结束 月份
	
	
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
	 *  加班天数设置
	 */
	public WorkAttendanceSetting() {
		super();
	}

	/**
	 *  加班天数设置
	 * @param id
	 */
	public WorkAttendanceSetting(String id){
		super(id);
	}

	@ExcelField(title="加班天数", align=2, sort=1 )
	public Integer getOvertimeDays() {
		return overtimeDays;
	}

	public void setOvertimeDays(Integer overtimeDays) {
		this.overtimeDays = overtimeDays;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="月份", align=2, sort=2 )
	public Date getYearMon() {
		return yearMon;
	}

	public void setYearMon(Date yearMon) {
		this.yearMon = yearMon;
	}
	
	public Date getBeginYearMon() {
		return beginYearMon;
	}

	public void setBeginYearMon(Date beginYearMon) {
		this.beginYearMon = beginYearMon;
	}
	
	public Date getEndYearMon() {
		return endYearMon;
	}

	public void setEndYearMon(Date endYearMon) {
		this.endYearMon = endYearMon;
	}
		
}