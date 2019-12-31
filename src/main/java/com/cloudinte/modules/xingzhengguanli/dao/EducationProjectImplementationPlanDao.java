package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectImplementationPlan;

/**
 * 项目支出实施方案DAO接口
 * @author dcl
 * @version 2019-12-13
 */
@MyBatisDao
public interface EducationProjectImplementationPlanDao extends CrudDao<EducationProjectImplementationPlan> {
	long findCount(EducationProjectImplementationPlan educationProjectImplementationPlan);
	void batchInsert(List<EducationProjectImplementationPlan> educationProjectImplementationPlan);
	void batchUpdate(List<EducationProjectImplementationPlan> educationProjectImplementationPlan);
	void batchInsertUpdate(List<EducationProjectImplementationPlan> educationProjectImplementationPlan);
	void disable(EducationProjectImplementationPlan educationProjectImplementationPlan);
	void deleteByIds(EducationProjectImplementationPlan educationProjectImplementationPlan);
}