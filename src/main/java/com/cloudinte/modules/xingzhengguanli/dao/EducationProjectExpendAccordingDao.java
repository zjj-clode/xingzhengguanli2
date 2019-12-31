package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpendAccording;

/**
 * 项目支出立项依据DAO接口
 * @author dcl
 * @version 2019-12-13
 */
@MyBatisDao
public interface EducationProjectExpendAccordingDao extends CrudDao<EducationProjectExpendAccording> {
	long findCount(EducationProjectExpendAccording educationProjectExpendAccording);
	void batchInsert(List<EducationProjectExpendAccording> educationProjectExpendAccording);
	void batchUpdate(List<EducationProjectExpendAccording> educationProjectExpendAccording);
	void batchInsertUpdate(List<EducationProjectExpendAccording> educationProjectExpendAccording);
	void disable(EducationProjectExpendAccording educationProjectExpendAccording);
	void deleteByIds(EducationProjectExpendAccording educationProjectExpendAccording);
}