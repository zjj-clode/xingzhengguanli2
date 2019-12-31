package com.cloudinte.modules.xingzhengguanli.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 项目支出计划Entity
 * @author dcl
 * @version 2019-12-13
 */
public class EducationProjectExpenditurePlan extends DataEntity<EducationProjectExpenditurePlan> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 申报人
	private EducationProjectExpenditureDetails details;		// 明细id
	private Double expenditureMoney;		// 金额
	private String expenditureYear;		// 年份
	
	
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
	 *  项目支出计划
	 */
	public EducationProjectExpenditurePlan() {
		super();
	}

	/**
	 *  项目支出计划
	 * @param id
	 */
	public EducationProjectExpenditurePlan(String id){
		super(id);
	}

	@NotNull(message="申报人不能为空")
	@ExcelField(title="申报人", align=2, sort=1 )
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

	public EducationProjectExpenditureDetails getDetails() {
		return details;
	}

	public void setDetails(EducationProjectExpenditureDetails details) {
		this.details = details;
	}

	@ExcelField(title="金额", align=2, sort=3 )
	public Double getExpenditureMoney() {
		return expenditureMoney;
	}

	public void setExpenditureMoney(Double expenditureMoney) {
		this.expenditureMoney = expenditureMoney;
	}
	
	@Length(min=0, max=64, message="年份长度必须介于 0 和 64 之间")
	@ExcelField(title="年份", align=2, sort=4 )
	public String getExpenditureYear() {
		return expenditureYear;
	}

	public void setExpenditureYear(String expenditureYear) {
		this.expenditureYear = expenditureYear;
	}
	
}