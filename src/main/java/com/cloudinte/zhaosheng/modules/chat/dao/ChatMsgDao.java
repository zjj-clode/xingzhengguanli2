/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.chat.dao;

import com.cloudinte.zhaosheng.modules.chat.entity.ChatMsg;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 聊天消息DAO接口
 * @author pengrz
 * @version 2017-08-29
 */
@MyBatisDao
public interface ChatMsgDao extends CrudDao<ChatMsg> {
	
}