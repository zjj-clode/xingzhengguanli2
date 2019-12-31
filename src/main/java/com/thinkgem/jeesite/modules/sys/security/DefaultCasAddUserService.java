package com.thinkgem.jeesite.modules.sys.security;

import java.util.Map;

import org.apache.shiro.cas.CasAuthenticationException;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 默认实现：不创建用户抛出异常提示本系统没有这个账号，不让登录。
 *
 * @author lsp
 *
 */
@Service
public class DefaultCasAddUserService implements CasAddUserService {

	@Override
	public User addUser(Map<String, Object> attributes, String userId) throws CasAuthenticationException {
		throw new CasAuthenticationException("本系统不存在您的账号");
	}

}
