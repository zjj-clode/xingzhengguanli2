package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeeting;

/**
 * 工作例会征集DAO接口
 * @author dcl
 * @version 2019-12-11
 */
@MyBatisDao
public interface WorkRegularMeetingDao extends CrudDao<WorkRegularMeeting> {
	long findCount(WorkRegularMeeting workRegularMeeting);
	void batchInsert(List<WorkRegularMeeting> workRegularMeeting);
	void batchUpdate(List<WorkRegularMeeting> workRegularMeeting);
	void batchInsertUpdate(List<WorkRegularMeeting> workRegularMeeting);
	void disable(WorkRegularMeeting workRegularMeeting);
	void deleteByIds(WorkRegularMeeting workRegularMeeting);
}