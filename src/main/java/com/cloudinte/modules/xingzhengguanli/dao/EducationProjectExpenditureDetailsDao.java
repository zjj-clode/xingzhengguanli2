package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpenditureDetails;

/**
 * 项目支出明细DAO接口
 * @author dcl
 * @version 2019-12-13
 */
@MyBatisDao
public interface EducationProjectExpenditureDetailsDao extends CrudDao<EducationProjectExpenditureDetails> {
	long findCount(EducationProjectExpenditureDetails educationProjectExpenditureDetails);
	void batchInsert(List<EducationProjectExpenditureDetails> educationProjectExpenditureDetails);
	void batchUpdate(List<EducationProjectExpenditureDetails> educationProjectExpenditureDetails);
	void batchInsertUpdate(List<EducationProjectExpenditureDetails> educationProjectExpenditureDetails);
	void disable(EducationProjectExpenditureDetails educationProjectExpenditureDetails);
	void deleteByIds(EducationProjectExpenditureDetails educationProjectExpenditureDetails);
}