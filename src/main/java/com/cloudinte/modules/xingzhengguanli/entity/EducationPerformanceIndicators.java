package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 绩效指标信息Entity
 * @author dcl
 * @version 2019-12-16
 */
public class EducationPerformanceIndicators extends DataEntity<EducationPerformanceIndicators> {
	
	private static final long serialVersionUID = 1L;
	private String firstTarget;		// 一级指标
	private String firstTargetName;		// 一级指标名称
	private String targetType;		// 二级指标类型
	private String secondTargetName;		// 二级指标名称
	private String thirdTargetName;		// 三级指标
	
	private Integer sort;
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
	 *  绩效指标信息
	 */
	public EducationPerformanceIndicators() {
		super();
	}

	/**
	 *  绩效指标信息
	 * @param id
	 */
	public EducationPerformanceIndicators(String id){
		super(id);
	}

	@Length(min=1, max=3, message="一级指标长度必须介于 1 和 3 之间")
	//@ExcelField(title="一级指标", align=2, sort=1 ,dictType="first_target")
	public String getFirstTarget() {
		return firstTarget;
	}

	public void setFirstTarget(String firstTarget) {
		this.firstTarget = firstTarget;
	}
	
	@Length(min=1, max=64, message="一级指标名称长度必须介于 1 和 64 之间")
	@ExcelField(title="一级指标名称", align=2, sort=2 )
	public String getFirstTargetName() {
		return firstTargetName;
	}

	public void setFirstTargetName(String firstTargetName) {
		this.firstTargetName = firstTargetName;
	}
	
	@Length(min=1, max=3, message="二级指标类型长度必须介于 1 和 3 之间")
//	@ExcelField(title="二级指标类型", align=2, sort=3 ,dictType="target_type")
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
	@Length(min=1, max=64, message="二级指标名称长度必须介于 1 和 64 之间")
	@ExcelField(title="二级指标名称", align=2, sort=4 )
	public String getSecondTargetName() {
		return secondTargetName;
	}

	public void setSecondTargetName(String secondTargetName) {
		this.secondTargetName = secondTargetName;
	}
	
	@Length(min=1, max=255, message="三级指标长度必须介于 1 和 255 之间")
	@ExcelField(title="三级指标", align=2, sort=5 )
	public String getThirdTargetName() {
		return thirdTargetName;
	}

	public void setThirdTargetName(String thirdTargetName) {
		this.thirdTargetName = thirdTargetName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}