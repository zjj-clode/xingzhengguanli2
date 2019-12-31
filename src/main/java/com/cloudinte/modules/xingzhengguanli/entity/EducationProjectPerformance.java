package com.cloudinte.modules.xingzhengguanli.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 项目支出绩效目标申报Entity
 * @author dcl
 * @version 2019-12-16
 */
public class EducationProjectPerformance extends DataEntity<EducationProjectPerformance> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 申报人id
	private String year;		// 年份
	private String projectName;		// 项目名称
	private String projectCode;		// 主管部门及代码
	private String projectUnit;		// 实施单位
	private String projectCategory;		// 项目属性
	private String projectCycle;		// 项目周期
	private Double metaphaseMoney;		// 中期资金总额
	private Double metaphaseOtherMoney;		// 中期其他资金
	private Double metaphaseFinanceMoney;		// 中期财政拨款
	private Double shortTermMoney;		// 年度资金总额
	private Double shortTermOtherMoney;		// 年度其他资金
	private Double shortTermFinanceMoney;		// 年度财政拨款
	private String shortTermFirstTarget;		// 一级指标
	private String shortTermFirstTargetName;		// 一级指标名称
	private String metaphaseContent;		// 中期目标
	private String shortTermContent;		// 年度目标
	private String shortTermTargetType;		// 短期二级指标类型
	private String shortTermSecondTargetName;		// 短期二级指标名称
	private String shortTermThirdTargetName;		// 短期三级指标
	private String shortTermTargetValue;		// 短期三级指标
	private String metaphaseTargetType;		// 中期二级指标类型
	private String metaphaseSecondTargetName;		// 中期二级指标名称
	private String metaphaseThirdTargetName;		// 中期三级指标
	private String metaphaseTargetValue;		// 中期三级指标
	private String quantityUnit;		// 单位
	
	
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
	 *  项目支出绩效目标申报
	 */
	public EducationProjectPerformance() {
		super();
	}

	/**
	 *  项目支出绩效目标申报
	 * @param id
	 */
	public EducationProjectPerformance(String id){
		super(id);
	}

	@NotNull(message="申报人id不能为空")
	@ExcelField(title="申报人id", align=2, sort=1 )
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=255, message="年份长度必须介于 0 和 255 之间")
	@ExcelField(title="年份", align=2, sort=2 )
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@Length(min=0, max=255, message="项目名称长度必须介于 0 和 255 之间")
	@ExcelField(title="项目名称", align=2, sort=3 )
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Length(min=0, max=255, message="主管部门及代码长度必须介于 0 和 255 之间")
	@ExcelField(title="主管部门及代码", align=2, sort=4 )
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	@Length(min=0, max=255, message="实施单位长度必须介于 0 和 255 之间")
	@ExcelField(title="实施单位", align=2, sort=5 )
	public String getProjectUnit() {
		return projectUnit;
	}

	public void setProjectUnit(String projectUnit) {
		this.projectUnit = projectUnit;
	}
	
	@Length(min=0, max=255, message="项目属性长度必须介于 0 和 255 之间")
	@ExcelField(title="项目属性", align=2, sort=6 )
	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	
	@Length(min=0, max=255, message="项目周期长度必须介于 0 和 255 之间")
	@ExcelField(title="项目周期", align=2, sort=7 )
	public String getProjectCycle() {
		return projectCycle;
	}

	public void setProjectCycle(String projectCycle) {
		this.projectCycle = projectCycle;
	}
	
	@ExcelField(title="中期资金总额", align=2, sort=8 )
	public Double getMetaphaseMoney() {
		return metaphaseMoney;
	}

	public void setMetaphaseMoney(Double metaphaseMoney) {
		this.metaphaseMoney = metaphaseMoney;
	}
	
	@ExcelField(title="中期其他资金", align=2, sort=9 )
	public Double getMetaphaseOtherMoney() {
		return metaphaseOtherMoney;
	}

	public void setMetaphaseOtherMoney(Double metaphaseOtherMoney) {
		this.metaphaseOtherMoney = metaphaseOtherMoney;
	}
	
	@ExcelField(title="中期财政拨款", align=2, sort=10 )
	public Double getMetaphaseFinanceMoney() {
		return metaphaseFinanceMoney;
	}

	public void setMetaphaseFinanceMoney(Double metaphaseFinanceMoney) {
		this.metaphaseFinanceMoney = metaphaseFinanceMoney;
	}
	
	@ExcelField(title="年度资金总额", align=2, sort=11 )
	public Double getShortTermMoney() {
		return shortTermMoney;
	}

	public void setShortTermMoney(Double shortTermMoney) {
		this.shortTermMoney = shortTermMoney;
	}
	
	@ExcelField(title="年度其他资金", align=2, sort=12 )
	public Double getShortTermOtherMoney() {
		return shortTermOtherMoney;
	}

	public void setShortTermOtherMoney(Double shortTermOtherMoney) {
		this.shortTermOtherMoney = shortTermOtherMoney;
	}
	
	@ExcelField(title="年度财政拨款", align=2, sort=13 )
	public Double getShortTermFinanceMoney() {
		return shortTermFinanceMoney;
	}

	public void setShortTermFinanceMoney(Double shortTermFinanceMoney) {
		this.shortTermFinanceMoney = shortTermFinanceMoney;
	}
	
	@Length(min=0, max=3, message="一级指标长度必须介于 0 和 3 之间")
	@ExcelField(title="一级指标", align=2, sort=14 ,dictType="first_target")
	public String getShortTermFirstTarget() {
		return shortTermFirstTarget;
	}

	public void setShortTermFirstTarget(String shortTermFirstTarget) {
		this.shortTermFirstTarget = shortTermFirstTarget;
	}
	
	@Length(min=0, max=64, message="一级指标名称长度必须介于 0 和 64 之间")
	@ExcelField(title="一级指标名称", align=2, sort=15 )
	public String getShortTermFirstTargetName() {
		return shortTermFirstTargetName;
	}

	public void setShortTermFirstTargetName(String shortTermFirstTargetName) {
		this.shortTermFirstTargetName = shortTermFirstTargetName;
	}
	
	@ExcelField(title="中期目标", align=2, sort=16 )
	public String getMetaphaseContent() {
		return metaphaseContent;
	}

	public void setMetaphaseContent(String metaphaseContent) {
		this.metaphaseContent = metaphaseContent;
	}
	
	@ExcelField(title="年度目标", align=2, sort=17 )
	public String getShortTermContent() {
		return shortTermContent;
	}

	public void setShortTermContent(String shortTermContent) {
		this.shortTermContent = shortTermContent;
	}
	
	@Length(min=0, max=3, message="短期二级指标类型长度必须介于 0 和 3 之间")
	@ExcelField(title="短期二级指标类型", align=2, sort=18 ,dictType="target_type")
	public String getShortTermTargetType() {
		return shortTermTargetType;
	}

	public void setShortTermTargetType(String shortTermTargetType) {
		this.shortTermTargetType = shortTermTargetType;
	}
	
	@Length(min=0, max=64, message="短期二级指标名称长度必须介于 0 和 64 之间")
	@ExcelField(title="短期二级指标名称", align=2, sort=19 )
	public String getShortTermSecondTargetName() {
		return shortTermSecondTargetName;
	}

	public void setShortTermSecondTargetName(String shortTermSecondTargetName) {
		this.shortTermSecondTargetName = shortTermSecondTargetName;
	}
	
	@Length(min=0, max=255, message="短期三级指标长度必须介于 0 和 255 之间")
	@ExcelField(title="短期三级指标", align=2, sort=20 )
	public String getShortTermThirdTargetName() {
		return shortTermThirdTargetName;
	}

	public void setShortTermThirdTargetName(String shortTermThirdTargetName) {
		this.shortTermThirdTargetName = shortTermThirdTargetName;
	}
	
	@Length(min=0, max=255, message="短期三级指标长度必须介于 0 和 255 之间")
	@ExcelField(title="短期三级指标", align=2, sort=21 )
	public String getShortTermTargetValue() {
		return shortTermTargetValue;
	}

	public void setShortTermTargetValue(String shortTermTargetValue) {
		this.shortTermTargetValue = shortTermTargetValue;
	}
	
	@Length(min=0, max=3, message="中期二级指标类型长度必须介于 0 和 3 之间")
	@ExcelField(title="中期二级指标类型", align=2, sort=22 ,dictType="target_type")
	public String getMetaphaseTargetType() {
		return metaphaseTargetType;
	}

	public void setMetaphaseTargetType(String metaphaseTargetType) {
		this.metaphaseTargetType = metaphaseTargetType;
	}
	
	@Length(min=0, max=64, message="中期二级指标名称长度必须介于 0 和 64 之间")
	@ExcelField(title="中期二级指标名称", align=2, sort=23 )
	public String getMetaphaseSecondTargetName() {
		return metaphaseSecondTargetName;
	}

	public void setMetaphaseSecondTargetName(String metaphaseSecondTargetName) {
		this.metaphaseSecondTargetName = metaphaseSecondTargetName;
	}
	
	@Length(min=0, max=255, message="中期三级指标长度必须介于 0 和 255 之间")
	@ExcelField(title="中期三级指标", align=2, sort=24 )
	public String getMetaphaseThirdTargetName() {
		return metaphaseThirdTargetName;
	}

	public void setMetaphaseThirdTargetName(String metaphaseThirdTargetName) {
		this.metaphaseThirdTargetName = metaphaseThirdTargetName;
	}
	
	@Length(min=0, max=255, message="中期三级指标长度必须介于 0 和 255 之间")
	@ExcelField(title="中期三级指标", align=2, sort=25 )
	public String getMetaphaseTargetValue() {
		return metaphaseTargetValue;
	}

	public void setMetaphaseTargetValue(String metaphaseTargetValue) {
		this.metaphaseTargetValue = metaphaseTargetValue;
	}
	
	@Length(min=0, max=255, message="单位长度必须介于 0 和 255 之间")
	@ExcelField(title="单位", align=2, sort=26 ,dictType="quantity_unit")
	public String getQuantityUnit() {
		return quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}
	
}