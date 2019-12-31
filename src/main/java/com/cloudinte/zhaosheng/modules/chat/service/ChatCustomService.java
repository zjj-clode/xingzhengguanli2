package com.cloudinte.zhaosheng.modules.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.zhaosheng.modules.chat.dao.ChatCustomDao;
import com.cloudinte.zhaosheng.modules.chat.entity.ChatCustom;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 在线客服人员Service
 *
 * @author pengrz
 * @version 2017-09-09
 */
@Service
@Transactional(readOnly = true)
public class ChatCustomService extends CrudService<ChatCustomDao, ChatCustom> {

	public static final String ROLE_CHAT_CUSTOM = "chat_custom";

	@Autowired
	private SystemService systemService;

	@Override
	public Page<ChatCustom> findPage(Page<ChatCustom> page, ChatCustom chatCustom) {
		page.setCount(dao.findCount(chatCustom));
		return super.findPage(page, chatCustom);
	}

	@Transactional(readOnly = false)
	public void disable(ChatCustom chatCustom) {
		dao.disable(chatCustom);
	}

	@Transactional(readOnly = false)
	@Override
	public void save(ChatCustom entity) {
		if (entity.getPriority() == null) {
			entity.setPriority(5); // 默认优先级5
		}

		// 添加客服，同时设置其“客服”角色
		if (StringUtils.isBlank(entity.getId())) {
			Role role = systemService.getRoleByEnname(ROLE_CHAT_CUSTOM);
			User user = UserUtils.get(entity.getUser().getId());
			if (role != null && user != null) {
				systemService.assignUserToRole(role, user);
			}
		}
		//

		super.save(entity);
	}

	@Transactional(readOnly = false)
	@Override
	public void delete(ChatCustom entity) {
		// 删除客服，同时删除其“客服”角色
		Role role = systemService.getRoleByEnname(ROLE_CHAT_CUSTOM);
		User user = UserUtils.get(entity.getUser().getId());
		if (role != null && user != null) {
			systemService.outUserInRole(role, user);
		}
		//
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deleteByIds(ChatCustom chatCustom) {
		if (chatCustom == null || chatCustom.getIds() == null || chatCustom.getIds().length < 1) {
			return;
		}
		//
		for (String id : chatCustom.getIds()) {
			ChatCustom entity = get(id);
			// 删除客服，同时删除其“客服”角色
			Role role = systemService.getRoleByEnname(ROLE_CHAT_CUSTOM);
			User user = UserUtils.get(entity.getUser().getId());
			if (role != null && user != null) {
				systemService.outUserInRole(role, user);
			}
			//
		}
		//
		dao.deleteByIds(chatCustom);
	}

	@Transactional(readOnly = false)
	public void batchInsert(List<ChatCustom> objlist) {
		dao.batchInsert(objlist);
	}

	@Transactional(readOnly = false)
	public void batchUpdate(List<ChatCustom> objlist) {
		dao.batchUpdate(objlist);
	}

	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<ChatCustom> objlist) {
		dao.batchInsertUpdate(objlist);
	}

}