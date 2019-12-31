package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.WorkAttendance;

/**
 * 工作考勤DAO接口
 * @author dcl
 * @version 2019-12-07
 */
@MyBatisDao
public interface WorkAttendanceDao extends CrudDao<WorkAttendance> {
	long findCount(WorkAttendance workAttendance);
	void batchInsert(List<WorkAttendance> workAttendance);
	void batchUpdate(List<WorkAttendance> workAttendance);
	void batchInsertUpdate(List<WorkAttendance> workAttendance);
	void disable(WorkAttendance workAttendance);
	void deleteByIds(WorkAttendance workAttendance);
}