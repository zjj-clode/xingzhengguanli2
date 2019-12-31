package com.thinkgem.jeesite.modules.sys.security;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 配置多个realm时，只执行支持当前AuthenticationToken的realm，防止抛异常但获取不到具体的异常信息，没法给前台具体的提示信息。<br/>
 * SystemAuthorizingRealm处理UsernamePasswordToken<br/>
 * CasRealm处理CasToken<br/>
 * 等等......
 *
 * @author 廖水平
 *
 */
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {
	
	//private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 先过滤一下，只执行支持的realm
	 */
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
		try {
			assertRealmsConfigured();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		Collection<Realm> realms = getRealms();
		/////////////////////////////////////////////////////////
		List<Realm> list = Lists.newArrayList();
		for (Realm realm : realms) {
			//log.debug("realm--->{},token----->{},supports------>{}", realm.getClass().getName(), authenticationToken.getClass().getName(), realm.supports(authenticationToken));
			if (realm.supports(authenticationToken)) {
				list.add(realm);
			}
		}
		realms = list;
		//log.debug("realms.size = {}", realms.size());
		/////////////////////////////////////////////////////////
		if (realms.size() == 1) {
			return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
		} else {
			return doMultiRealmAuthentication(realms, authenticationToken);
		}
	}
}
