package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 项目支出立项Entity
 * @author dcl
 * @version 2019-12-13
 */
public class EducationProjectExpend extends DataEntity<EducationProjectExpend> {
	
	private static final long serialVersionUID = 1L;
	private String projectName;		// 项目名称
	private String projectUnit;		// 项目单位
	private String projectCode;		// 主管单位及代码
	private String projectCategory;		// 项目类别
	private String projectStartYear;		// 项目开始年份
	private String projectCycle;		// 项目周期
	
	
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
	 *  项目支出立项
	 */
	public EducationProjectExpend() {
		super();
	}

	/**
	 *  项目支出立项
	 * @param id
	 */
	public EducationProjectExpend(String id){
		super(id);
	}

	@Length(min=0, max=255, message="项目名称长度必须介于 0 和 255 之间")
	@ExcelField(title="项目名称", align=2, sort=1 )
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Length(min=0, max=255, message="项目单位长度必须介于 0 和 255 之间")
	@ExcelField(title="项目单位", align=2, sort=2 )
	public String getProjectUnit() {
		return projectUnit;
	}

	public void setProjectUnit(String projectUnit) {
		this.projectUnit = projectUnit;
	}
	
	@Length(min=0, max=255, message="主管单位及代码长度必须介于 0 和 255 之间")
	@ExcelField(title="主管单位及代码", align=2, sort=3 )
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	@Length(min=0, max=255, message="项目类别长度必须介于 0 和 255 之间")
	@ExcelField(title="项目类别", align=2, sort=4 )
	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	
	@Length(min=0, max=255, message="项目开始年份长度必须介于 0 和 255 之间")
	@ExcelField(title="项目开始年份", align=2, sort=5 )
	public String getProjectStartYear() {
		return projectStartYear;
	}

	public void setProjectStartYear(String projectStartYear) {
		this.projectStartYear = projectStartYear;
	}
	
	@Length(min=0, max=255, message="项目周期长度必须介于 0 和 255 之间")
	@ExcelField(title="项目周期", align=2, sort=6 )
	public String getProjectCycle() {
		return projectCycle;
	}

	public void setProjectCycle(String projectCycle) {
		this.projectCycle = projectCycle;
	}
	
}