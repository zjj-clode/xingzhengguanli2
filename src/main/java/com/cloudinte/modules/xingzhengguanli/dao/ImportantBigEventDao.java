package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.ImportantBigEvent;

/**
 * 三重一大事项DAO接口
 * @author dcl
 * @version 2019-12-04
 */
@MyBatisDao
public interface ImportantBigEventDao extends CrudDao<ImportantBigEvent> {
	long findCount(ImportantBigEvent importantBigEvent);
	void batchInsert(List<ImportantBigEvent> importantBigEvent);
	void batchUpdate(List<ImportantBigEvent> importantBigEvent);
	void batchInsertUpdate(List<ImportantBigEvent> importantBigEvent);
	void disable(ImportantBigEvent importantBigEvent);
	void deleteByIds(ImportantBigEvent importantBigEvent);
}