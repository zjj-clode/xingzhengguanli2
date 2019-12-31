package com.thinkgem.jeesite.modules.sys.event;

import org.apache.shiro.session.Session;
import org.springframework.context.ApplicationEvent;

/**
 * @author 廖水平
 */
public class LoginEvent extends ApplicationEvent {
	private static final long serialVersionUID = 2428995753004120653L;
	private Session session;

	public LoginEvent(Object source) {
		super(source);
	}

	public LoginEvent(Object source, Session session) {
		super(source);
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

}
