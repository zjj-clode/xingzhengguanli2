package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementContent;

/**
 * 固定值记录DAO接口
 * @author dcl
 * @version 2019-12-11
 */
@MyBatisDao
public interface WorkArrangementContentDao extends CrudDao<WorkArrangementContent> {
	long findCount(WorkArrangementContent workArrangementContent);
	void batchInsert(List<WorkArrangementContent> workArrangementContent);
	void batchUpdate(List<WorkArrangementContent> workArrangementContent);
	void batchInsertUpdate(List<WorkArrangementContent> workArrangementContent);
	void disable(WorkArrangementContent workArrangementContent);
	void deleteByIds(WorkArrangementContent workArrangementContent);
}