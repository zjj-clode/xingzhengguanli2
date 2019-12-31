package com.thinkgem.jeesite.modules.sys.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 额外的认证规则。
 *
 * @author lsp
 *
 */
public interface OtherAuthorizingLogic {

	/**
	 * 在 doGetAuthenticationInfo() 方法中最先执行的逻辑
	 *
	 * @param authenticationToken
	 * @throws AuthenticationException
	 */
	void beforeDoGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException;

	/**
	 * 在 doGetAuthenticationInfo() 方法中最后（reurn authenticationInfo 之前）执行的逻辑
	 *
	 * @param authenticationToken
	 * @param authenticationInfo
	 * @param user
	 * @throws AuthenticationException
	 */
	void afterDoGetAuthenticationInfo(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo, User user) throws AuthenticationException;

}
