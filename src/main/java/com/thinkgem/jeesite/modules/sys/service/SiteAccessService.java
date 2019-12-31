/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.SiteAccessDao;
import com.thinkgem.jeesite.modules.sys.entity.SiteAccess;

@Service
@Transactional(readOnly = true)
public class SiteAccessService extends CrudService<SiteAccessDao, SiteAccess> implements InitializingBean {
	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	private SiteAccessDao siteAccessDao;
	@Override
	public Page<SiteAccess> findPage(Page<SiteAccess> page, SiteAccess siteAccess) {
		page.setCount(findCount(siteAccess));
		return super.findPage(page, siteAccess);
	}

	public long findCount(SiteAccess siteAccess) {
		return dao.findCount(siteAccess);
	}

	public long findLoginUserCount() {
		SiteAccess siteAccess = new SiteAccess();
		siteAccess.setOnline("1");
		return findCount(siteAccess);
	}

	public Collection<Session> findOnlineUser() {
		return sessionDAO.getActiveSessions(true);
	}

	public long findOnlineUserCount() {
		Collection<Session> sessions = findOnlineUser();
		return Collections3.isEmpty(sessions) ? 0 : sessions.size();
	}

	/**
	 * 根据session id 设置为离线状态
	 */
	@Transactional(readOnly = false)
	public void logout(SiteAccess siteAccess) {
		siteAccess.setOnline("0");
		siteAccess.setLogoutDateTime(new Date());
		dao.logout(siteAccess);
	}

	@Transactional(readOnly = false)
	public void logoutAll(SiteAccess siteAccess) {
		siteAccess.setLogoutDateTime(new Date());
		dao.logoutAll(siteAccess);
	}

	@Transactional(readOnly = false)
	public void login(SiteAccess siteAccess) {
		siteAccess.setOnline("1");
		siteAccess.setLogoutDateTime(null);
		try {
			save(siteAccess);//数据库对session_id字段作唯一限定，如果插入重复记录会报错，这里忽略掉就行了。
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 同步登录状态<br>
	 * 初始化后(系统启动后)，检查数据库中当前状态仍然是在线的，根据实际情况更新状态。
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		SiteAccess siteAccess = new SiteAccess();
		siteAccess.setOnline("1");
		List<SiteAccess> siteAccesses = findList(siteAccess);
		for (SiteAccess sa : siteAccesses) {
			check(sa);
		}
	}

	private void check(SiteAccess siteAccess) {
		String sessionId = siteAccess.getSessionId();
		logger.debug("check session[{}] ...", sessionId);
		Session session = null;
		try {
			session = sessionDAO.readSession(sessionId);
		} catch (UnknownSessionException e) {
			logger.debug(e.getMessage());
		}
		if (session == null) {//session不存在了
			logout(siteAccess);
		} else {
			SimpleSession simpleSession = (SimpleSession) session;
			try {
				simpleSession.validate();//验证session是否有效
			} catch (InvalidSessionException e) {//session失效了
				logger.debug(e.getMessage());
				logout(siteAccess);
			}
		}
	}

	public LinkedHashMap<String, Long> countMonthByDay(SiteAccess siteAccess) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		Date yearMonth = null;
		String loginYearMonth = siteAccess.getLoginYearMonth();
		if (!StringUtils.isBlank(loginYearMonth)) {
			try {
				yearMonth = DateUtils.parseDate(loginYearMonth, "yyyy-MM");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (yearMonth == null) {
			return map;
		}
		List<HashMap<String, Object>> list = dao.countMonthByDay(siteAccess);
		if (Collections3.isEmpty(list)) {
			return map;
		}
		HashMap<String, Long> tepMap = new HashMap<>();
		for (HashMap<String, Object> hashMap : list) {
			String login_date = (String) hashMap.get("login_date");
			Long login_count = ((Number) hashMap.get("login_count")).longValue();
			tepMap.put(login_date, login_count);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(yearMonth);
		int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= max; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, i);
			String dateStr = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd");
			if (!tepMap.containsKey(dateStr)) {
				map.put(dateStr, 0L);
			} else {
				map.put(dateStr, tepMap.get(dateStr));
			}
			//logger.debug("日期={},数量={}", dateStr, map.get(dateStr));
		}
		return map;
	}

	public LinkedHashMap<String, Long> countYearByMonth(SiteAccess siteAccess) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		Date year = null;
		String loginYear = siteAccess.getLoginYear();
		if (!StringUtils.isBlank(loginYear)) {
			try {
				year = DateUtils.parseDate(loginYear, "yyyy");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (year == null) {
			return map;
		}
		List<HashMap<String, Object>> list = dao.countYearByMonth(siteAccess);
		if (Collections3.isEmpty(list)) {
			return map;
		}
		HashMap<String, Long> tepMap = new HashMap<>();
		for (HashMap<String, Object> hashMap : list) {
			String login_month = (String) hashMap.get("login_month");
			Long login_count = ((Number) hashMap.get("login_count")).longValue();
			//logger.debug("月={},数量={}", login_month, login_count);
			tepMap.put(login_month, login_count);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(year);
		for (int i = 0; i < 12; i++) {
			calendar.set(Calendar.MONTH, i);
			String dateStr = DateUtils.formatDate(calendar.getTime(), "yyyy-MM");
			//logger.debug("map.containsKey(dateStr)={}", map.containsKey(dateStr));
			if (!tepMap.containsKey(dateStr)) {
				map.put(dateStr, 0L);
			} else {
				map.put(dateStr, tepMap.get(dateStr));
			}
			//logger.debug("月={},数量={}", dateStr, map.get(dateStr));
		}
		return map;
	}

	public LinkedHashMap<String, Long> countByLoginname(SiteAccess siteAccess) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		List<HashMap<String, Object>> list = dao.countByLoginname(siteAccess);
		for (HashMap<String, Object> hashMap : list) {
			String login_name = (String) hashMap.get("login_name");
			String name = (String) hashMap.get("name");
			Long login_count = ((Number) hashMap.get("login_count")).longValue();
			map.put(name + "[" + login_name + "]", login_count);
		}
		return map;
	}

	@Transactional(readOnly = false)
	public boolean kickSession(SiteAccess siteAccess) {
		Session session = null;
		try {
			session = sessionDAO.readSession(siteAccess.getSessionId());
		} catch (UnknownSessionException e) {
			logger.debug(e.getMessage());
			return false;
		}
		if (session != null) {
			sessionDAO.delete(session);
			logout(siteAccess);
			return true;
		}
		return false;
	}
	
	/**
	 * 首页的系统访问统计图表
	 */
	public List<HashMap<String, Object>> countForHighcharts(String beginDay,String endDay){
		return dao.countForHighcharts(beginDay,endDay);
	}
	
	/**
	 * 首页的学生访问统计图表
	 */
	public List<HashMap<String, Object>> countForStudentHighcharts(String beginDay,String endDay){
		return dao.countForStudentHighcharts(beginDay,endDay);
	}
	
	public long countSiteAccess(SiteAccess siteAccess) {
		return siteAccessDao.countSiteAccess(siteAccess);
	}
	
	public long countViewSiteAccess(SiteAccess siteAccess) {
		return siteAccessDao.countViewSiteAccess(siteAccess);
	}
}
