package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementSetting;

/**
 * 工作安排设置DAO接口
 * @author dcl
 * @version 2019-12-06
 */
@MyBatisDao
public interface WorkArrangementSettingDao extends CrudDao<WorkArrangementSetting> {
	long findCount(WorkArrangementSetting workArrangementSetting);
	void batchInsert(List<WorkArrangementSetting> workArrangementSetting);
	void batchUpdate(List<WorkArrangementSetting> workArrangementSetting);
	void batchInsertUpdate(List<WorkArrangementSetting> workArrangementSetting);
	void disable(WorkArrangementSetting workArrangementSetting);
	void deleteByIds(WorkArrangementSetting workArrangementSetting);
}