package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectDescription;

/**
 * 项目描述DAO接口
 * @author dcl
 * @version 2019-12-14
 */
@MyBatisDao
public interface EducationProjectDescriptionDao extends CrudDao<EducationProjectDescription> {
	long findCount(EducationProjectDescription educationProjectDescription);
	void batchInsert(List<EducationProjectDescription> educationProjectDescription);
	void batchUpdate(List<EducationProjectDescription> educationProjectDescription);
	void batchInsertUpdate(List<EducationProjectDescription> educationProjectDescription);
	void disable(EducationProjectDescription educationProjectDescription);
	void deleteByIds(EducationProjectDescription educationProjectDescription);
}