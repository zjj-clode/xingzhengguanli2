/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.modules.log.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.cloudinte.modules.log.dao.BusinessLogDao;
import com.cloudinte.modules.log.entity.BusinessLog;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 业务日志Service
 */
@Service
@Transactional(readOnly = true)
public class BusinessLogService extends CrudService<BusinessLogDao, BusinessLog> {

	/**
	 * 读取jeesite.properties配置文件中的akmi.log.save值，决定是否保存业务操作日志。
	 */
	@Override
	public void save(BusinessLog entity) {
		logger.debug(entity.toString());
		if (Global.logEnable()) {
			super.save(entity);
		}
	}

	public long findCount(BusinessLog log) {
		return dao.findCount(log);
	}

	@Override
	public Page<BusinessLog> findPage(Page<BusinessLog> page, BusinessLog log) {
		page.setCount(dao.findCount(log));
		return super.findPage(page, log);
	}

	@Transactional(readOnly = false)
	public void deleteBatch(BusinessLog log) {
		String[] ids = log.getIds();
		if (!ObjectUtils.isEmpty(ids)) {
			dao.deleteBatch(log);
		}
	}
	
	/**
	 * 首页的最新操作记录
	 */
	public List<HashMap<String, Object>> getBusinessLog(String date,String userId){
		return dao.getBusinessLog(date,userId);
	}
}