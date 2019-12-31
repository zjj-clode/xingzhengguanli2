package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpend;

/**
 * 项目支出立项DAO接口
 * @author dcl
 * @version 2019-12-13
 */
@MyBatisDao
public interface EducationProjectExpendDao extends CrudDao<EducationProjectExpend> {
	long findCount(EducationProjectExpend educationProjectExpend);
	void batchInsert(List<EducationProjectExpend> educationProjectExpend);
	void batchUpdate(List<EducationProjectExpend> educationProjectExpend);
	void batchInsertUpdate(List<EducationProjectExpend> educationProjectExpend);
	void disable(EducationProjectExpend educationProjectExpend);
	void deleteByIds(EducationProjectExpend educationProjectExpend);
}