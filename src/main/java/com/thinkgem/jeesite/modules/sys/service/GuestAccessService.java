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
import com.thinkgem.jeesite.modules.sys.dao.GuestAccessDao;
import com.thinkgem.jeesite.modules.sys.entity.GuestAccess;

/**
 * 访问信息记录模块Service
 * @author gyl
 * @version 2019-03-09
 */
@Service
@Transactional(readOnly = false)
public class GuestAccessService extends CrudService<GuestAccessDao, GuestAccess> implements InitializingBean {
	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	private GuestAccessDao guestAccessDao;
	@Override
	public Page<GuestAccess> findPage(Page<GuestAccess> page, GuestAccess guestAccess) {
		page.setCount(findCount(guestAccess));
		guestAccess.setPage(page);
		page.setList(findList(guestAccess));
		return page;
	}
	
	@Override
	public List<GuestAccess> findList(GuestAccess guestAccess) {
		guestAccess.processEmptyArrayParam();
		List<GuestAccess> guests = dao.findList(guestAccess);
		for(GuestAccess guest:guests) {
			if(StringUtils.isNotBlank(guest.getOnline())&&guest.getOnline().equals("1")) {
				guest.setLogoutDateTime(null);
				guest.setLastAccessTime(null);
				guest.setDuration("正在访问");
				continue;
			}
			changeDuration(guest);
		}
		return guests;
	}
	
	@Override
	public GuestAccess get(String id) {
		GuestAccess guest = dao.get(id);
		if(StringUtils.isNotBlank(guest.getOnline())&&guest.getOnline().equals("1")) {
			guest.setLogoutDateTime(null);
			guest.setLastAccessTime(null);
			guest.setDuration("正在访问");
			return guest;
		}
		return changeDuration(guest);
	}
	
	private GuestAccess changeDuration(GuestAccess guestAccess) {
		long duration = 0L;
		try {
			duration = Long.parseLong(guestAccess.getDuration());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return guestAccess;
		}
		long day = duration / (24 * 60 * 60);
		if (day > 7) {
			guestAccess.setDuration("超过一星期");
			return guestAccess;
		}
		long hour = duration / (60 * 60) - day * 24;
		long min = duration / 60 - day * 24 * 60 - hour * 60;
		long s = duration - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
		guestAccess.setDuration(
				(day > 0 ? day + "天" : "") + (hour > 0 ? hour + "小时" : "") + (min > 0 ? min + "分钟" : "") + s + "秒");
		return guestAccess;
	}
	
	@Transactional(readOnly = false)
	public void disable(GuestAccess guestAccess) {
		dao.disable(guestAccess);
	}
	
	/*
	@Transactional(readOnly = false)
	public void deleteByIds(String[] ids) {
		if (ids == null || ids.length < 1) {
			return;
		}
		dao.deleteByIds(ids);
	}
	*/
	
	@Transactional(readOnly = false)
	public void deleteByIds(GuestAccess guestAccess) {
		if (guestAccess == null || guestAccess.getIds() == null || guestAccess.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(guestAccess);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<GuestAccess> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<GuestAccess> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<GuestAccess> objlist) {
		dao.batchInsertUpdate(objlist);
	}

	public long findCount(GuestAccess guestAccess) {
		return dao.findCount(guestAccess);
	}

	public long findLoginUserCount() {
		GuestAccess guestAccess = new GuestAccess();
		guestAccess.setOnline("1");
		return findCount(guestAccess);
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
	public void logout(GuestAccess guestAccess) {
//		guestAccess.setOnline("0");
//		guestAccess.setLogoutDateTime(new Date());
		//无限调用session.onstop()根源，preUpdate会判断当前是否有User，没有就新建User，新建User会新建session，引起无限循环
//		guestAccess.preUpdate();
		dao.logout(guestAccess);
	}

	@Transactional(readOnly = false)
	public void logoutAll(GuestAccess guestAccess) {
//		guestAccess.setLogoutDateTime(new Date());
		dao.logoutAll(guestAccess);
	}

	@Transactional(readOnly = false)
	public void login(GuestAccess guestAccess) {
//		guestAccess.setOnline("1");
//		guestAccess.setLogoutDateTime(null);
	    try {
			save(guestAccess);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 同步登录状态<br>
	 * 初始化后(系统启动后)，检查数据库中当前状态仍然是在线的，根据实际情况更新状态。
	 * 
	 * bug 冲突，SpringContextHolder类出错。获取不到ApplicationContext，初始化注入顺序bug
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		GuestAccess guestAccess = new GuestAccess();
		logoutAll(guestAccess);
//		guestAccess.setOnline("1");
//		List<GuestAccess> guestAccesses = findList(guestAccess);
//		for (GuestAccess sa : guestAccesses) {
//			check(sa);
//		}
	}

	private void check(GuestAccess guestAccess) {
		String sessionId = guestAccess.getSessionId();
		logger.debug("check session[{}] ...", sessionId);
		Session session = null;
		try {
			session = sessionDAO.readSession(sessionId);
		} catch (UnknownSessionException e) {
			logger.debug(e.getMessage());
		}
		if (session == null) {//session不存在了
			logout(guestAccess);
		} else {
			SimpleSession simpleSession = (SimpleSession) session;
			try {
				simpleSession.validate();//验证session是否有效
			} catch (InvalidSessionException e) {//session失效了
				logger.debug(e.getMessage());
				logout(guestAccess);
			}
		}
	}

	public LinkedHashMap<String, Long> countMonthByDay(GuestAccess guestAccess) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		Date yearMonth = null;
		String loginYearMonth = guestAccess.getLoginYearMonth();
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
		List<HashMap<String, Object>> list = dao.countMonthByDay(guestAccess);
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

	public LinkedHashMap<String, Long> countYearByMonth(GuestAccess guestAccess) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		Date year = null;
		String loginYear = guestAccess.getLoginYear();
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
		List<HashMap<String, Object>> list = dao.countYearByMonth(guestAccess);
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

	public LinkedHashMap<String, Long> countByLoginname(GuestAccess guestAccess) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		List<HashMap<String, Object>> list = dao.countByLoginname(guestAccess);
		for (HashMap<String, Object> hashMap : list) {
			String login_name = (String) hashMap.get("login_name");
			String name = (String) hashMap.get("name");
			Long login_count = ((Number) hashMap.get("login_count")).longValue();
			map.put(name + "[" + login_name + "]", login_count);
		}
		return map;
	}

	@Transactional(readOnly = false)
	public boolean kickSession(GuestAccess guestAccess) {
		Session session = null;
		try {
			session = sessionDAO.readSession(guestAccess.getSessionId());
		} catch (UnknownSessionException e) {
			logger.debug(e.getMessage());
			return false;
		}
		if (session != null) {
			sessionDAO.delete(session);
			logout(guestAccess);
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
	
	public long countGuestAccess(GuestAccess guestAccess) {
		return guestAccessDao.countGuestAccess(guestAccess);
	}
	
	public long countViewGuestAccess(GuestAccess guestAccess) {
		return guestAccessDao.countViewGuestAccess(guestAccess);
	}
}
