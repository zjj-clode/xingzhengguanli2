package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationPerformanceIndicators;

/**
 * 绩效指标信息DAO接口
 * @author dcl
 * @version 2019-12-16
 */
@MyBatisDao
public interface EducationPerformanceIndicatorsDao extends CrudDao<EducationPerformanceIndicators> {
	long findCount(EducationPerformanceIndicators educationPerformanceIndicators);
	void batchInsert(List<EducationPerformanceIndicators> educationPerformanceIndicators);
	void batchUpdate(List<EducationPerformanceIndicators> educationPerformanceIndicators);
	void batchInsertUpdate(List<EducationPerformanceIndicators> educationPerformanceIndicators);
	void disable(EducationPerformanceIndicators educationPerformanceIndicators);
	void deleteByIds(EducationPerformanceIndicators educationPerformanceIndicators);
}