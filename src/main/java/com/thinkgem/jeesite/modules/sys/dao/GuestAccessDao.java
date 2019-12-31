/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.GuestAccess;

/**
 * 访问信息记录模块DAO接口
 * @author gyl
 * @version 2019-03-09
 */
@MyBatisDao
public interface GuestAccessDao extends CrudDao<GuestAccess> {

	long findCount(GuestAccess guestAccess);

	void logout(GuestAccess guestAccess);

	void logoutAll(GuestAccess guestAccess);

	List<HashMap<String, Object>> countMonthByDay(GuestAccess guestAccess);

	List<HashMap<String, Object>> countYearByMonth(GuestAccess guestAccess);

	List<HashMap<String, Object>> countByLoginname(GuestAccess guestAccess);
	
	List<HashMap<String, Object>> countForHighcharts(@Param("beginDay") String beginDay,@Param("endDay") String endDay);

	List<HashMap<String, Object>> countForStudentHighcharts(@Param("beginDay") String beginDay,@Param("endDay") String endDay);
	
	long countGuestAccess(GuestAccess guestAccess);

	long countViewGuestAccess(GuestAccess guestAccess);
	
	void batchInsert(List<GuestAccess> guestAccess);
	void batchUpdate(List<GuestAccess> guestAccess);
	void batchInsertUpdate(List<GuestAccess> guestAccess);
	void disable(GuestAccess guestAccess);
	void deleteByIds(GuestAccess guestAccess);
}