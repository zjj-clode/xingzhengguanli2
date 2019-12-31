package com.cloudinte.zhaosheng.modules.common.dao;
import java.util.HashMap;
import java.util.List;

import com.cloudinte.zhaosheng.modules.common.entity.StaSql;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;


/**
 * 统计模板DAO接口
 * @author hhw
 * @version 2018-09-06
 */
@MyBatisDao
public interface StaSqlDao extends CrudDao<StaSql> {
	long findCount(StaSql staSql);
	void batchInsert(List<StaSql> staSql);
	void batchUpdate(List<StaSql> staSql);
	void batchInsertUpdate(List<StaSql> staSql);
	void disable(StaSql staSql);
	void deleteByIds(StaSql staSql);
	
	 List<HashMap<String, Object>> commonSta(StaSql staSql);

}