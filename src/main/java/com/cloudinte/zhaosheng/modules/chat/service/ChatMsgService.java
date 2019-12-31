/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.zhaosheng.modules.chat.dao.ChatMsgDao;
import com.cloudinte.zhaosheng.modules.chat.entity.ChatMsg;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 聊天消息Service
 * @author pengrz
 * @version 2017-08-29
 */
@Service
@Transactional(readOnly = true)
public class ChatMsgService extends CrudService<ChatMsgDao, ChatMsg> {

	public ChatMsg get(String id) {
		return super.get(id);
	}
	
	public List<ChatMsg> findList(ChatMsg chatMsg) {
		return super.findList(chatMsg);
	}
	
	public Page<ChatMsg> findPage(Page<ChatMsg> page, ChatMsg chatMsg) {
		return super.findPage(page, chatMsg);
	}
	
	@Transactional(readOnly = false)
	public void save(ChatMsg chatMsg) {
		super.save(chatMsg);
	}
	
	@Transactional(readOnly = false)
	public void delete(ChatMsg chatMsg) {
		super.delete(chatMsg);
	}
	
}