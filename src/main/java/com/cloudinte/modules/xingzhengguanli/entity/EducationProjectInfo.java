package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 教育教学项目基本信息Entity
 * @author dcl
 * @version 2019-12-12
 */
public class EducationProjectInfo extends DataEntity<EducationProjectInfo> {
	
	private static final long serialVersionUID = 1L;
	private String belongToFirst;		// 所属一级项目
	private String subjectCode;		// 科目代码
	private String projectUnit;		// 项目单位
	private String projectCode;		// 项目代码
	private String projectName;		// 项目名称
	private String projectCategory;		// 项目类别
	private String attribute;		// 基建属性
	private String isTogether;		// 是否拼盘
	private String planYear;		// 计划开始执行年份
	private String projectCycle;		// 项目周期
	private String latestApprovalYear;		// 最新批复年份
	private String confidentialityLevel;		// 密级
	private String confidentialityTermUnit;		// 保密期限单位
	private String confidentialityTerm;		// 保密期限
	private String projectLeader;		// 项目负责人
	private String projectPost;		// 职务
	private String projectContactsPhone;		// 联系人电话
	private String isTransverse;		// 是否横向标识
	private String auditStatus;		// 财政审核状态
	private String isReport;		// 是否标识上报财政库
	private String departmentStatisticsId;		// 部门统计标识212
	
	
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
	 *  教育教学项目基本信息
	 */
	public EducationProjectInfo() {
		super();
	}

	/**
	 *  教育教学项目基本信息
	 * @param id
	 */
	public EducationProjectInfo(String id){
		super(id);
	}

	@Length(min=0, max=255, message="所属一级项目长度必须介于 0 和 255 之间")
	@ExcelField(title="所属一级项目", align=2, sort=1 )
	public String getBelongToFirst() {
		return belongToFirst;
	}

	public void setBelongToFirst(String belongToFirst) {
		this.belongToFirst = belongToFirst;
	}
	
	@Length(min=0, max=255, message="科目代码长度必须介于 0 和 255 之间")
	@ExcelField(title="科目代码", align=2, sort=2 )
	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	@Length(min=0, max=255, message="项目单位长度必须介于 0 和 255 之间")
	@ExcelField(title="项目单位", align=2, sort=3 )
	public String getProjectUnit() {
		return projectUnit;
	}

	public void setProjectUnit(String projectUnit) {
		this.projectUnit = projectUnit;
	}
	
	@Length(min=0, max=255, message="项目代码长度必须介于 0 和 255 之间")
	@ExcelField(title="项目代码", align=2, sort=4 )
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	@Length(min=0, max=255, message="项目名称长度必须介于 0 和 255 之间")
	@ExcelField(title="项目名称", align=2, sort=5 )
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Length(min=0, max=255, message="项目类别长度必须介于 0 和 255 之间")
	@ExcelField(title="项目类别", align=2, sort=6 )
	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	
	@Length(min=0, max=255, message="基建属性长度必须介于 0 和 255 之间")
	@ExcelField(title="基建属性", align=2, sort=7 )
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	@Length(min=0, max=255, message="是否拼盘长度必须介于 0 和 255 之间")
	@ExcelField(title="是否拼盘", align=2, sort=8 )
	public String getIsTogether() {
		return isTogether;
	}

	public void setIsTogether(String isTogether) {
		this.isTogether = isTogether;
	}
	
	@Length(min=0, max=255, message="计划开始执行年份长度必须介于 0 和 255 之间")
	@ExcelField(title="计划开始执行年份", align=2, sort=9 )
	public String getPlanYear() {
		return planYear;
	}

	public void setPlanYear(String planYear) {
		this.planYear = planYear;
	}
	
	@Length(min=0, max=255, message="项目周期长度必须介于 0 和 255 之间")
	@ExcelField(title="项目周期", align=2, sort=10 )
	public String getProjectCycle() {
		return projectCycle;
	}

	public void setProjectCycle(String projectCycle) {
		this.projectCycle = projectCycle;
	}
	
	@Length(min=0, max=255, message="最新批复年份长度必须介于 0 和 255 之间")
	@ExcelField(title="最新批复年份", align=2, sort=11 )
	public String getLatestApprovalYear() {
		return latestApprovalYear;
	}

	public void setLatestApprovalYear(String latestApprovalYear) {
		this.latestApprovalYear = latestApprovalYear;
	}
	
	@Length(min=0, max=255, message="密级长度必须介于 0 和 255 之间")
	@ExcelField(title="密级", align=2, sort=12 )
	public String getConfidentialityLevel() {
		return confidentialityLevel;
	}

	public void setConfidentialityLevel(String confidentialityLevel) {
		this.confidentialityLevel = confidentialityLevel;
	}
	
	@Length(min=0, max=255, message="保密期限单位长度必须介于 0 和 255 之间")
	@ExcelField(title="保密期限单位", align=2, sort=13 )
	public String getConfidentialityTermUnit() {
		return confidentialityTermUnit;
	}

	public void setConfidentialityTermUnit(String confidentialityTermUnit) {
		this.confidentialityTermUnit = confidentialityTermUnit;
	}
	
	@Length(min=0, max=255, message="保密期限长度必须介于 0 和 255 之间")
	@ExcelField(title="保密期限", align=2, sort=14 )
	public String getConfidentialityTerm() {
		return confidentialityTerm;
	}

	public void setConfidentialityTerm(String confidentialityTerm) {
		this.confidentialityTerm = confidentialityTerm;
	}
	
	@Length(min=0, max=255, message="项目负责人长度必须介于 0 和 255 之间")
	@ExcelField(title="项目负责人", align=2, sort=15 )
	public String getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}
	
	@Length(min=0, max=255, message="职务长度必须介于 0 和 255 之间")
	@ExcelField(title="职务", align=2, sort=16 )
	public String getProjectPost() {
		return projectPost;
	}

	public void setProjectPost(String projectPost) {
		this.projectPost = projectPost;
	}
	
	@Length(min=0, max=255, message="联系人电话长度必须介于 0 和 255 之间")
	@ExcelField(title="联系人电话", align=2, sort=17 )
	public String getProjectContactsPhone() {
		return projectContactsPhone;
	}

	public void setProjectContactsPhone(String projectContactsPhone) {
		this.projectContactsPhone = projectContactsPhone;
	}
	
	@Length(min=0, max=5, message="是否横向标识长度必须介于 0 和 5 之间")
	@ExcelField(title="是否横向标识", align=2, sort=18 )
	public String getIsTransverse() {
		return isTransverse;
	}

	public void setIsTransverse(String isTransverse) {
		this.isTransverse = isTransverse;
	}
	
	@Length(min=0, max=10, message="财政审核状态长度必须介于 0 和 10 之间")
	@ExcelField(title="财政审核状态", align=2, sort=19 )
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@Length(min=0, max=5, message="是否标识上报财政库长度必须介于 0 和 5 之间")
	@ExcelField(title="是否标识上报财政库", align=2, sort=20 )
	public String getIsReport() {
		return isReport;
	}

	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}
	
	@Length(min=0, max=255, message="部门统计标识212长度必须介于 0 和 255 之间")
	@ExcelField(title="部门统计标识212", align=2, sort=21 )
	public String getDepartmentStatisticsId() {
		return departmentStatisticsId;
	}

	public void setDepartmentStatisticsId(String departmentStatisticsId) {
		this.departmentStatisticsId = departmentStatisticsId;
	}
	
}