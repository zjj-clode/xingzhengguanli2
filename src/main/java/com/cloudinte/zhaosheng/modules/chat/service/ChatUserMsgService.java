/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.zhaosheng.modules.chat.dao.ChatUserMsgDao;
import com.cloudinte.zhaosheng.modules.chat.entity.ChatUserMsg;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 用户聊天消息Service
 * @author pengrz
 * @version 2017-08-29
 */
@Service
@Transactional(readOnly = true)
public class ChatUserMsgService extends CrudService<ChatUserMsgDao, ChatUserMsg> {

	public ChatUserMsg get(String id) {
		return super.get(id);
	}
	
	public List<ChatUserMsg> findList(ChatUserMsg chatUserMsg) {
		return super.findList(chatUserMsg);
	}
	
	public Page<ChatUserMsg> findPage(Page<ChatUserMsg> page, ChatUserMsg chatUserMsg) {
		return super.findPage(page, chatUserMsg);
	}
	
	@Transactional(readOnly = false)
	public void save(ChatUserMsg chatUserMsg) {
		super.save(chatUserMsg);
	}
	
	@Transactional(readOnly = false)
	public void delete(ChatUserMsg chatUserMsg) {
		super.delete(chatUserMsg);
	}
	
}