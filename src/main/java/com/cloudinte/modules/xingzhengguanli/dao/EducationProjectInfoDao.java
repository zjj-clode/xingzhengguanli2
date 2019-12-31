package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectInfo;

/**
 * 教育教学项目基本信息DAO接口
 * @author dcl
 * @version 2019-12-12
 */
@MyBatisDao
public interface EducationProjectInfoDao extends CrudDao<EducationProjectInfo> {
	long findCount(EducationProjectInfo educationProjectInfo);
	void batchInsert(List<EducationProjectInfo> educationProjectInfo);
	void batchUpdate(List<EducationProjectInfo> educationProjectInfo);
	void batchInsertUpdate(List<EducationProjectInfo> educationProjectInfo);
	void disable(EducationProjectInfo educationProjectInfo);
	void deleteByIds(EducationProjectInfo educationProjectInfo);
}