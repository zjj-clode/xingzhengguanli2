/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.zhaosheng.modules.chat.dao.ChatUserSessionDao;
import com.cloudinte.zhaosheng.modules.chat.entity.ChatUserSession;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 用户聊天会话Service
 * @author pengrz
 * @version 2017-08-29
 */
@Service
@Transactional(readOnly = true)
public class ChatUserSessionService extends CrudService<ChatUserSessionDao, ChatUserSession> {

	public ChatUserSession get(String id) {
		return super.get(id);
	}
	
	public List<ChatUserSession> findList(ChatUserSession chatUserSession) {
		return super.findList(chatUserSession);
	}
	
	public Page<ChatUserSession> findPage(Page<ChatUserSession> page, ChatUserSession chatUserSession) {
		return super.findPage(page, chatUserSession);
	}
	
	@Transactional(readOnly = false)
	public void save(ChatUserSession chatUserSession) {
		super.save(chatUserSession);
	}
	
	@Transactional(readOnly = false)
	public void delete(ChatUserSession chatUserSession) {
		super.delete(chatUserSession);
	}
	
	/**
	 * get userSession, if not exist will created
	 * @param userId
	 * @param targetId
	 * @return
	 */
	@Transactional(readOnly = false)
	public ChatUserSession findUserSession(String userId, String targetId){
		ChatUserSession userSession = dao.findUserSession(userId, targetId);
		if(userSession == null) {
			userSession = new ChatUserSession();
			userSession.setUser(new User(userId));
			userSession.setTarget(targetId);
			userSession.preInsert();
			dao.insert(userSession);
		}
		
		return userSession;
	}
	

	public int findCustomSessionCount(String customId) {
		return dao.findCustomSessionCount(customId);
	}
}