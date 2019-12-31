package com.cloudinte.zhaosheng.modules.chat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 在线客服人员Entity
 * 
 * @author pengrz
 * @version 2017-09-09
 */
public class ChatCustom extends DataEntity<ChatCustom> {

	private static final long serialVersionUID = 1L;

	private Integer priority;

	private User user; // 客服人员

	//
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public void processEmptyArrayParam() {
		if (ids != null && ids.length < 1) {
			ids = null;
		}
	}

	public ChatCustom() {
		super();
	}

	public ChatCustom(String id) {
		super(id);
	}

	@ExcelField(title = "客服人员", align = 2, sort = 1, dictType = "")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}