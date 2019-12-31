/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.SiteAccessDao;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class HomeHighchartsUtils {
	protected static Logger logger = LoggerFactory.getLogger(HomeHighchartsUtils.class);
	
	private static SiteAccessDao siteAccessDao = SpringContextHolder.getBean(SiteAccessDao.class);
	
	private static Map<String,Object> stuSysAccessCountMap = new HashMap<String,Object>();

	public static final String CACHE_STU_SYS_ACCESS_COUNT = "stu_sys_access_count";
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 前台获取当前学生和系统的人数统计数据
	 * @param 
	 * @return
	 */
	public static Map<String,Object> getStuAccess(){
		
		String cachename=CACHE_STU_SYS_ACCESS_COUNT;
		try
		{
			stuSysAccessCountMap=(Map<String, Object>) CacheUtils.get(cachename);
		}catch(Exception ex){}

		if(stuSysAccessCountMap==null)
		{
			queryStuSysAccess();
			CacheUtils.put(cachename, stuSysAccessCountMap);
		}else{
			//如果缓存中存在，则比对日期，看是否为今天的数据
			String today = stuSysAccessCountMap.get("today").toString();
			String now = sdf.format(new Date());
			if(now.equals(today)){//如果等于当天，直接从缓存中取数据
				return stuSysAccessCountMap;
			}
			//重新查询数据库
			queryStuSysAccess();
			//并且放于缓存中
			CacheUtils.put(cachename,stuSysAccessCountMap);
		}
		return stuSysAccessCountMap;
	}
	/**
	 * 获取近30天系统登录人数列表
	 * @return
	 */
	public static List getSysAccessCountList(){
		getStuAccess();
		return (List) stuSysAccessCountMap.get("sys_access_count");
	}
	
	/**
	 * 获取近30天学生登录人数列表
	 * @return
	 */
	public static List getStuAccessCountList(){
		getStuAccess();
		return (List) stuSysAccessCountMap.get("stu_access_count");
	}
	
	
	/**
	 * 获取截止昨天的系统登录日期，
	 * @return
	 */
	public static Map<String,Object> queryStuSysAccess(){
		//获取截止昨天的之前7天，10天，30天系统登录的次数，人数
		//获取当前时间，并且计算出昨天以及30天前
		Calendar calendar = Calendar.getInstance();
		stuSysAccessCountMap = new HashMap<String,Object>();
		stuSysAccessCountMap.put("today", sdf.format(calendar.getTime()));
		calendar.add(Calendar.DATE, -1);
		String endDay = sdf.format(calendar.getTime());
		//在昨天日期的基础上-30天
		calendar.add(Calendar.DATE, -29);
		String beginDay= sdf.format(calendar.getTime());
		List<HashMap<String, Object>> list = siteAccessDao.countForHighcharts(beginDay, endDay);
		List<HashMap<String, Object>> stuList = siteAccessDao.countForStudentHighcharts(beginDay, endDay);
		//集合用来准备系统访问页面数据
		List<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();
		for(int i=0;i<30;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(i!=0){
				calendar.add(Calendar.DATE, 1);
			}
			String nextDay = sdf.format(calendar.getTime());
			map.put("login_year_month_day", nextDay);
			for (HashMap<String, Object> l : list) {
				if(nextDay.equals(l.get("login_year_month_day"))){
					map.put("login_count", l.get("login_count"));
					map.put("login_name_count", l.get("login_name_count"));
					break;
				}else{
					map.put("login_count", 0);
					map.put("login_name_count", 0);
				}
			}
			list1.add(map);
		}
		//集合用来准备系统访问页面数据
		calendar.add(Calendar.DATE, -30);
		List<HashMap<String, Object>> list2 = new ArrayList<HashMap<String, Object>>();
		for(int i=0;i<30;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			calendar.add(Calendar.DATE, 1);
			String nextDay = sdf.format(calendar.getTime());
			map.put("login_year_month_day", nextDay);
			for (HashMap<String, Object> l : stuList) {
				if(nextDay.equals(l.get("login_year_month_day"))){
					map.put("login_count", l.get("login_count"));
					map.put("login_name_count", l.get("login_name_count"));
					break;
				}else{
					map.put("login_count", 0);
					map.put("login_name_count", 0);
				}
			}
			list2.add(map);
		}
		stuSysAccessCountMap.put("sys_access_count", list1);
		stuSysAccessCountMap.put("stu_access_count", list2);
		//获取截止昨天的之前7天，10天，30天学生登录的次数，人数
		return stuSysAccessCountMap;
	}
}
