package com.thinkgem.jeesite.modules.sys.security;

import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieRememberMeManager extends org.apache.shiro.web.mgt.CookieRememberMeManager {

	protected Logger logger = LoggerFactory.getLogger(CookieRememberMeManager.class);

	private boolean isRememberMeCookieEnable = true;

	/**
	 * 是否往客户端写rememberMe的cookie，默认为true
	 *
	 * @param isRememberMeCookieEnable
	 */
	public void setRememberMeEnable(boolean isRememberMeCookieEnable) {
		this.isRememberMeCookieEnable = isRememberMeCookieEnable;
	}

	/**
	 * 返回false就不会往客户端写rememberMe的cookie了
	 */
	@Override
	protected boolean isRememberMe(AuthenticationToken token) {
		boolean isRememberMe = super.isRememberMe(token);
		logger.debug("token.class --------> {} ，isRememberMe --------> {} ", token.getClass().getName(), isRememberMe);
		if (!isRememberMeCookieEnable) {
			return false;
		}
		return isRememberMe;
	}

}
