package com.thinkgem.jeesite.modules.sys.event;

import org.apache.shiro.session.Session;
import org.springframework.context.ApplicationEvent;

/**
 * @author gyl
 */
public class GuestEntryEvent extends ApplicationEvent {
	private static final long serialVersionUID = 2422865753004120971L;
	private Session session;

	public GuestEntryEvent(Object source) {
		super(source);
	}

	public GuestEntryEvent(Object source, Session session) {
		super(source);
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

}
