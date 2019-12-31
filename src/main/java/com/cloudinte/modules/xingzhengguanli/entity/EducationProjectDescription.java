package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 项目描述Entity
 * @author dcl
 * @version 2019-12-14
 */
public class EducationProjectDescription extends DataEntity<EducationProjectDescription> {
	
	private static final long serialVersionUID = 1L;
	private String projectName;		// 项目活动
	private String projectDescription;		// 对项目活动的描述
	private String childProject;		// 子活动
	private String childProjectDescription;		// 对子活动的描述
	private String subItemExpenditure;		// 分项支出
	private String numberFrequency;		// 数量/频率
	private Integer sort;		// 序号
	
	
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
	 *  项目描述
	 */
	public EducationProjectDescription() {
		super();
	}

	/**
	 *  项目描述
	 * @param id
	 */
	public EducationProjectDescription(String id){
		super(id);
	}

	@Length(min=0, max=255, message="项目活动长度必须介于 0 和 255 之间")
	@ExcelField(title="项目活动", align=2, sort=1 )
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Length(min=0, max=500, message="对项目活动的描述长度必须介于 0 和 500 之间")
	@ExcelField(title="对项目活动的描述", align=2, sort=2 )
	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	
	@Length(min=0, max=255, message="子活动长度必须介于 0 和 255 之间")
	@ExcelField(title="子活动", align=2, sort=3 )
	public String getChildProject() {
		return childProject;
	}

	public void setChildProject(String childProject) {
		this.childProject = childProject;
	}
	
	@Length(min=0, max=500, message="对子活动的描述长度必须介于 0 和 500 之间")
	@ExcelField(title="对子活动的描述", align=2, sort=4 )
	public String getChildProjectDescription() {
		return childProjectDescription;
	}

	public void setChildProjectDescription(String childProjectDescription) {
		this.childProjectDescription = childProjectDescription;
	}
	
	@Length(min=0, max=64, message="分项支出长度必须介于 0 和 64 之间")
	@ExcelField(title="分项支出", align=2, sort=5 )
	public String getSubItemExpenditure() {
		return subItemExpenditure;
	}

	public void setSubItemExpenditure(String subItemExpenditure) {
		this.subItemExpenditure = subItemExpenditure;
	}
	
	@Length(min=0, max=64, message="数量/频率长度必须介于 0 和 64 之间")
	@ExcelField(title="数量/频率", align=2, sort=6 )
	public String getNumberFrequency() {
		return numberFrequency;
	}

	public void setNumberFrequency(String numberFrequency) {
		this.numberFrequency = numberFrequency;
	}
	
	@ExcelField(title="序号", align=2, sort=7 )
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}