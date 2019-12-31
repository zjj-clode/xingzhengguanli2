package com.thinkgem.jeesite.modules.sys.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author 廖水平
 */
public class LogoutAllEvent extends ApplicationEvent {
	private static final long serialVersionUID = -1272609626591300852L;

	public LogoutAllEvent(Object source) {
		super(source);
	}

}
