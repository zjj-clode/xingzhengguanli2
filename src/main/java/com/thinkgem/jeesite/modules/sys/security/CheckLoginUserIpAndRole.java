package com.thinkgem.jeesite.modules.sys.security;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IpRangeUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 需求：<br/>
 * 在某些时间段（如：国庆期间），禁止某些角色（如：内容发布管理员）的用户在某些IP（如：校外）登录<br/>
 *
 * @author lsp
 *
 */
@SuppressWarnings("unchecked")
public class CheckLoginUserIpAndRole implements OtherAuthorizingLogic {

	private final static Logger logger = LoggerFactory.getLogger(CheckLoginUserIpAndRole.class);

	private List<String> aclList = new ArrayList<String>();
	private List<String> timeRangeStrList = new ArrayList<String>();
	private Set<String> roles = Sets.newHashSet();
	private Set<String> loginNames = Sets.newHashSet();

	private List<Date[]> timeRangeList = new ArrayList<Date[]>();

	/**
	 * 角色
	 *
	 * @param roles
	 */
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	/**
	 * 账号
	 *
	 * @param loginNames
	 */
	public void setLoginNames(Set<String> loginNames) {
		this.loginNames = loginNames;
	}

	/**
	 * IP
	 *
	 * @param aclList
	 */
	public void setAclList(List<String> aclList) {
		this.aclList = aclList;
	}

	/**
	 * 时间
	 *
	 * @param timeRangeStrList
	 */
	public void setTimeRangeStrList(List<String> timeRangeStrList) {
		this.timeRangeStrList = timeRangeStrList;
	}

	private void init() {
		initAclList();
		initRoles();
		initLoginNames();
		initTimeRange();
	}

	/*
	20180808111111 - 20180809111111,
	20180808111111 - ,
	- 20180809111111
	*/
	/**
	 * 初始化时间列表
	 */
	private void initTimeRange() {

		if (timeRangeStrList == null) {
			timeRangeStrList = new ArrayList<String>();
		}
		try {
			String allowIpRangeStr = StringUtils.trimToEmpty(SettingsUtils.getSysConfig("CheckLoginUserIpAndRole.timeRange"));
			if (StringUtils.isNotBlank(allowIpRangeStr)) {
				timeRangeStrList.clear();
				List<String> items = StringUtils.split(allowIpRangeStr, new String[] { ",", "\n", "\r", "，" });
				for (String item : items) {
					if (StringUtils.isNotBlank(item)) {
						timeRangeStrList.add(item);
					}
				}
			}
			logger.debug("timeRangeStrList ------------> {}", timeRangeStrList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		timeRangeList.clear();
		for (String item : timeRangeStrList) {

			Date startDate = null;
			try {
				String startDateStr = StringUtils.trimToEmpty(StringUtils.substringBefore(item, "-"));
				if (StringUtils.isNotBlank(startDateStr)) {
					try {
						startDate = DateUtils.parseDate(startDateStr, "yyyyMMddHHmmss");
					} catch (ParseException e) {
						startDate = DateUtils.parseDate(startDateStr);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Date endDate = null;
			try {
				String endDateStr = StringUtils.trimToEmpty(StringUtils.substringAfter(item, "-"));
				if (StringUtils.isNotBlank(endDateStr)) {
					try {
						endDate = DateUtils.parseDate(endDateStr, "yyyyMMddHHmmss");
					} catch (ParseException e) {
						endDate = DateUtils.parseDate(endDateStr);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.debug("item:{},startDate:{},endDate:{}", item, startDate, endDate);
			timeRangeList.add(new Date[] { startDate, endDate });

		}

		logger.debug("timeRangeList ------------> {}", timeRangeList);

	}

	/**
	 * 初始化角色列表
	 */
	private void initRoles() {
		try {
			if (roles == null) {
				roles = new HashSet<String>();
			}
			String rolesStr = StringUtils.trimToEmpty(SettingsUtils.getSysConfig("CheckLoginUserIpAndRole.roles"));
			if (StringUtils.isNotBlank(rolesStr)) {
				roles.clear();
				List<String> items = StringUtils.split(rolesStr, new String[] { ",", "\n", "\r", "，" });
				for (String item : items) {
					if (StringUtils.isNotBlank(item)) {
						roles.add(item);
					}
				}
			}
			logger.debug("roles ------------> {}", roles);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 初始化账号列表
	 */
	private void initLoginNames() {
		try {
			if (loginNames == null) {
				loginNames = new HashSet<String>();
			}
			String loginNamesStr = StringUtils.trimToEmpty(SettingsUtils.getSysConfig("CheckLoginUserIpAndRole.loginNames"));
			if (StringUtils.isNotBlank(loginNamesStr)) {
				loginNames.clear();
				List<String> items = StringUtils.split(loginNamesStr, new String[] { ",", "\n", "\r", "，" });
				for (String item : items) {
					if (StringUtils.isNotBlank(item)) {
						loginNames.add(item);
					}
				}
			}
			logger.debug("loginNames ------------> {}", loginNames);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 初始化访问控制列表
	 */
	private void initAclList() {
		try {
			if (aclList == null) {
				aclList = new ArrayList<String>();
			}
			String allowIpRangeStr = StringUtils.trimToEmpty(SettingsUtils.getSysConfig("CheckLoginUserIpAndRole.aclList"));
			if (StringUtils.isNotBlank(allowIpRangeStr)) {
				aclList.clear();
				List<String> items = StringUtils.split(allowIpRangeStr, new String[] { ",", "\n", "\r", "，" });
				for (String item : items) {
					if (StringUtils.isNotBlank(item)) {
						aclList.add(item);
					}
				}
			}
			logger.debug("aclList ------------> {}", aclList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取客户端IP
	 */
	private String getIpAddress(HttpServletRequest request) {
		/*
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			if (ip.indexOf(",") != -1) {
				ip = ip.split(",")[0];
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			logger.debug("Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			logger.debug("WL-Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			logger.debug("HTTP_CLIENT_IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			logger.debug("HTTP_X_FORWARDED_FOR ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
			logger.debug("X-Real-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			logger.debug("getRemoteAddr ip: " + ip);
		}
		return ip;
		*/

		return StringUtils.getRemoteAddr(request);
	}

	/**
	 *
	 * 注：拒绝IP列表优先级大于允许IP列表。<br/>
	 * 1、如果配置了拒绝IP列表。如果客户端IP属于此列表，拒绝并返回。<br/>
	 * 2、如果配置了允许IP列表。如果客户端IP不属于此列表，拒绝并返回。<br/>
	 * 3、其他情况允许。<br/>
	 */
	private boolean isPermittedIp(String ip) {

		if (StringUtils.isBlank(ip)) {
			return true;
		}

		if (ip.indexOf(':') != -1) {
			return "0:0:0:0:0:0:0:1".equals(ip); // IPV6本机地址
		}

		return checkAcl(aclList, ip, SettingsUtils.getSysConfig("CheckLoginUserIpAndRole.notInAclList.defaultValue", true));

	}

	/**
	 * 检测ip是被ACL允许（true）还是被拒绝（false）
	 *
	 * @param aclList
	 *            访问控制列表
	 * @param ip
	 *            被检测ip
	 * @param defaultValue
	 *            不属ACL控制时返回的值
	 * @return
	 */
	private boolean checkAcl(List<String> aclList, String ip, boolean defaultValue) {

		if (!Collections3.isEmpty(aclList)) {

			for (String acl : aclList) {

				// permit 192.168.0.2
				// deny *

				acl = StringUtils.trimToEmpty(StringUtils.lowerCase(acl));

				if (StringUtils.isBlank(acl)) {
					continue;
				}

				if (StringUtils.startsWith(acl, "permit")) {

					String permitIpRange = StringUtils.trimToEmpty(StringUtils.substringAfter(acl, "permit"));

					if (StringUtils.isBlank(permitIpRange)) {
						continue;
					}

					if ("*".equals(permitIpRange) || "any".equals(permitIpRange) || "all".equals(permitIpRange)) {
						// permitIpRange = "*.*.*.*";
						return true;
					}

					try {

						boolean isInRanges = IpRangeUtils.isInRanges(ip, permitIpRange);
						logger.debug("isInRanges --> {}, ip --> {}, permitIpRange --> {}", isInRanges, ip, permitIpRange);

						if (isInRanges) {
							return true;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (StringUtils.startsWith(acl, "deny")) {

					String denyIpRange = StringUtils.trimToEmpty(StringUtils.substringAfter(acl, "deny"));

					if (StringUtils.isBlank(denyIpRange)) {
						continue;
					}

					if ("*".equals(denyIpRange) || "any".equals(denyIpRange) || "all".equals(denyIpRange)) {
						// denyIpRange = "*.*.*.*";
						return false;
					}

					try {

						boolean isInRanges = IpRangeUtils.isInRanges(ip, denyIpRange);
						logger.debug("isInRanges --> {}, ip --> {}, denyIpRange --> {}", isInRanges, ip, denyIpRange);

						if (isInRanges) {
							return false;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		}

		// 不匹配任何acl时，返回默认值
		return defaultValue;
	}

	private boolean isOpenTime() {
		if (!Collections3.isEmpty(timeRangeList)) {
			Date date = new Date();
			for (Date[] dates : timeRangeList) {
				Date startDate = dates[0];
				Date endDate = dates[1];
				boolean isInTime = isInTime(date, startDate, endDate);
				logger.debug("isInTime : {}, date : {}, startDate : {}, endDate : {}", isInTime, date, startDate, endDate);
				if (isInTime) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isInTime(Date date, Date startDate, Date endDate) {
		if (date == null || startDate == null && endDate == null) {
			return false;
		}
		// 未开始
		if (startDate != null && startDate.after(date)) {
			return false;
		}
		// 已结束
		if (endDate != null && endDate.before(date)) {
			return false;
		}
		return true;
	}

	@Override
	public void beforeDoGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		logger.debug("beforeDoGetAuthenticationInfo ......");
	}

	@Override
	public void afterDoGetAuthenticationInfo(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo, User user) throws AuthenticationException {
		logger.debug("afterDoGetAuthenticationInfo.......");

		boolean enable = SettingsUtils.getSysConfig("CheckLoginUserIpAndRole.enable", true);
		logger.debug("CheckLoginUserIpAndRole.enable : {}", enable);
		if (!enable) {
			return;
		}

		if (user == null) {
			return;
		}

		// 每次重新加载新的配置
		init();

		// 角色和账号都未配置
		if (Collections3.isEmpty(roles) && Collections3.isEmpty(loginNames)) {
			return;
		}
		// 时间段未配置
		if (Collections3.isEmpty(timeRangeList)) {
			return;
		}
		// 当前时间不在限制时间段
		if (isOpenTime()) {
			return;
		}

		// 是否匹配 - 角色或者账号
		boolean flag = false;
		List<Role> roleList = user.getRoleList(); // 用户拥有的角色
		logger.debug("roleList : {}", roleList);
		if (!Collections3.isEmpty(roleList)) {
			List<String> roleEnnameList = Collections3.extractToList(roleList, "enname"); // 比较的是enname
			logger.debug("roleEnnameList : {}", roleEnnameList);
			if (!Collections3.isEmpty(roles)) {
				for (String role : roles) {
					boolean hasRole = roleEnnameList.contains(role);
					logger.debug("hasRole({}) : {}", role, hasRole);
					if (hasRole) {
						flag = true;
						break;
					}
				}
			}
		}
		if (!flag) {
			if (!Collections3.isEmpty(loginNames)) {
				boolean hasLoginName = loginNames.contains(user.getLoginName());
				logger.debug("hasLoginName({}) : {}", user.getLoginName(), hasLoginName);
				if (hasLoginName) {
					flag = true;
				}
			}
		}
		// 角色和账号都没有包含此用户
		if (!flag) {
			return;
		}

		//
		String ip = getIpAddress(Servlets.getRequest());
		if (!isPermittedIp(ip)) {
			logger.debug("您所在IP被禁止登录系统");
			throw new AuthenticationException("msg:" + SettingsUtils.getSysConfig("CheckLoginUserIpAndRole.msg.forbid", "您所在IP被禁止登录系统"));
		}
	}

}
