package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeetingContent;

/**
 * 例会通报工作事项DAO接口
 * @author dcl
 * @version 2019-12-11
 */
@MyBatisDao
public interface WorkRegularMeetingContentDao extends CrudDao<WorkRegularMeetingContent> {
	long findCount(WorkRegularMeetingContent workRegularMeetingContent);
	void batchInsert(List<WorkRegularMeetingContent> workRegularMeetingContent);
	void batchUpdate(List<WorkRegularMeetingContent> workRegularMeetingContent);
	void batchInsertUpdate(List<WorkRegularMeetingContent> workRegularMeetingContent);
	void disable(WorkRegularMeetingContent workRegularMeetingContent);
	void deleteByIds(WorkRegularMeetingContent workRegularMeetingContent);
}