package com.thinkgem.jeesite.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTool {
	// 用来全局控制 上一周，本周，下一周的周数变化
	private static int weeks = 0;
	private static int MaxDate;// 一月最大天数

	/**
	 * 获得本周星期日的日期
	 */
	public static String getCurrentWeekday() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得当前日期与本周日相差的天数
	 */
	public static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	/**
	 * 获得本周一的日期
	 */
	public static String getMondayOFWeek() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得相应周的周六的日期
	 */
	public static String getSaturday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得上周星期日的日期
	 */
	public static String getPreviousWeekSunday() {
		weeks = 0;
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得上周星期一的日期
	 */
	public static String getPreviousWeekday() {
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得下周星期一的日期
	 */
	public static String getNextMonday() {
		weeks++;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得下周星期日的日期
	 */
	public static String getNextSunday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	public static int getMonthPlus() {
		Calendar cd = Calendar.getInstance();
		int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
		cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		MaxDate = cd.get(Calendar.DATE);
		if (monthOfNumber == 1) {
			return -MaxDate;
		} else {
			return 1 - monthOfNumber;
		}
	}

	/**
	 * 获得本年有多少天
	 */
	public static int getDaysOfThisYear() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		return MaxYear;
	}

	/**
	 * 今天零时 2011-01-01 00:00:00
	 */
	public static Date getThisDayFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		return calendar.getTime();
	}

	/**
	 * 今天最后一秒 2011-01-01 23:59:59
	 */
	public static Date getThisDayLastSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
				23, 59, 59);
		return calendar.getTime();
	}

	/**
	 * 昨日零时
	 */
	public static Date getLastDayFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getThisDayFirstSecond());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 明天零时
	 */
	public static Date getNextDayFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getThisDayFirstSecond());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 本周开始第一天零时
	 */
	public static Date getThisWeekFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, MaxDate);
		calendar.set(Calendar.SECOND, MaxDate);
		return calendar.getTime();
	}

	/**
	 * 上周开始第一天零时
	 */
	public static Date getLastWeekFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getThisWeekFirstSecond());
		calendar.add(Calendar.DAY_OF_WEEK, -7);
		return calendar.getTime();
	}

	/**
	 * 下周开始第一天零时
	 */
	public static Date getNextWeekFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getThisWeekFirstSecond());
		calendar.add(Calendar.DAY_OF_WEEK, 7);
		return calendar.getTime();
	}

	/**
	 * 获取本月1号零时
	 */
	public static Date getThisMonthFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		//设置时间的日为1，时、分、秒都为0 ，年、月不变。等于把时间调到这个月的1号零点
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
		return calendar.getTime(); // 这个月的1号零点
	}

	/**
	 * 获取本月最后一秒
	 */
	public static Date getThisMonthLastSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getThisMonthFirstSecond());//时间设为本月1号零时
		calendar.add(Calendar.MONTH, 1);// 月数加一，调到下月1号零时
		calendar.add(Calendar.SECOND, -1);//秒减一
		return calendar.getTime(); // 这个月的1号零点
	}

	/**
	 * 获取上月1号零时
	 */
	public static Date getLastMonthFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getThisMonthFirstSecond());//时间设为本月1号零时
		calendar.add(Calendar.MONTH, -1); // 月数减一，调到上一个月
		return calendar.getTime();
	}

	/**
	 * 获取下月1号零时
	 */
	public static Date getNextMonthFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getThisMonthFirstSecond());//时间设为本月1号零时
		calendar.add(Calendar.MONTH, 1);// 月数加一，调到下一个月
		return calendar.getTime();
	}

	/**
	 * 上季度第一天零时
	 */
	public static Date getLastQuarterFirstSecond() {
		return getLastQuarterFirstSecondOfDate(new Date());
	}

	/**
	 * 本季度第一天零时
	 */
	public static Date getThisQuarterFirstSecond() {
		return getThisQuarterFirstSecondOfDate(new Date());
	}

	/**
	 * 本季度最后一秒
	 */
	public static Date getThisQuarterLastSecond() {
		return getThisQuarterLastSecondOfDate(new Date());
	}

	/**
	 * 下季度第一天零时
	 */
	public static Date getNextQuarterFirstSecond() {
		return getNextQuarterFirstSecondOfDate(new Date());
	}

	/**
	 * 去年年初第一天零时 2011-01-01 00:00:00
	 */
	public static Date getLastYearFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0); //本年1月1号零点
		calendar.add(Calendar.YEAR, -1); //减少一年
		return calendar.getTime();
	}

	/**
	 * 本年年初第一天零时 2011-01-01 00:00:00
	 */
	public static Date getThisYearFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0); //本年1月1号零点
		return calendar.getTime();
	}

	/**
	 * 本年最后一秒
	 */
	public static Date getThisYearLastSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), 11, 31, 23, 59, 59); //本年最后一秒
		return calendar.getTime();
	}

	/**
	 * 下一年年初第一天零时 2012-01-01 00:00:00
	 */
	public static Date getNextYearFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0); //本年1月1号零点
		calendar.add(Calendar.YEAR, 1); //加一年
		return calendar.getTime();
	}

	/**
	 * 获取某年某月的天数
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月 某月的天数
	 */
	public static int getDaysOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 *            年
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	public static Date getThisQuarterFirstSecondOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int quarter = getQuarter(date);
		switch (quarter) {
			case 1:
				calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);//1月1号零时
				break;
			case 2:
				calendar.set(calendar.get(Calendar.YEAR), 3, 1, 0, 0, 0);//3月1号零时
				break;
			case 3:
				calendar.set(calendar.get(Calendar.YEAR), 6, 1, 0, 0, 0);//6月1号零时
				break;
			case 4:
				calendar.set(calendar.get(Calendar.YEAR), 9, 1, 0, 0, 0);//9月1号零时
				break;
			default:
				break;
		}
		return calendar.getTime();
	}

	public static Date getLastQuarterFirstSecondOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int quarter = getQuarter(date);
		switch (quarter) {
			case 1://当前季度是1，上一季度的开始时：去年10月1号零时
				calendar.add(Calendar.YEAR, -1);
				calendar.set(calendar.get(Calendar.YEAR), 9, 1, 0, 0, 0);
				break;
			case 2://当前季度是2，上一季度的开始时：今年1月1号零时
				calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
				break;
			case 3://当前季度是3，上一季度的开始时：今年4月1号零时
				calendar.set(calendar.get(Calendar.YEAR), 3, 1, 0, 0, 0);
				break;
			case 4://当前季度是4，上一季度的开始时：今年7月1号零时
				calendar.set(calendar.get(Calendar.YEAR), 6, 1, 0, 0, 0);
				break;
			default:
				break;
		}
		return calendar.getTime();
	}

	public static Date getNextQuarterFirstSecondOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int quarter = getQuarter(date);
		switch (quarter) {
			case 1:
				calendar.set(calendar.get(Calendar.YEAR), 3, 1, 0, 0, 0);//4月1号零时
				break;
			case 2:
				calendar.set(calendar.get(Calendar.YEAR), 6, 1, 0, 0, 0);//7月1号零时
				break;
			case 3:
				calendar.set(calendar.get(Calendar.YEAR), 9, 1, 0, 0, 0);//10月1号零时
				break;
			case 4:
				calendar.add(Calendar.YEAR, 1);
				calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);//下一年的1月1号零时
				break;
			default:
				break;
		}
		return calendar.getTime();
	}

	/**
	 * 日期所在季度的最后一秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date getThisQuarterLastSecondOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int quarter = getQuarter(date);
		switch (quarter) {
			case 1:
				calendar.set(calendar.get(Calendar.YEAR), 2, 31, 23, 59, 59);//3月31日23时59分59秒
				break;
			case 2:
				calendar.set(calendar.get(Calendar.YEAR), 5, 30, 23, 59, 59);//6月30日23时59分59秒
				break;
			case 3:
				calendar.set(calendar.get(Calendar.YEAR), 7, 30, 23, 59, 59);//9月30日23时59分59秒
				break;
			case 4:
				calendar.set(calendar.get(Calendar.YEAR), 11, 31, 23, 59, 59);//12月31日23时59分59秒
				break;
			default:
				break;
		}
		return calendar.getTime();
	}

	/**
	 * 当前时间是第几个季度
	 */
	public static int getQuarter(Date date) {
		int quarter = -1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);//月份，从0开始。
		if (month >= 0 && month <= 2) {
			quarter = 1;
		} else if (month >= 3 && month <= 5) {
			quarter = 2;
		} else if (month >= 6 && month <= 8) {
			quarter = 3;
		} else if (month >= 9 && month <= 11) {
			quarter = 4;
		}
		return quarter;
	}

	public static String datyToString(Date date) {
		String ret = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar calendarToday = Calendar.getInstance();
		calendarToday.setTime(new Date());
		int n = calendarToday.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
		switch (n) {
			case 0:
				ret = "今天";
				break;
			case -1:
				ret = "明天";
				break;
			case -2:
				ret = "后天";
				break;
			case -3:
				ret = "大后天";
				break;
			case 1:
				ret = "昨天";
				break;
			case 2:
				ret = "前天";
				break;
			case 3:
				ret = "大前天";
				break;
			default:
				ret = new SimpleDateFormat("yyyy年M月d日").format(date);
				break;
		}
		return ret;
	}

	/**
	 * 
	 * @param date
	 * @return 周日 ,周一,...周六
	 */
	public static String getWeekStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		switch (week) {
			case 1:
				return "周日";
			case 2:
				return "周一";
			case 3:
				return "周二";
			case 4:
				return "周三";
			case 5:
				return "周四";
			case 6:
				return "周五";
			case 7:
			default:
				return "周六";
		}
	}

	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println(getLastWeekFirstSecond());
		//System.out.println(getThisWeekFirstSecond());
		//System.out.println(datyToString(getLastWeekFirstSecond()));
		//System.out.println(datyToString(getThisWeekFirstSecond()));
	}
}
