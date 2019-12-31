package com.cloudinte.zhaosheng.modules.chat.web;

import java.net.URLEncoder;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.SessionManager;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudinte.zhaosheng.modules.chat.entity.ChatMsg;
import com.cloudinte.zhaosheng.modules.chat.entity.ChatUserMsg;
import com.cloudinte.zhaosheng.modules.chat.entity.ChatUserSession;
import com.cloudinte.zhaosheng.modules.chat.service.ChatMsgService;
import com.cloudinte.zhaosheng.modules.chat.service.ChatUserMsgService;
import com.cloudinte.zhaosheng.modules.chat.service.ChatUserSessionService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/chat")
public class ChatController  extends BaseController{

	@Autowired
	private ChatMsgService chatMsgService;

	@Autowired
	private ChatUserSessionService chatUserSessionService;

	@Autowired
	private ChatUserMsgService chatUserMsgService;
	
	@Autowired
	private SystemService systemService;

	@RequestMapping("user")
	public String user(String customId, String customPhoto, Model model) {
		User user = UserUtils.getUser();
		// 指定登录地址
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:" + adminPath + "/loginf?url=" + frontPath + "/chat/user";
		}
		model.addAttribute("userId", user.getId());
		model.addAttribute("userPhoto", user.getPhoto());
		model.addAttribute("customId", customId);
		model.addAttribute("customPhoto", customPhoto);
		User custom = systemService.getUser(customId);
		if(custom != null){
			model.addAttribute("customName", custom.getName());
		}
		return "modules/chat/userChat";
	}

	@RequiresPermissions("chat:custom")
	@RequestMapping("custom")
	public String custom(Model model) {
		User user = UserUtils.getUser();
		model.addAttribute("userId", user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("ename", "onlinechat");
		model.addAttribute("customPhoto", user.getPhoto());
		return "modules/chat/customChat";
	}

	/**
	 * 获取所有会话并添加到会话列表
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("chat:custom")
	@RequestMapping("userSessions")
	public List<ChatUserSession> getSessions() {
		User user = UserUtils.getUser();
		ChatUserSession chatUserSession = new ChatUserSession();
		chatUserSession.setUser(user);
		chatUserSession.setDelFlag("0");
		chatUserSession.setUnreadCount(1);
		List<ChatUserSession> sessions = chatUserSessionService.findList(chatUserSession);
		for (ChatUserSession session : sessions) {
			User targetUser = systemService.getUser(session.getTarget());
			session.setTargetUser(targetUser);
		}
		return sessions;
	}
	
	
	/**
	 * 根据名称查询会话
	 * @return
	 * */
	@ResponseBody
	@RequiresPermissions("chat:custom")
	@RequestMapping("queryUserSessions")
	public List<ChatUserSession> queryUserSessions(String name){
	    User user = UserUtils.getUser();
	    
	    List<User> userList = UserUtils.getByName(name);
	    List<ChatUserSession> sessions = new ArrayList<>();
	    for (User targetUser : userList) {
	        ChatUserSession chatUserSession = chatUserSessionService.findUserSession(user.getId(), targetUser.getId());
	        chatUserSession.setTargetUser(targetUser);
            sessions.add(chatUserSession);
        }
	    
	    return sessions;
	}

	@RequestMapping("sendMsg")
	@ResponseBody
	public String sendMsg(ChatMsg chatMsg, String toUserId) {
		User user = UserUtils.getUser();
		chatMsgService.save(chatMsg);
		ChatUserSession sendUserSession = chatUserSessionService.findUserSession(user.getId(), toUserId);
		sendUserSession.setDelFlag("0");
		sendUserSession.setUnreadCount(sendUserSession.getUnreadCount()+1);
		chatUserSessionService.save(sendUserSession);
		
		ChatUserMsg sendMsg = new ChatUserMsg();
		sendMsg.setSender("1");
		sendMsg.setReaded("0");
		sendMsg.setCreateBy(user);
		sendMsg.setSessionId(sendUserSession.getId());
		sendMsg.setChatMsgId(chatMsg.getId());

		chatUserMsgService.save(sendMsg);

		ChatUserSession recvUserSession = chatUserSessionService.findUserSession(toUserId, user.getId());
		recvUserSession.setDelFlag("0");
		recvUserSession.setUnreadCount(recvUserSession.getUnreadCount()+1);
		chatUserSessionService.save(recvUserSession);
		
		ChatUserMsg recvMsg = new ChatUserMsg();
		recvMsg.setCreateBy(new User(toUserId));
		recvMsg.setSender("0");
		recvMsg.setReaded("0");
		recvMsg.setSessionId(recvUserSession.getId());
		recvMsg.setChatMsgId(chatMsg.getId());
		chatUserMsgService.save(recvMsg);
		
		if (SessionManager.getInstance().hasSession(toUserId)) {
			try {
				Event event = Event.createDataEvent("onLineCustom");
				//event.setField("content", URLEncoder.encode(chatMsg.getContent(), "UTF-8"));
				event.setField("name", URLEncoder.encode(user.getName(), "UTF-8"));
				event.setField("photo", org.apache.commons.lang.StringUtils.trimToEmpty(user.getPhoto()));
				event.setField("time", System.currentTimeMillis());
				event.setField("msgId", chatMsg.getId());
				event.setField("senderId", user.getId());
				event.setField("sessionId", sendUserSession.getId());
				Dispatcher.getInstance().unicast(event, toUserId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "success";
	}

	/**
	 * 获取历史消息
	 * @param toUserId
	 * @param topMsgDate
	 * @param size
	 * @return
	 */
	@RequestMapping("userMsgs")
	@ResponseBody
	public List<ChatUserMsg> listUserMsgs(String toUserId, String topMsgDate, int size) {
		User user = UserUtils.getUser();
		ChatUserSession sendUserSession = chatUserSessionService.findUserSession(user.getId(), toUserId);
		
		if(sendUserSession == null) {
			return new ArrayList<>();
		}

		ChatUserMsg chatUserMsg = new ChatUserMsg();
		if(!StringUtils.isEmpty(topMsgDate)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(topMsgDate);
				chatUserMsg.setCreateDate(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		chatUserMsg.setSessionId(sendUserSession.getId());
		Page<ChatUserMsg> page = new Page<>();
		page.setPageSize(size);
		chatUserMsg.setPage(page);
		
		List<ChatUserMsg> chatUserMsgs = chatUserMsgService.findList(chatUserMsg);
		return chatUserMsgs;
	}
	
	//列表中删除会话
	@RequiresPermissions("chat:custom")
	@RequestMapping("deleteUserSession")
	@ResponseBody
	public String deleteSession(String toUserId){
		User user = UserUtils.getUser();
		ChatUserSession sendUserSession = chatUserSessionService.findUserSession(user.getId(), toUserId);
		chatUserSessionService.delete(sendUserSession);
		return "success";
	}
	
	/**
	 * 添加聊天对象的消息
	 * @param id
	 * @return
	 */
	@RequestMapping("getContent")
	@ResponseBody
	public ChatMsg getContent(String id){
		ChatMsg chatMsg = chatMsgService.get(id);
		return chatMsg;
	}

	/**
	 * 清空未读消息记录
	 * @param userId
	 * @param targetId
	 * @return
	 */
	@RequiresPermissions("chat:custom")
	@RequestMapping("clearUnreadCount")
	@ResponseBody
	public String clearUnreadCount(String userId, String targetId){
		ChatUserSession sendUserSession = chatUserSessionService.findUserSession(userId, targetId);
		sendUserSession.setUnreadCount(0);
		chatUserSessionService.save(sendUserSession);
		return "success";
	}
	
	
}
