package com.cloudinte.modules.xingzhengguanli.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 项目支出立项依据Entity
 * @author dcl
 * @version 2019-12-13
 */
public class EducationProjectExpendAccording extends DataEntity<EducationProjectExpendAccording> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 申报人
	private String projectName;		// 项目名称
	private String projectUnit;		// 项目单位
	private String projectCode;		// 主管单位及代码
	private String projectCategory;		// 项目类别
	private String projectStartYear;		// 项目开始年份
	private String projectCycle;		// 项目周期
	private String reformInEducation;		// 一、深入推进高校创新创业教育改革
	private String basicPosition;		// 二、巩固本科教学基础地位
	private String professionalStructure;		// 三、调整优化学科专业结构
	private String mechanism;		// 四、完善协同育人机制
	private String fuse;		// 五、着力推进信息技术与教育教学深度融合
	private String personnelTraining;		// 六、建立完善拔尖人才培养体制机制
	
	
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
	 *  项目支出立项依据
	 */
	public EducationProjectExpendAccording() {
		super();
	}

	/**
	 *  项目支出立项依据
	 * @param id
	 */
	public EducationProjectExpendAccording(String id){
		super(id);
	}

	@ExcelField(title="申报人", align=2, sort=1 )
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=255, message="项目名称长度必须介于 0 和 255 之间")
	@ExcelField(title="项目名称", align=2, sort=2 )
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Length(min=0, max=255, message="项目单位长度必须介于 0 和 255 之间")
	@ExcelField(title="项目单位", align=2, sort=3 )
	public String getProjectUnit() {
		return projectUnit;
	}

	public void setProjectUnit(String projectUnit) {
		this.projectUnit = projectUnit;
	}
	
	@Length(min=0, max=255, message="主管单位及代码长度必须介于 0 和 255 之间")
	@ExcelField(title="主管单位及代码", align=2, sort=4 )
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	@Length(min=0, max=255, message="项目类别长度必须介于 0 和 255 之间")
	@ExcelField(title="项目类别", align=2, sort=5 )
	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	
	@Length(min=0, max=255, message="项目开始年份长度必须介于 0 和 255 之间")
	@ExcelField(title="项目开始年份", align=2, sort=6 )
	public String getProjectStartYear() {
		return projectStartYear;
	}

	public void setProjectStartYear(String projectStartYear) {
		this.projectStartYear = projectStartYear;
	}
	
	@Length(min=0, max=255, message="项目周期长度必须介于 0 和 255 之间")
	@ExcelField(title="项目周期", align=2, sort=7 )
	public String getProjectCycle() {
		return projectCycle;
	}

	public void setProjectCycle(String projectCycle) {
		this.projectCycle = projectCycle;
	}
	
	@ExcelField(title="一、深入推进高校创新创业教育改革", align=2, sort=8 )
	public String getReformInEducation() {
		return reformInEducation;
	}

	public void setReformInEducation(String reformInEducation) {
		this.reformInEducation = reformInEducation;
	}
	
	@ExcelField(title="二、巩固本科教学基础地位", align=2, sort=9 )
	public String getBasicPosition() {
		return basicPosition;
	}

	public void setBasicPosition(String basicPosition) {
		this.basicPosition = basicPosition;
	}
	
	@ExcelField(title="三、调整优化学科专业结构", align=2, sort=10 )
	public String getProfessionalStructure() {
		return professionalStructure;
	}

	public void setProfessionalStructure(String professionalStructure) {
		this.professionalStructure = professionalStructure;
	}
	
	@ExcelField(title="四、完善协同育人机制", align=2, sort=11 )
	public String getMechanism() {
		return mechanism;
	}

	public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}
	
	@ExcelField(title="五、着力推进信息技术与教育教学深度融合", align=2, sort=12 )
	public String getFuse() {
		return fuse;
	}

	public void setFuse(String fuse) {
		this.fuse = fuse;
	}
	
	@ExcelField(title="六、建立完善拔尖人才培养体制机制", align=2, sort=13 )
	public String getPersonnelTraining() {
		return personnelTraining;
	}

	public void setPersonnelTraining(String personnelTraining) {
		this.personnelTraining = personnelTraining;
	}
	
}