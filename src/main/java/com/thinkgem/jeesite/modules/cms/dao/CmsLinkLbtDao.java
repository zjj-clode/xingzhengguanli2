package com.thinkgem.jeesite.modules.cms.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.CmsLinkLbt;
import com.thinkgem.jeesite.modules.cms.entity.Link;

/**
 * 轮播图DAO接口
 * @author gf
 * @version 2018-01-24
 */
@MyBatisDao
public interface CmsLinkLbtDao extends CrudDao<CmsLinkLbt> {
	long findCount(CmsLinkLbt cmsLinkLbt);
	void batchInsert(List<CmsLinkLbt> cmsLinkLbt);
	void batchUpdate(List<CmsLinkLbt> cmsLinkLbt);
	void batchInsertUpdate(List<CmsLinkLbt> cmsLinkLbt);
	void disable(CmsLinkLbt cmsLinkLbt);
	void deleteByIds(CmsLinkLbt cmsLinkLbt);
	public int updateExpiredWeight(CmsLinkLbt cmsLinkLbt);
}