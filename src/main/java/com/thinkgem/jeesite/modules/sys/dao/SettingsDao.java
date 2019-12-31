/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Settings;

/**
 * 运行参数DAO接口
 */
@MyBatisDao
public interface SettingsDao extends CrudDao<Settings> {

	long findCount(Settings settings);

	List<Settings> getByKey(Settings settings);

	void updateValue(Settings settings);

	void updateSystemDefined(Settings settings);
}
