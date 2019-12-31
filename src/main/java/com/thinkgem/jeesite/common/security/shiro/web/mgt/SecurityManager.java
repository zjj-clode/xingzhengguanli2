package com.thinkgem.jeesite.common.security.shiro.web.mgt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.thinkgem.jeesite.modules.sys.event.LoginEvent;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;

/**
 * 继承{@link org.apache.shiro.web.mgt.DefaultWebSecurityManager}，复写
 * {@link org.apache.shiro.web.mgt.DefaultWebSecurityManager#onSuccessfulLogin(AuthenticationToken, AuthenticationInfo, Subject)}
 * 方法，增加自定义操作。
 * 
 * @author 廖水平
 */
public class SecurityManager extends org.apache.shiro.web.mgt.DefaultWebSecurityManager {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 登录成功后，记录到登录用户库。
	 */
	@Override
	protected void onSuccessfulLogin(AuthenticationToken token, AuthenticationInfo info, Subject subject) {
		// TODO Auto-generated method stub
		super.onSuccessfulLogin(token, info, subject);

		Object principal = subject.getPrincipal();
		if (principal instanceof Principal) {
			logger.debug("onSuccessfulLogin...登录成功，用户账号：{}", ((Principal) principal).getLoginName());
		}

		applicationContext.publishEvent(new LoginEvent(this, subject.getSession()));
	}
}
