/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.SettingsDao;
import com.thinkgem.jeesite.modules.sys.entity.Settings;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 运行参数Service
 */
@Service
@Transactional(readOnly = true)
public class SettingsService extends CrudService<SettingsDao, Settings> {

	public long findCount(Settings settings) {
		return dao.findCount(settings);
	}

	@Override
	public Page<Settings> findPage(Page<Settings> page, Settings settings) {
		page.setCount(findCount(settings));
		return super.findPage(page, settings);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Settings settings) {
		Object value = SettingsUtils.convertSettingsValueByDataType(settings);
		//settings.setValue(value == null ? null : value.toString());
		if (value != null) {
			if (value instanceof Date) {
				value = DateUtils.formatDate((Date) value, "yyyy-MM-dd HH:mm:ss");
			}
		}
		super.save(settings);
		SettingsUtils.clearCache();
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Settings settings) {
		super.delete(settings);
		SettingsUtils.clearCache();
	}

	@Transactional(readOnly = false)
	public void updateValue(Settings settings) {
		settings.preUpdate();
		dao.updateValue(settings);
		SettingsUtils.clearCache();
	}

	public Settings getByKey(Settings settings) {
		if (settings == null || StringUtils.isBlank(settings.getKey())) {
			return null;
		}
		List<Settings> list = dao.getByKey(settings);
		if (Collections3.isEmpty(list)) {
			return null;
		}
		if (list.size() == 1) {
			return list.get(0);
		}
		throw new RuntimeException("发现重复的KEY");
	}

}
