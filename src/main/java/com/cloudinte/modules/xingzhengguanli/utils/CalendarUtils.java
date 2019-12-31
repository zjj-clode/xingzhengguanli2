package com.cloudinte.modules.xingzhengguanli.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	* 得到本周周一
	* 
	* @return yyyy-MM-dd
	*/
	public static String getMondayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		
		return format.format(c.getTime());
	}
	
	/**
	* 得到本周周一
	* 
	* @return yyyy-MM-dd
	*/
	public static Date getMondayOfThisWeekDate() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		
		return c.getTime();
	}
	
	/**
	* 得到本周周日
	* 
	* @return yyyy-MM-dd
	*/
	public static String getSundayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		return format.format(c.getTime());
	}

	/**
	* 得到本周周日
	* 
	* @return yyyy-MM-dd
	*/
	public static Date getSundayOfThisWeekDate() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		return c.getTime();
	}
	
	
	/**
	* 得到本周周一
	* 
	* @return yyyy-MM-dd
	*/
	public static String getMondayOfThisWeek(Date date) {
		
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);  
		
		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = c.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        if(1 == dayWeek) {  
           c.add(Calendar.DAY_OF_MONTH, -1);  
        }  

       c.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  

       int day = c.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
       c.add(Calendar.DATE, c.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

		return format.format(c.getTime());
	}
	
	/**
	* 得到本周周日
	* 
	* @return yyyy-MM-dd
	*/
	public static String getSundayOfThisWeek(Date date) {
		
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);  
		
		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = c.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        if(1 == dayWeek) {  
           c.add(Calendar.DAY_OF_MONTH, -1);  
        }  

       c.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  

       int day = c.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
       c.add(Calendar.DATE, c.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        c.add(Calendar.DATE, 6);
		return format.format(c.getTime());
	}

}
