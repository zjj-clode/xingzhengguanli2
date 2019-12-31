package com.thinkgem.jeesite.modules.sys.event;

import org.apache.shiro.session.Session;
import org.springframework.context.ApplicationEvent;

/**
 * @author gyl
 */
public class GuestLeaveEvent extends ApplicationEvent {
	private static final long serialVersionUID = 2463245753004129632L;
	private Session session;

	public GuestLeaveEvent(Object source) {
		super(source);
	}

	public GuestLeaveEvent(Object source, Session session) {
		super(source);
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

}
