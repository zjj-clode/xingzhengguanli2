/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = { "yyyy", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
			"yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获得当前年
	 * @return
	 */
	public static int getYearInt()
	{
		Calendar now = Calendar.getInstance();  
		return  now.get(Calendar.YEAR);
		
	}
	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
	 * "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
	 * "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的年数
	 * 
	 * @param String
	 * @return
	 */
	public static String pastYears(String year) {
		int now = Integer.parseInt(getYear());
		try {
			int yearI = Integer.parseInt(year.substring(0, 4));
			return String.valueOf(now - yearI);
		} catch (Exception e) {
			return year;
		}
	}
	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		if (date != null) {
			long t = new Date().getTime() - date.getTime();
			return t / (60 * 1000);
		} else
			return 9999;
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = timeMillis / (60 * 60 * 1000) - day * 24;
		long min = timeMillis / (60 * 1000) - day * 24 * 60 - hour * 60;
		long s = timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
		long sss = timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000;
		return (day > 0 ? day + "," : "") + hour + "小时" + min + "分" + s + "秒" + sss+"毫秒";
	}

	public static String formatDateTimeCn(long timeMillis) {
		/////System.out.println("timeMillis==>" + timeMillis);
		long day = timeMillis / (24 * 60 * 60 * 1000);
		////System.out.println("day==>" + day);
		long hour = timeMillis / (60 * 60 * 1000) - day * 24;
		////System.out.println("hour==>" + hour);
		long min = timeMillis / (60 * 1000) - day * 24 * 60 - hour * 60;
		////System.out.println("min==>" + min);
		//long s = timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
		//long sss = timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000;
		if (day > 7) {
			return "超过一星期";
		}
		return (day > 0 ? day + "天" : "") + (hour > 0 ? hour + "小时" : "") + min + "分钟";//+ s + "秒" + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 根据日期获取星期几
	 * 
	 * @param date
	 * @return author LEE
	 */
	public static String getWeek(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		String week = sdf.format(date);
		return week;
	}

	/**
	 * 根据日期获取--年--月--日
	 * 
	 * @param date
	 * @return author LEE
	 */
	public static String getYearMonthDayZH(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String da = simpleDateFormat.format(date);
		String y = da.substring(0, 4);
		String m = da.substring(4, 6);
		String d = da.substring(6, 8);
		return y + "年" + m + "月" + d + "日";
	}

	public static String getYearMonthDayZH_(Date date) {
		return new SimpleDateFormat("yyyy年MM月dd日").format(date);
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		//		//System.out.println(formatDate(parseDate("2010/3/6")));
		//		//System.out.println(getDate("yyyy年MM月dd日 E"));
		//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
		//		//System.out.println(time/(24*60*60*1000));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		long start = calendar.getTimeInMillis();
		//System.out.println("开始：" + start);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		calendar.add(Calendar.HOUR_OF_DAY, +2);
		calendar.add(Calendar.MINUTE, +3);
		long end = calendar.getTimeInMillis();
		//System.out.println("结束：" + end);
		//System.out.println("相差：" + (end - start) + "毫秒");
		//System.out.println("换算为：" + formatDateTimeCn(Math.abs(end - start)));

		//System.out.println(formatDateTimeCn(138105 * 60000L));

		//System.out.println(getDistanceOfTwoDate(parseDate("2011/3/6"), parseDate("2010/3/6")));

		///////////////////

		List<Date> dates = new ArrayList<>();
		Date beginRiqi = parseDate("2010/3/6");
		Date endRiqi = parseDate("2016/12/7");

		Calendar c = Calendar.getInstance();
		c.setTime(endRiqi);
		//System.out.println(c.get(Calendar.DAY_OF_WEEK));

		if (beginRiqi != null && endRiqi != null && !beginRiqi.after(endRiqi)) {
			if (beginRiqi.equals(endRiqi)) {
				dates.add(endRiqi);
			} else {
				//计算几个日
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(endRiqi);
				Calendar beginCalendar = Calendar.getInstance();
				beginCalendar.setTime(beginRiqi);
				while (!endCalendar.before(beginCalendar)) {
					dates.add(endCalendar.getTime());
					endCalendar.add(Calendar.DATE, -1);
				}
			}
		}
		for (Date date : dates) {
			//System.out.println(DateUtils.formatDateTime(date));
		}

	}
	
	
}
