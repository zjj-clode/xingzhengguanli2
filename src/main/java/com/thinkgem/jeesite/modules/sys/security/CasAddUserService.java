package com.thinkgem.jeesite.modules.sys.security;

import java.util.Map;

import org.apache.shiro.cas.CasAuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 根据CAS统一身份认证服务器返回的认证数据（具体是哪些数据由CAS服务器决定），在本系统中创建新的用户.<br/>
 * 默认实现： {@link DefaultCasAddUserService}
 *
 * @author lsp
 *
 */
public interface CasAddUserService {
	/**
	 * 根据CAS统一身份认证服务器返回的认证数据（具体是哪些数据由CAS服务器决定），在本系统中创建新的用户.<br/>
	 * 默认行为是，不创建用户抛出异常提示本系统没有这个账号，不让登录。子类根据需要覆盖此方法。
	 *
	 * @param attributes
	 *            CAS服务器返回的认证数据
	 * @param userId
	 *            用户账号
	 * @return 新创建的用户对象
	 * @throws CasAuthenticationException
	 *             cas认证失败信息
	 */
	@Transactional(readOnly = false)
	User addUser(Map<String, Object> attributes, String userId) throws CasAuthenticationException;
}
