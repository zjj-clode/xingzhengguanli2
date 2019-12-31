package com.thinkgem.jeesite.modules.sys.listener;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.http.util.IPAddress;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.SiteAccess;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.event.LoginEvent;
import com.thinkgem.jeesite.modules.sys.event.LogoutAllEvent;
import com.thinkgem.jeesite.modules.sys.event.LogoutEvent;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.SiteAccessService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * @author 廖水平
 */
@Component
public class LogonEventListener implements ApplicationListener<ApplicationEvent> {

	protected final static Logger logger = LoggerFactory.getLogger(LogonEventListener.class);

	@Autowired
	private SiteAccessService accessService;

	// 异步操作，不影响主线程运行。
	@Async
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		try {
			if (event instanceof LoginEvent) {//登录事件
				LoginEvent loginEvent = (LoginEvent) event;
				logger.debug("onApplicationEvent()...loginEvent={}", loginEvent.getSource());

				Session session = loginEvent.getSession();

				SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				logger.debug("session={},principalCollection={}", session, principalCollection);

				//
				SiteAccess access = new SiteAccess();
				access.setSessionId(session.getId().toString());
				//
				Date date = new Date();
				access.setLoginYear(DateUtils.getYear());
				access.setLoginYearMonth(DateUtils.formatDate(date, "yyyy-MM"));
				access.setLoginYearMonthDay(DateUtils.formatDate(date, "yyyy-MM-dd"));
				access.setLoginDateTime(date);
				access.setLogoutDateTime(date);
				//

				if (principalCollection == null) {
					return;
				}
				Principal primaryPrincipal = (Principal) principalCollection.getPrimaryPrincipal();
				if (primaryPrincipal == null) {
					return;
				}

				logger.debug("primaryPrincipal.getId()={},primaryPrincipal.getLoginName()={},primaryPrincipal.getName()={},primaryPrincipal.getSessionid()={},primaryPrincipal.getUrl1()={}",
						primaryPrincipal.getId(), primaryPrincipal.getLoginName(), primaryPrincipal.getName(), primaryPrincipal.getSessionid(), primaryPrincipal.getUrl1());

				String userId = primaryPrincipal.getId();
				User user = UserUtils.get(userId);
				access.setLoginName(user.getLoginName());
				access.setName(user.getName());
				access.setDepId(user.getOffice().getId());
				access.setDepName(user.getOffice().getName());
				//
				String ip = session.getHost();
				access.setIp(ip);
				//
				if (!"127.0.0.1".equals(ip) && !"0:0:0:0:0:0:0:1".equals(ip)) {//本机
					Map<String, String> map = getIpInfo(ip);
					if (map != null && !map.isEmpty()) {
						access.setCountry(map.get("country"));
						access.setArea(map.get("area"));
						access.setRegion(map.get("region"));
						access.setCity(map.get("city"));
					}
				}
				//
				String platform = (String) session.getAttribute("deviceType");
				access.setPlatform(platform);
				//
				access.setLogoutDateTime(null);
				access.setOnline("1");
				//
				accessService.login(access);

			} else if (event instanceof LogoutEvent) {//登出事件
				LogoutEvent logoutEvent = (LogoutEvent) event;
				logger.debug("onApplicationEvent()...LogoutEvent");
				Session session = logoutEvent.getSession();
				SiteAccess access = new SiteAccess();
				access.setSessionId(session.getId().toString());
				accessService.logout(access);
			} else if (event instanceof LogoutAllEvent) {//全部登出事件
				logger.debug("onApplicationEvent()...LogoutAllEvent");
				accessService.logoutAll(new SiteAccess());
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据ip获取其所在国家、地区、省份、城市
	 */
	public static Map<String, String> getIpInfo(String ip) {
		if (StringUtils.isBlank(ip)) {
			return new HashMap<String, String>();
		}
		try {
			new IPAddress(ip);
		} catch (Exception e) {//not ip
			return new HashMap<String, String>();
		}
		//
		//////////////////////////////////
		Map<String, String> map = null;
		if (new Random().nextBoolean()) {
			map = getIpInfo_taobao(ip);
			if (map.isEmpty()) {
				map = getIpInfo_sina(ip);
			}
		} else {
			map = getIpInfo_sina(ip);
			if (map.isEmpty()) {
				map = getIpInfo_taobao(ip);
			}
		}
		//////////////////////////////////

		return map;
	}

	/**
	 * http://ip.taobao.com/service/getIpInfo.php?ip=202.205.88.66
	 *
	 * @param ip
	 * @return
	 */
	public static Map<String, String> getIpInfo_taobao(String ip) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(ip)) {
			return map;
		}
		//
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
		logger.debug(url);
		String jsonStr = getJsonStrFromNet(url);
		if (StringUtils.isBlank(jsonStr)) {
			return map;
		}
		try {
			JSONObject jsonObject = JSON.parseObject(jsonStr);
			logger.debug("jsonObject ---------> {}", jsonObject);
			if (jsonObject.containsKey("data")) {
				JSONObject data = jsonObject.getJSONObject("data");
				map.put("country", data.getString("country"));//国家
				map.put("area", data.getString("area"));//地区
				map.put("region", data.getString("region"));//省
				map.put("city", data.getString("city"));//市
			}
		} catch (JSONException e) {
			logger.error("e.getMessage => {}，jsonStr => {}", e.getMessage(), jsonStr);
		}
		//
		return map;
	}

	/**
	 * http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=219.242.98.111
	 */
	public static Map<String, String> getIpInfo_sina(String ip) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(ip)) {
			return map;
		}
		String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip;
		logger.debug(url);
		String jsonStr = getJsonStrFromNet(url);
		if (StringUtils.isBlank(jsonStr)) {
			return map;
		}
		try {
			// {"ret":1,"start":-1,"end":-1,"country":"\u4e2d\u56fd","province":"\u5317\u4eac","city":"\u5317\u4eac","district":"","isp":"","type":"","desc":""}
			JSONObject jsonObject = JSON.parseObject(jsonStr);
			logger.debug("jsonObject ---------> {}", jsonObject);
			if (jsonObject.containsKey("country")) {
				map.put("country", jsonObject.getString("country"));
				map.put("area", "");
				map.put("region", jsonObject.getString("province"));
				map.put("city", jsonObject.getString("city"));
			}
		} catch (JSONException e) {
			logger.error("e.getMessage => {}，jsonStr => {}", e.getMessage(), jsonStr);
		}
		//
		return map;
	}

	public static List<String> userAgentSet = Lists.newArrayList(//
			"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50", //
			"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50", //
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;", //
			"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)", //
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)", //
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1", //
			"Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1", //
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0", //
			"Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11", //
			"Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11", //
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11", //
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)", //
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)", //
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)", //
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"//
	);

	public static String getJsonStrFromNet(String url) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		request.addHeader("Accept-Encoding", "gzip, deflate");
		request.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		request.addHeader("Cache-Control", "max-age=0");
		request.addHeader("Connection", "keep-alive");
		request.addHeader("Host", url.contains("taobao.com") ? "ip.taobao.com" : "int.dpool.sina.com.cn");
		request.addHeader("Upgrade-Insecure-Requests", "1");
		request.addHeader("User-Agent", userAgentSet.get(new Random().nextInt(userAgentSet.size())));
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request);
			String content = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.debug("content-------->{}", content);
			return content;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		getIpInfo_taobao("202.112.162.62");
		getIpInfo_sina("202.112.162.62");
	}
}
