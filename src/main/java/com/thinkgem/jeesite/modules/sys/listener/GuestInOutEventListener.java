package com.thinkgem.jeesite.modules.sys.listener;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.UserAgentUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.GuestAccess;
import com.thinkgem.jeesite.modules.sys.event.GuestEntryEvent;
import com.thinkgem.jeesite.modules.sys.event.GuestLeaveEvent;
import com.thinkgem.jeesite.modules.sys.service.GuestAccessService;

import eu.bitwalker.useragentutils.UserAgent;
import net.ipip.ipdb.IpInfoUtils;

/**
 * @author gyl
 */
@Component
public class GuestInOutEventListener implements ApplicationListener<ApplicationEvent> {

	protected final static Logger logger = LoggerFactory.getLogger(GuestInOutEventListener.class);

	@Autowired
	private GuestAccessService guestAccessService;

	// 异步操作，@Async，不影响主线程运行。
	//开启异步会导致获取不到request
	//@Async
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		try {
			if (event instanceof GuestEntryEvent) {//访客进入事件
				GuestEntryEvent guestEntryEvent = (GuestEntryEvent) event;
				logger.debug("onApplicationEvent()...guestEntryEvent={}", guestEntryEvent.getSource());

				Session session = guestEntryEvent.getSession();
				HttpServletRequest request = Servlets.getRequest();
				logger.debug("session={}", session);
				//
				GuestAccess guest = new GuestAccess();
				String id = IdGen.uuid();
				guest.setIsNewRecord(true);
				guest.setId(id);
				session.setAttribute("guest_access_id", id);
				guest.setSessionId(session.getId().toString());
				//
				Date date = session.getStartTimestamp();
				guest.setLoginYear(DateUtils.getYear());
				guest.setLoginYearMonth(DateUtils.formatDate(date, "yyyy-MM"));
				guest.setLoginYearMonthDay(DateUtils.formatDate(date, "yyyy-MM-dd"));
				guest.setLoginDateTime(date);
				guest.setLogoutDateTime(session.getLastAccessTime());
				guest.setDuration("1");
				guest.setLastAccessTime(session.getLastAccessTime());
				//
				String url = IpInfoUtils.getLocation(request);
				if(StringUtils.isBlank(url)) {
					url = session.getAttribute("guest_access_url").toString();
				}
				if(StringUtils.isBlank(url)) {
					url = "未知网址";
				}
				guest.setUrl(url);
				
				String ip = IpInfoUtils.getIpAddr(request);
				if(StringUtils.isBlank(ip)) {
					ip = session.getAttribute("guest_access_ip").toString();
				}
				if(StringUtils.isBlank(ip)) {
					ip = session.getHost();
				}
				if (ip.equals("0:0:0:0:0:0:0:1")) {
					ip = "127.0.0.1";
				}
				
				Map<String,String> ipInfo = IpInfoUtils.getIpInfo(ip);
				if(ipInfo!=null&&!ipInfo.isEmpty()) {
					guest.setCountry(ipInfo.get("country_name"));
					guest.setRegion(ipInfo.get("region_name"));
					guest.setCity(ipInfo.get("city_name"));
					guest.setArea("");
				}else{
					guest.setCountry("未知");
					guest.setRegion("未知");
					guest.setCity("未知");
				}
				if(StringUtils.isBlank(ip)) {
					ip = "未知IP";
				}
				guest.setIp(ip);
				
				UserAgent agent;
				if(request!=null) {
					agent = UserAgentUtils.getUserAgent(request);
				} else {
					agent = (UserAgent)session.getAttribute("guest_access_agent");
				}
				
				if(agent != null) {
					guest.setBrowser(agent.getBrowser() == null ? "未知类型" : agent.getBrowser().toString());
					guest.setBrowserVersion(agent.getBrowserVersion() == null ? "未知版本" : agent.getBrowserVersion().toString());
					guest.setOs(agent.getOperatingSystem() == null ? "未知系统" : agent.getOperatingSystem().toString());
					guest.setDeviceType(agent.getOperatingSystem() == null || agent.getOperatingSystem().getDeviceType() == null ? "未知类型" : agent.getOperatingSystem().getDeviceType().toString());
					guest.setManufacturer(agent.getOperatingSystem() == null || agent.getOperatingSystem().getManufacturer() == null ? "未知品牌" : agent.getOperatingSystem().getManufacturer().toString());
				}else {
					guest.setBrowser("未知类型");
					guest.setBrowserVersion("未知版本");
					guest.setOs("未知系统");
					guest.setDeviceType("未知类型");
					guest.setManufacturer("未知品牌");
				}
				
//				Enumeration<String> names = request.getHeaderNames();
//				StringBuffer headerInfo = new StringBuffer();
//				while(names.hasMoreElements()) {
//					String name = names.nextElement();
//					Enumeration<String> values = request.getHeaders(name);
//					while(values.hasMoreElements()) {
//						String value = values.nextElement();
//						headerInfo.append(name+":"+value+";;");
//					}
//				}
				//先不保存，内容太长，后期优化
//				guest.setHeaderInfo(headerInfo.toString());
				guest.setHeaderInfo("");
				
				guest.setOnline("1");
				
				guest.setLoginName(null);
				guest.setName(null);
				guest.setDepId(null);
				guest.setDepName(null);
				//
				guest.setPlatform(null);
				//
				guestAccessService.login(guest);
			} else if (event instanceof GuestLeaveEvent) {//访客离开事件
				GuestLeaveEvent guestLeaveEvent = (GuestLeaveEvent) event;
				logger.debug("onApplicationEvent()...GuestLeaveEvent");
				Session session = guestLeaveEvent.getSession();
				if(session!=null) {
					GuestAccess guest = new GuestAccess();
					guest.setId(session.getAttribute("guest_access_id").toString());
					
					Date date = new Date();
					String duration = (date.getTime() - session.getStartTimestamp().getTime())/1000+1+"";//单位秒
					guest.setLogoutDateTime(date);
					guest.setDuration(duration);
					guest.setLastAccessTime(session.getLastAccessTime());
					guest.setOnline("0");
					
					guestAccessService.logout(guest);
				}
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
