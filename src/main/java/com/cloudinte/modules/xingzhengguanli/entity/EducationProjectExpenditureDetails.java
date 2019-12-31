package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 项目支出明细Entity
 * @author dcl
 * @version 2019-12-13
 */
public class EducationProjectExpenditureDetails extends DataEntity<EducationProjectExpenditureDetails> {
	
	private static final long serialVersionUID = 1L;
	private String expenditureClassification;		// 部门预算支出经济分类科目
	private String governmentClassification;		// 对应的政府预算支出经济分类科目
	private Integer sort;		// 排序
	
	
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
	 *  项目支出明细
	 */
	public EducationProjectExpenditureDetails() {
		super();
	}

	/**
	 *  项目支出明细
	 * @param id
	 */
	public EducationProjectExpenditureDetails(String id){
		super(id);
	}

	@Length(min=0, max=64, message="部门预算支出经济分类科目长度必须介于 0 和 64 之间")
	@ExcelField(title="部门预算支出经济分类科目", align=2, sort=1 )
	public String getExpenditureClassification() {
		return expenditureClassification;
	}

	public void setExpenditureClassification(String expenditureClassification) {
		this.expenditureClassification = expenditureClassification;
	}
	
	@Length(min=0, max=64, message="对应的政府预算支出经济分类科目长度必须介于 0 和 64 之间")
	@ExcelField(title="对应的政府预算支出经济分类科目", align=2, sort=2 )
	public String getGovernmentClassification() {
		return governmentClassification;
	}

	public void setGovernmentClassification(String governmentClassification) {
		this.governmentClassification = governmentClassification;
	}
	
	@ExcelField(title="排序", align=2, sort=3 )
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}