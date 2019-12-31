package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.common.utils.Articletemp;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.dao.QaAndArticleIndexDao;
import com.thinkgem.jeesite.modules.cms.entity.QaAndArticleIndex;

/**
 * 问题咨询和文章索引管理Service
 *
 * @author gff
 * @version 2018-03-19
 */
@Service
@Transactional(readOnly = true)
public class QaAndArticleIndexService extends CrudService<QaAndArticleIndexDao, QaAndArticleIndex> {
	
	@Autowired
	private QaAndArticleIndexDao qaAndArticleIndexDao;
	
	@Override
	public Page<QaAndArticleIndex> findPage(Page<QaAndArticleIndex> page, QaAndArticleIndex qaAndArticleIndex) {
		page.setCount(dao.findCount(qaAndArticleIndex));
		return super.findPage(page, qaAndArticleIndex);
	}
	
	@Transactional(readOnly = false)
	public void disable(QaAndArticleIndex qaAndArticleIndex) {
		dao.disable(qaAndArticleIndex);
	}
	
	/*
	@Transactional(readOnly = false)
	public void deleteByIds(String[] ids) {
		if (ids == null || ids.length < 1) {
			return;
		}
		dao.deleteByIds(ids);
	}
	*/
	
	@Transactional(readOnly = false)
	public void deleteByIds(QaAndArticleIndex qaAndArticleIndex) {
		if (qaAndArticleIndex == null || qaAndArticleIndex.getIds() == null || qaAndArticleIndex.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(qaAndArticleIndex);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<QaAndArticleIndex> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<QaAndArticleIndex> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<QaAndArticleIndex> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
	/**
	 * 将索引转换为统一的对象
	 */
	@Transactional(readOnly = false)
	public QaAndArticleIndex toQaAndArticleIndex(Articletemp articletemp) {
		QaAndArticleIndex qaAndArticletemp = new QaAndArticleIndex();
		qaAndArticletemp.setIndexId(articletemp.getId());
		qaAndArticletemp.setTitle(articletemp.getTitle());
		qaAndArticletemp.setType(articletemp.getType());
		qaAndArticletemp.setLink(articletemp.getLink());
		qaAndArticletemp.setContent(articletemp.getContent());
		qaAndArticletemp.setAskpname(articletemp.getAskpname());
		qaAndArticletemp.setSearchFrom(articletemp.getSearchFrom());
		qaAndArticletemp.setLuCreateDate(articletemp.getCreateDate());
		QaAndArticleIndex qaAndArticleIndex = qaAndArticleIndexDao.getByIdAndType(qaAndArticletemp);
		if (qaAndArticleIndex == null) {
			return qaAndArticletemp;
		} else {
			return qaAndArticleIndex;
		}
	}
	
	@Transactional(readOnly = false)
	public void save(Articletemp articletemp) {
		save(toQaAndArticleIndex(articletemp));
	}
	
}