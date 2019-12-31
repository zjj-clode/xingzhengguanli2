package com.thinkgem.jeesite.modules.sys.listener;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.UserAgentUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.event.GuestEntryEvent;
import com.thinkgem.jeesite.modules.sys.event.GuestLeaveEvent;
import com.thinkgem.jeesite.modules.sys.event.LogoutEvent;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;

import eu.bitwalker.useragentutils.UserAgent;
import net.ipip.ipdb.IpInfoUtils;

/**
 * 登录 状态维护由自定义的
 * {@link org.apache.shiro.web.mgt.DefaultWebSecurityManager#onSuccessfulLogin(AuthenticationToken, AuthenticationInfo, Subject)}
 * 处理<br>
 * 登出 状态维护由{@link #onExpiration(Session)}和{@link #onStop(Session)}处理<br>
 * 
 * 访客进入退出事件加入{@link #onStart(Session)}、{@link #onExpiration(Session)}、{@link #onStop(Session)}方法管理
 * 
 * @author 廖水平
 */
@Component
public class SessionListener extends SessionListenerAdapter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected ApplicationContext applicationContext;

	/**
	 * session 建立
	 * @param session
	 */
	@Override
	public void onStart(Session session) {
		// TODO Auto-generated method stub
		logger.debug("onStart()...session={}", logSession(session));
		
		HttpServletRequest request = Servlets.getRequest();
		
		String ip = IpInfoUtils.getIpAddr(request);
		session.setAttribute("guest_access_ip", ip);
		
		String url = IpInfoUtils.getLocation(request);
		session.setAttribute("guest_access_url", url);
		
		if(request != null) {
			UserAgent agent = UserAgentUtils.getUserAgent(request);
			session.setAttribute("guest_access_agent", agent);
		}
		
		applicationContext.publishEvent(new GuestEntryEvent(this, session));
		super.onStart(session);
	}

	/**
	 * session超时
	 */
	@Override
	public void onExpiration(Session session) {
		logger.debug("onExpiration()...session={}", logSession(session));
		applicationContext.publishEvent(new GuestLeaveEvent(this, session));
		//只有登录用户的session才执行操作.
		if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
			applicationContext.publishEvent(new LogoutEvent(this, session));
		}
		super.onExpiration(session);
	}

	/**
	 * session终止。<br>
	 * 由 Session.stop()方法 或者 Subject.logout()方法触发.Subject.logout()会间接调用到Session.stop().<br>
	 */
	@Override
	public void onStop(Session session) {
		// TODO Auto-generated method stub
		logger.debug("onStop()...session={}", logSession(session));
		applicationContext.publishEvent(new GuestLeaveEvent(this, session));
		//此处的session已经没有了登录者信息了.
		applicationContext.publishEvent(new LogoutEvent(this, session));
		super.onStop(session);
	}

	private String logSession(Session session) {
		StringBuilder sb = new StringBuilder();
		Serializable id = session.getId();
		String host = session.getHost();
		Date lastAccessTime = session.getLastAccessTime();
		Date startTimestamp = session.getStartTimestamp();
		long timeout = session.getTimeout();
		SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session
				.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		sb.append("sessionId").append(id).append(",");
		sb.append("host").append(host).append(",");
		sb.append("lastAccessTime").append(DateUtils.formatDate(lastAccessTime, "yyyy-MM-dd HH:mm:ss")).append(",");
		sb.append("startTimestamp").append(DateUtils.formatDate(startTimestamp, "yyyy-MM-dd HH:mm:ss")).append(",");
		sb.append("timeout").append(timeout).append(",");
		//登录用户
		if (principalCollection != null) {
			Principal primaryPrincipal = (Principal) principalCollection.getPrimaryPrincipal();
			if (primaryPrincipal != null) {
				sb.append("user.id").append(primaryPrincipal.getId()).append(",");
				sb.append("user.loginName").append(primaryPrincipal.getLoginName()).append(",");
				sb.append("user.name").append(primaryPrincipal.getName()).append(",");
				sb.append("Url1").append(primaryPrincipal.getUrl1());
			}
		}
		return sb.toString();
	}
}
