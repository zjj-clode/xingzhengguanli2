package com.cloudinte.modules.xingzhengguanli.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

public class ExportExpenditurePlan  extends DataEntity<ExportExpenditurePlan>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  sort;
	private String expenditureYear;
	private String expenditureClassification;
	private Double allMoney;
	private String governmentClassification;
	
	@ExcelField(title="序号", align=2, sort=10 )
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@ExcelField(title="年份", align=2, sort=20 )
	public String getExpenditureYear() {
		
		return expenditureYear;
	}
	public void setExpenditureYear(String expenditureYear) {
		this.expenditureYear = expenditureYear;
	}
	
	@ExcelField(title="部门预算支出经济分类科目", align=2, sort=30 )
	public String getExpenditureClassification() {
		return expenditureClassification;
	}
	public void setExpenditureClassification(String expenditureClassification) {
		this.expenditureClassification = expenditureClassification;
	}
	
	@ExcelField(title="金额", align=2, sort=40 )
	public Double getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(Double allMoney) {
		this.allMoney = allMoney;
	}
	
	@ExcelField(title="对应的政府预算支出经济分类科目", align=2, sort=50 )
	public String getGovernmentClassification() {
		return governmentClassification;
	}
	public void setGovernmentClassification(String governmentClassification) {
		this.governmentClassification = governmentClassification;
	}
	
	
	

}
