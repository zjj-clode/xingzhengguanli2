package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 访问信息记录模块Entity
 * @author gyl
 * @version 2019-03-09
 */
public class GuestAccess extends DataEntity<GuestAccess> {
	private static final long serialVersionUID = 1L;

	private String sessionId;//session id// 会话标识
	//时间
	private String loginYear;//2016// 访问年份
	private String loginYearMonth;//2016-01// 访问年月
	private String loginYearMonthDay;//2016-01-01// 访问日期
	private Date loginDateTime;//2016-01-09 10:52:56// 访问开始时间
	private Date logoutDateTime;//2016-01-09 10:53:56// 访问结束时间
	private String duration;// 访问时长（秒）
	private Date lastAccessTime;//2016-01-09 10:53:56// 最后操作时间
	//获取ip信息（国家country、省region、市city）
	private String url;// 访问地址
	private String ip;// ip地址
	private String country;// 国家
	private String region;// 省
	private String city;// 城市
	private String area;// 地区
	//用户设备信息
	private String browser;// 浏览器
	private String browserVersion;// 浏览器版本
	private String os;// 操作系统
	private String deviceType;// 访问设备
	private String manufacturer;// 设备制造商
	//request中header信息
		private String headerInfo;// 访问信息
	//是否在线
	private String online;// 是否在线
	//登录用户信息
	private String loginName;//账号
	private String name;//姓名
	private String depId;//部门id
	private String depName;//部门名称
	//1 PC 、2 Android APP、3 IOS APP、4 微信   、5其他
	private String platform;// 客户端
	//查询
	private Date beginLoginDateTime;		// 开始 访问开始时间
	private Date endLoginDateTime;		// 结束 访问开始时间
	
	private Date loginDateTimeStart;
	private Date loginDateTimeEnd;
	private Date logoutDateTimeStart;
	private Date logoutDateTimeEnd;

	//
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public void processEmptyArrayParam() {
		if (ids != null && ids.length < 1) {
			ids = null;
		}
	}
	
	/**
	 *  访问信息记录模块
	 */
	public GuestAccess() {
		super();
	}
	
	/**
	 *  访问信息记录模块
	 * @param id
	 */
	public GuestAccess(String id){
		super(id);
	}
	
	@Length(min=0, max=40, message="会话标识长度必须介于 0 和 40 之间")
//	@ExcelField(title="会话标识", align=2, sort=1 )
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Length(min=0, max=4, message="访问年份长度必须介于 0 和 4 之间")
//	@ExcelField(title="访问年份", align=2, sort=2 )
	public String getLoginYear() {
		return loginYear;
	}

	public void setLoginYear(String loginYear) {
		this.loginYear = loginYear;
	}
	
	@Length(min=0, max=7, message="访问年月长度必须介于 0 和 7 之间")
//	@ExcelField(title="访问年月", align=2, sort=3 )
	public String getLoginYearMonth() {
		return loginYearMonth;
	}

	public void setLoginYearMonth(String loginYearMonth) {
		this.loginYearMonth = loginYearMonth;
	}
	
	@Length(min=0, max=10, message="访问日期长度必须介于 0 和 10 之间")
//	@ExcelField(title="访问日期", align=2, sort=4 )
	public String getLoginYearMonthDay() {
		return loginYearMonthDay;
	}

	public void setLoginYearMonthDay(String loginYearMonthDay) {
		this.loginYearMonthDay = loginYearMonthDay;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="访问开始时间", align=2, sort=5 )
	public Date getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="访问结束时间", align=2, sort=6 )
	public Date getLogoutDateTime() {
		return logoutDateTime;
	}

	public void setLogoutDateTime(Date logoutDateTime) {
		this.logoutDateTime = logoutDateTime;
	}
	
	@Length(min=0, max=10, message="访问时长（秒）长度必须介于 0 和 10 之间")
	@ExcelField(title="时长", align=2, sort=7 )
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后操作时间", align=2, sort=8 )
	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
	
	@Length(min=0, max=200, message="访问地址长度必须介于 0 和 200 之间")
	@ExcelField(title="访问地址", align=2, sort=9 )
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=0, max=40, message="ip地址长度必须介于 0 和 40 之间")
	@ExcelField(title="IP地址", align=2, sort=10 )
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Length(min=0, max=20, message="国家长度必须介于 0 和 20 之间")
	@ExcelField(title="国家", align=2, sort=11 )
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Length(min=0, max=20, message="省长度必须介于 0 和 20 之间")
	@ExcelField(title="省", align=2, sort=12 )
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	@Length(min=0, max=20, message="城市长度必须介于 0 和 20 之间")
	@ExcelField(title="市", align=2, sort=13 )
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=20, message="地区长度必须介于 0 和 20 之间")
//	@ExcelField(title="地区", align=2, sort=14 )
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	@Length(min=0, max=20, message="浏览器长度必须介于 0 和 20 之间")
	@ExcelField(title="浏览器", align=2, sort=15 )
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	@Length(min=0, max=20, message="浏览器版本长度必须介于 0 和 20 之间")
	@ExcelField(title="浏览器版本", align=2, sort=16 )
	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	
	@Length(min=0, max=20, message="操作系统长度必须介于 0 和 20 之间")
	@ExcelField(title="操作系统", align=2, sort=17 )
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}
	
	@Length(min=0, max=20, message="访问设备长度必须介于 0 和 20 之间")
	@ExcelField(title="访问设备", align=2, sort=18 )
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	@Length(min=0, max=20, message="设备制造商长度必须介于 0 和 20 之间")
	@ExcelField(title="系统制造商", align=2, sort=19 )
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	@Length(min=0, max=800, message="访问信息长度必须介于 0 和 800 之间")
//	@ExcelField(title="访问信息", align=2, sort=20 )
	public String getHeaderInfo() {
		return headerInfo;
	}

	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}
	
	@Length(min=0, max=1, message="是否在线长度必须介于 0 和 1 之间")
	@ExcelField(title="状态", align=2, sort=21 ,dictType="is_online")
	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}
	
	@Length(min=0, max=50, message="账号长度必须介于 0 和 50 之间")
//	@ExcelField(title="账号", align=2, sort=22 )
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Length(min=0, max=50, message="姓名长度必须介于 0 和 50 之间")
//	@ExcelField(title="姓名", align=2, sort=23 )
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=64, message="单位id长度必须介于 0 和 64 之间")
//	@ExcelField(title="单位id", align=2, sort=24 )
	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	@Length(min=0, max=50, message="单位名称长度必须介于 0 和 50 之间")
//	@ExcelField(title="单位名称", align=2, sort=25 )
	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	@Length(min=0, max=20, message="客户端长度必须介于 0 和 20 之间")
//	@ExcelField(title="客户端", align=2, sort=26 )
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public Date getBeginLoginDateTime() {
		return beginLoginDateTime;
	}

	public void setBeginLoginDateTime(Date beginLoginDateTime) {
		this.beginLoginDateTime = beginLoginDateTime;
	}
	
	public Date getEndLoginDateTime() {
		return endLoginDateTime;
	}

	public void setEndLoginDateTime(Date endLoginDateTime) {
		this.endLoginDateTime = endLoginDateTime;
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
	
	@Override
	public String toString() {
		return "GuestAccess [sessionId=" + sessionId + ", loginYear=" + loginYear + ", loginYearMonth=" + loginYearMonth
				+ ", loginYearMonthDay=" + loginYearMonthDay + ", loginDateTime=" + loginDateTime + ", logoutDateTime="
				+ logoutDateTime + ", duration=" + duration + ", lastAccessTime=" + lastAccessTime + ", url=" + url
				+ ", ip=" + ip + ", country=" + country + ", region=" + region + ", city=" + city + ", area=" + area
				+ ", browser=" + browser + ", browserVersion=" + browserVersion + ", os=" + os + ", deviceType="
				+ deviceType + ", manufacturer=" + manufacturer + ", headerInfo=" + headerInfo + ", online=" + online
				+ ", loginName=" + loginName + ", name=" + name + ", depId=" + depId + ", depName=" + depName
				+ ", platform=" + platform + ", loginDateTimeStart=" + loginDateTimeStart + ", loginDateTimeEnd="
				+ loginDateTimeEnd + ", logoutDateTimeStart=" + logoutDateTimeStart + ", logoutDateTimeEnd="
				+ logoutDateTimeEnd + "]";
	}
}
