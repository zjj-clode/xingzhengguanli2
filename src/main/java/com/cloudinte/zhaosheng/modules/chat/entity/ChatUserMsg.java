/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.chat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户聊天消息Entity
 * 
 * @author pengrz
 * @version 2017-08-29
 */
public class ChatUserMsg extends DataEntity<ChatUserMsg> {

	private static final long serialVersionUID = 1L;
	private String sender; // 是否发件人
	private String readed; // 是否已读
	private String sessionId; // 更新者
	private String chatMsgId;
	
	//关联查询
	private String content;

	public ChatUserMsg() {
		super();
	}

	public ChatUserMsg(String id) {
		super(id);
	}

	@Length(min = 1, max = 1, message = "是否发件人长度必须介于 1 和 1 之间")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Length(min = 1, max = 1, message = "是否已读长度必须介于 1 和 1 之间")
	public String getReaded() {
		return readed;
	}

	public void setReaded(String readed) {
		this.readed = readed;
	}

	@Length(min = 0, max = 64, message = "更新者长度必须介于 0 和 64 之间")
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getChatMsgId() {
		return chatMsgId;
	}

	public void setChatMsgId(String chatMsgId) {
		this.chatMsgId = chatMsgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}