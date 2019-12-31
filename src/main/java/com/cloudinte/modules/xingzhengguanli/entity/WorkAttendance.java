package com.cloudinte.modules.xingzhengguanli.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 工作考勤Entity
 * @author dcl
 * @version 2019-12-07
 */
public class WorkAttendance extends DataEntity<WorkAttendance> {
	
	private static final long serialVersionUID = 1L;
	private User overtimeUser;		// 加班人
	private String keshi;		// 科室
	private String jobNumber;		// 工号
	private String name;		// 姓名
	private Integer overtimeDays;		// 加班天数
	private Date yearMon;		// 月份  year_mon
	
	
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
	 *  工作考勤
	 */
	public WorkAttendance() {
		super();
	}

	/**
	 *  工作考勤
	 * @param id
	 */
	public WorkAttendance(String id){
		super(id);
	}

	//@ExcelField(title="加班人", align=2, sort=1 )
	public User getOvertimeUser() {
		return overtimeUser;
	}

	public void setOvertimeUser(User overtimeUser) {
		this.overtimeUser = overtimeUser;
	}
	
	@Length(min=0, max=255, message="科室长度必须介于 0 和 255 之间")
	@ExcelField(title="科室", align=2, sort=2 )
	public String getKeshi() {
		return keshi;
	}

	public void setKeshi(String keshi) {
		this.keshi = keshi;
	}
	
	@Length(min=0, max=255, message="工号长度必须介于 0 和 255 之间")
	@ExcelField(title="工号", align=2, sort=3 )
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Length(min=0, max=255, message="姓名长度必须介于 0 和 255 之间")
	@ExcelField(title="姓名", align=2, sort=4 )
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="加班天数", align=2, sort=5 )
	public Integer getOvertimeDays() {
		return overtimeDays;
	}

	public void setOvertimeDays(Integer overtimeDays) {
		this.overtimeDays = overtimeDays;
	}
	
	//@ExcelField(title="月份", align=2, sort=6 )
	public Date getYearMon() {
		return yearMon;
	}

	public void setYearMon(Date yearMon) {
		this.yearMon = yearMon;
	}
	
}