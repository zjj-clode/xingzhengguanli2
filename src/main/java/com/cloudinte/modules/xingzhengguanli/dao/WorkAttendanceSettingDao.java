package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.WorkAttendanceSetting;

/**
 * 加班天数设置DAO接口
 * @author dcl
 * @version 2019-12-12
 */
@MyBatisDao
public interface WorkAttendanceSettingDao extends CrudDao<WorkAttendanceSetting> {
	long findCount(WorkAttendanceSetting workAttendanceSetting);
	void batchInsert(List<WorkAttendanceSetting> workAttendanceSetting);
	void batchUpdate(List<WorkAttendanceSetting> workAttendanceSetting);
	void batchInsertUpdate(List<WorkAttendanceSetting> workAttendanceSetting);
	void disable(WorkAttendanceSetting workAttendanceSetting);
	void deleteByIds(WorkAttendanceSetting workAttendanceSetting);
}