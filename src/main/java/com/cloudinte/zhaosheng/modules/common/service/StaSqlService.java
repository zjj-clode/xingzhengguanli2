package com.cloudinte.zhaosheng.modules.common.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.zhaosheng.modules.common.dao.StaSqlDao;
import com.cloudinte.zhaosheng.modules.common.entity.StaSql;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.Collections3;


/**
 * 统计模板Service
 * 
 * @author hhw
 * @version 2018-09-06
*/
@Service
@Transactional(readOnly = true)
public class StaSqlService extends CrudService<StaSqlDao, StaSql> {
	
	public Page<StaSql> findPage(Page<StaSql> page, StaSql staSql) {
		page.setCount(dao.findCount(staSql));
		return super.findPage(page, staSql);
	}
	
	public List<HashMap<String, Object>> commonSta(StaSql staSql)
	{
		return dao.commonSta(staSql);
	}
	
	
	@Transactional(readOnly = false)
	public void disable(StaSql staSql) {
		dao.disable(staSql);
	}
	
	@Transactional(readOnly = false)
	public void deleteByIds(StaSql staSql) {
		if (staSql == null || staSql.getIds() == null || staSql.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(staSql);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<StaSql> list) {
		if (Collections3.isEmpty(list)) {
			return;
		}
		for (StaSql item : list) {
			if (item.getIsNewRecord()) {
				item.preInsert();
			}
		}
		dao.batchInsert(list);
	}

	@Transactional(readOnly = false)
	public void batchUpdate(List<StaSql> list) {
		if (Collections3.isEmpty(list)) {
			return;
		}
		for (StaSql item : list) {
			item.preUpdate();
		}
		dao.batchUpdate(list);
	}

	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<StaSql> list) {
		if (Collections3.isEmpty(list)) {
			return;
		}
		for (StaSql item : list) {
			if (item.getIsNewRecord()) {
				item.preUpdate();
			} else {
				item.preInsert();
			}
		}
		dao.batchInsertUpdate(list);
	}
	
}