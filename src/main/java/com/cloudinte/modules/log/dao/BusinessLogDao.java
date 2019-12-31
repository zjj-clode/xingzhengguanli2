/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.modules.log.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloudinte.modules.log.entity.BusinessLog;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 业务日志dao
 */
@MyBatisDao
public interface BusinessLogDao extends CrudDao<BusinessLog> {

	public long findCount(BusinessLog log);

	public void deleteBatch(BusinessLog log);
	
	List<HashMap<String, Object>> getBusinessLog(@Param("date") String date,@Param("userId") String userId);

}