/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.SiteAccess;

@MyBatisDao
public interface SiteAccessDao extends CrudDao<SiteAccess> {

	long findCount(SiteAccess siteAccess);

	void logout(SiteAccess siteAccess);

	void logoutAll(SiteAccess siteAccess);

	List<HashMap<String, Object>> countMonthByDay(SiteAccess siteAccess);

	List<HashMap<String, Object>> countYearByMonth(SiteAccess siteAccess);

	List<HashMap<String, Object>> countByLoginname(SiteAccess siteAccess);
	
	List<HashMap<String, Object>> countForHighcharts(@Param("beginDay") String beginDay,@Param("endDay") String endDay);

	List<HashMap<String, Object>> countForStudentHighcharts(@Param("beginDay") String beginDay,@Param("endDay") String endDay);
	
	long countSiteAccess(SiteAccess siteAccess);

	long countViewSiteAccess(SiteAccess siteAccess);
}