package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.BjProject;

/**
 * 北京市共建项目DAO接口
 * @author dcl
 * @version 2019-12-12
 */
@MyBatisDao
public interface BjProjectDao extends CrudDao<BjProject> {
	long findCount(BjProject bjProject);
	void batchInsert(List<BjProject> bjProject);
	void batchUpdate(List<BjProject> bjProject);
	void batchInsertUpdate(List<BjProject> bjProject);
	void disable(BjProject bjProject);
	void deleteByIds(BjProject bjProject);
}