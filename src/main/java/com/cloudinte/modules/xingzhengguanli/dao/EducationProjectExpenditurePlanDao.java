package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;

import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpenditurePlan;
import com.cloudinte.modules.xingzhengguanli.entity.ExportExpenditurePlan;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 项目支出计划DAO接口
 * @author dcl
 * @version 2019-12-13
 */
@MyBatisDao
public interface EducationProjectExpenditurePlanDao extends CrudDao<EducationProjectExpenditurePlan> {
	long findCount(EducationProjectExpenditurePlan educationProjectExpenditurePlan);
	void batchInsert(List<EducationProjectExpenditurePlan> educationProjectExpenditurePlan);
	void batchUpdate(List<EducationProjectExpenditurePlan> educationProjectExpenditurePlan);
	void batchInsertUpdate(List<EducationProjectExpenditurePlan> educationProjectExpenditurePlan);
	void disable(EducationProjectExpenditurePlan educationProjectExpenditurePlan);
	void deleteByIds(EducationProjectExpenditurePlan educationProjectExpenditurePlan);
	List<ExportExpenditurePlan> findExpenditurePlanList(EducationProjectExpenditurePlan educationProjectExpenditurePlan);
}