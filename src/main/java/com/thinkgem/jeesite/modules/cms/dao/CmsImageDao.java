package com.thinkgem.jeesite.modules.cms.dao;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.CmsImage;

/**
 * 图片管理DAO接口
 * @author G6
 * @version 2019-01-11
 */
@MyBatisDao
public interface CmsImageDao extends CrudDao<CmsImage> {
	long findCount(CmsImage cmsImage);
	void batchInsert(List<CmsImage> cmsImage);
	void batchUpdate(List<CmsImage> cmsImage);
	void batchInsertUpdate(List<CmsImage> cmsImage);
	void disable(CmsImage cmsImage);
	void deleteByIds(CmsImage cmsImage);
}