package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangement;

/**
 * 工作安排DAO接口
 * @author dcl
 * @version 2019-12-06
 */
@MyBatisDao
public interface WorkArrangementDao extends CrudDao<WorkArrangement> {
	long findCount(WorkArrangement workArrangement);
	void batchInsert(List<WorkArrangement> workArrangement);
	void batchUpdate(List<WorkArrangement> workArrangement);
	void batchInsertUpdate(List<WorkArrangement> workArrangement);
	void disable(WorkArrangement workArrangement);
	void deleteByIds(WorkArrangement workArrangement);
}