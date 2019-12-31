package com.cloudinte.zhaosheng.modules.chat.service;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.Session;
import nl.justobjects.pushlet.core.SessionManager;
import nl.justobjects.pushlet.util.PushletException;

public class ChatSessionManager extends SessionManager {

	@Override
	public Session createSession(Event anEvent) throws PushletException {
		String sessionId = anEvent.getField("userId", "visitor");
		// 根据传过来的参数生成sessionId
		return Session.create(sessionId);
	}

}
