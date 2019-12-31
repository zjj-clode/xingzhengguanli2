/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.modules.log.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.cloudinte.modules.log.dao.MsgLogDao;
import com.cloudinte.modules.log.entity.MsgLog;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

@Service
@Transactional(readOnly = true)
public class MsgLogService extends CrudService<MsgLogDao, MsgLog> {

	@Transactional(readOnly = false)
	@Override
	public void save(MsgLog entity) {
		logger.debug(entity.toString());
		if (Global.logEnable()) {
			super.save(entity);
		}
	}

	public long findCount(MsgLog log) {
		return dao.findCount(log);
	}

	@Override
	public Page<MsgLog> findPage(Page<MsgLog> page, MsgLog log) {
		page.setCount(dao.findCount(log));
		return super.findPage(page, log);
	}

	@Transactional(readOnly = false)
	public void deleteBatch(MsgLog log) {
		String[] ids = log.getIds();
		if (!ObjectUtils.isEmpty(ids)) {
			dao.deleteBatch(log);
		}
	}
}