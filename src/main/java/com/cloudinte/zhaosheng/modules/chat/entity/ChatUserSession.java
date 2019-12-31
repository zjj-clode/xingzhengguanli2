/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.chat.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户聊天会话Entity
 * 
 * @author pengrz
 * @version 2017-08-29
 */
public class ChatUserSession extends DataEntity<ChatUserSession> {

	private static final long serialVersionUID = 1L;
	private String target; // 聊天对象
	private User user; // 聊天人
	private User targetUser;
	private int unreadCount;//未读消息数量

	public ChatUserSession() {
		super();
	}

	public ChatUserSession(String id) {
		super(id);
	}

	@Length(min = 1, max = 64, message = "聊天对象长度必须介于 1 和 64 之间")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

}