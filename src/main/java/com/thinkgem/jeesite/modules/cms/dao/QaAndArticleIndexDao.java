package com.thinkgem.jeesite.modules.cms.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.QaAndArticleIndex;

/**
 * 问题咨询和文章索引管理DAO接口
 * @author gff
 * @version 2018-03-19
 */
@MyBatisDao
public interface QaAndArticleIndexDao extends CrudDao<QaAndArticleIndex> {
	long findCount(QaAndArticleIndex qaAndArticleIndex);
	void batchInsert(List<QaAndArticleIndex> qaAndArticleIndex);
	void batchUpdate(List<QaAndArticleIndex> qaAndArticleIndex);
	void batchInsertUpdate(List<QaAndArticleIndex> qaAndArticleIndex);
	void disable(QaAndArticleIndex qaAndArticleIndex);
	void deleteByIds(QaAndArticleIndex qaAndArticleIndex);
	QaAndArticleIndex getByIdAndType(QaAndArticleIndex qaAndArticletemp);
}