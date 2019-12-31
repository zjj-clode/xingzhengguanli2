package com.cloudinte.zhaosheng.modules.chat.dao;
import java.util.List;

import com.cloudinte.zhaosheng.modules.chat.entity.ChatCustom;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 在线客服人员DAO接口
 * @author pengrz
 * @version 2017-09-09
 */
@MyBatisDao
public interface ChatCustomDao extends CrudDao<ChatCustom> {
	//
	long findCount(ChatCustom chatCustom);

	void batchInsert(List<ChatCustom> chatCustom);
	void batchUpdate(List<ChatCustom> chatCustom);
	void batchInsertUpdate(List<ChatCustom> chatCustom);
	void disable(ChatCustom chatCustom);
	void deleteByIds(ChatCustom chatCustom);
}