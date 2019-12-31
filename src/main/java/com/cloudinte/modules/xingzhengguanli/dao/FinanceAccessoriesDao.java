package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.FinanceAccessories;

/**
 * 附件管理DAO接口
 * @author dcl
 * @version 2019-12-12
 */
@MyBatisDao
public interface FinanceAccessoriesDao extends CrudDao<FinanceAccessories> {
	long findCount(FinanceAccessories financeAccessories);
	void batchInsert(List<FinanceAccessories> financeAccessories);
	void batchUpdate(List<FinanceAccessories> financeAccessories);
	void batchInsertUpdate(List<FinanceAccessories> financeAccessories);
	void disable(FinanceAccessories financeAccessories);
	void deleteByIds(FinanceAccessories financeAccessories);
	void updateDownloadTimes(FinanceAccessories financeAccessories);
}