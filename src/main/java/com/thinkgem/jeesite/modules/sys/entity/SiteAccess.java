package com.thinkgem.jeesite.modules.sys.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 在线用户、站点访问
 * 
 * @author 廖水平
 */
public class SiteAccess extends DataEntity<SiteAccess> {
	private static final long serialVersionUID = 46177437404002461L;

	private String sessionId;//session id
	//时间
	private String loginYear;//2016
	private String loginYearMonth;//2016-01
	private String loginYearMonthDay;//2016-01-01
	private Date loginDateTime;//2016-01-09 10:52:56
	private Date logoutDateTime;//2016-01-09 10:53:56
	//登录用户信息
	private String loginName;//账号
	private String name;//姓名
	private String depId;//部门id
	private String depName;//部门名称
	//ip
	private String ip;
	//http://www.helloweba.com/view-blog-190.html
	//http://ip.taobao.com/service/getIpInfo.php?ip=202.205.88.66
	//获取ip信息接口（国家country、地区area、省region、市city、县county、网络提供商isp）
	private String country;
	private String area;
	private String region;
	private String city;
	//1 PC 、2 Android APP、3 IOS APP、4 微信   、5其他
	private String platform;
	private String online;//是否在线
	//查询
	private Date loginDateTimeStart;
	private Date loginDateTimeEnd;
	private Date logoutDateTimeStart;
	private Date logoutDateTimeEnd;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLoginYear() {
		return loginYear;
	}

	public void setLoginYear(String loginYear) {
		this.loginYear = loginYear;
	}

	public String getLoginYearMonth() {
		return loginYearMonth;
	}

	public void setLoginYearMonth(String loginYearMonth) {
		this.loginYearMonth = loginYearMonth;
	}

	public Date getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	public Date getLogoutDateTime() {
		return logoutDateTime;
	}

	public void setLogoutDateTime(Date logoutDateTime) {
		this.logoutDateTime = logoutDateTime;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public Date getLoginDateTimeStart() {
		return loginDateTimeStart;
	}

	public void setLoginDateTimeStart(Date loginDateTimeStart) {
		this.loginDateTimeStart = loginDateTimeStart;
	}

	public Date getLoginDateTimeEnd() {
		return loginDateTimeEnd;
	}

	public void setLoginDateTimeEnd(Date loginDateTimeEnd) {
		this.loginDateTimeEnd = loginDateTimeEnd;
	}

	public Date getLogoutDateTimeStart() {
		return logoutDateTimeStart;
	}

	public void setLogoutDateTimeStart(Date logoutDateTimeStart) {
		this.logoutDateTimeStart = logoutDateTimeStart;
	}

	public Date getLogoutDateTimeEnd() {
		return logoutDateTimeEnd;
	}

	public void setLogoutDateTimeEnd(Date logoutDateTimeEnd) {
		this.logoutDateTimeEnd = logoutDateTimeEnd;
	}

	public String getLoginYearMonthDay() {
		return loginYearMonthDay;
	}

	public void setLoginYearMonthDay(String loginYearMonthDay) {
		this.loginYearMonthDay = loginYearMonthDay;
	}

}
