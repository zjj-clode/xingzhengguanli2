package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectPerformance;

/**
 * 项目支出绩效目标申报DAO接口
 * @author dcl
 * @version 2019-12-16
 */
@MyBatisDao
public interface EducationProjectPerformanceDao extends CrudDao<EducationProjectPerformance> {
	long findCount(EducationProjectPerformance educationProjectPerformance);
	void batchInsert(List<EducationProjectPerformance> educationProjectPerformance);
	void batchUpdate(List<EducationProjectPerformance> educationProjectPerformance);
	void batchInsertUpdate(List<EducationProjectPerformance> educationProjectPerformance);
	void disable(EducationProjectPerformance educationProjectPerformance);
	void deleteByIds(EducationProjectPerformance educationProjectPerformance);
}