package com.thinkgem.jeesite.modules.sys.security;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 登出，继承{@link org.apache.shiro.web.filter.authc.LogoutFilter}，实现自定义登出后跳转页面
 *
 * @author 廖水平
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, String> userTypeRedirectUrlMap = Maps.newHashMap();

	/**
	 * 用户退出登录时跳转的URL<br/>
	 *
	 * @param userTypeRedirectUrlMap
	 *            Map<用户类型, 退出时跳转的URL>
	 */
	public void setUserTypeRedirectUrlMap(Map<String, String> userTypeRedirectUrlMap) {
		this.userTypeRedirectUrlMap = userTypeRedirectUrlMap;
	}

	/**
	 * 重写，从配置中读取：退出登录后跳转的url
	 */
	@Override
	protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
		String redirectUrl = null;
		User user = UserUtils.getUser();
		if (user != null) {
			String userType = user.getUserType();
			if (!StringUtils.isBlank(userType)) {
				if (userTypeRedirectUrlMap != null && !userTypeRedirectUrlMap.isEmpty()) {
					redirectUrl = userTypeRedirectUrlMap.get(userType);
					if (!StringUtils.isBlank(redirectUrl)) {
						logger.debug("userType={}，redirectUrl={}", userType, redirectUrl);
						return redirectUrl;
					}
				}
			}
		}
		redirectUrl = super.getRedirectUrl();
		if (StringUtils.isBlank(redirectUrl)) {
			redirectUrl = LogoutFilter.DEFAULT_REDIRECT_URL;
		}
		return redirectUrl;
	}
}
