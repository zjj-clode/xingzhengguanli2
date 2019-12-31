package com.cloudinte.modules.xingzhengguanli.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectSpendDetails;

/**
 * 项目支出明细DAO接口
 * @author dcl
 * @version 2019-12-14
 */
@MyBatisDao
public interface EducationProjectSpendDetailsDao extends CrudDao<EducationProjectSpendDetails> {
	long findCount(EducationProjectSpendDetails educationProjectSpendDetails);
	void batchInsert(List<EducationProjectSpendDetails> educationProjectSpendDetails);
	void batchUpdate(List<EducationProjectSpendDetails> educationProjectSpendDetails);
	void batchInsertUpdate(List<EducationProjectSpendDetails> educationProjectSpendDetails);
	void disable(EducationProjectSpendDetails educationProjectSpendDetails);
	void deleteByIds(EducationProjectSpendDetails educationProjectSpendDetails);
	void deleteByUserAndYear(EducationProjectSpendDetails educationProjectSpendDetails);
	List<EducationProjectSpendDetails> findExportSpendDetails(EducationProjectSpendDetails educationProjectSpendDetails);
}