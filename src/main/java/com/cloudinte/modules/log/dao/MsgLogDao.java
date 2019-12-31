/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.modules.log.dao;

import com.cloudinte.modules.log.entity.MsgLog;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface MsgLogDao extends CrudDao<MsgLog> {

	long findCount(MsgLog log);

	void deleteBatch(MsgLog log);
}