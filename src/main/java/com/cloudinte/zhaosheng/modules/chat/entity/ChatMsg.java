/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.zhaosheng.modules.chat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 聊天消息Entity
 * @author pengrz
 * @version 2017-08-29
 */
public class ChatMsg extends DataEntity<ChatMsg> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型：1、聊天消息，2系统消息
	private String content;		// 聊天内容
	
	public ChatMsg() {
		super();
	}

	public ChatMsg(String id){
		super(id);
	}

	@Length(min=0, max=11, message="类型：1、聊天消息，2系统消息长度必须介于 0 和 11 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}