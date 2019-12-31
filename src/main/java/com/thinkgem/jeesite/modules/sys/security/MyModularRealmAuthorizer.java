package com.thinkgem.jeesite.modules.sys.security;

import java.util.Set;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多realm分别授权。默认是遍历所有的realm进行授权。不管是cas登录还是本地登录，我的授权信息都是一样的，只需要执行一次授权。
 *
 * @author 廖水平
 *
 */
public class MyModularRealmAuthorizer extends ModularRealmAuthorizer {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		// return super.isPermitted(principals, permission);

		assertRealmsConfigured();

		// 认证通过的Realm的名字
		Set<String> realmNames = principals.getRealmNames();

		/*
		for (String realmName : realmNames) {
			log.debug("realmName ----------> {}", realmName);
		}
		*/

		for (Realm realm : getRealms()) {

			//log.debug("getRealms() ----------> {}", realm.getName());

			if (!(realm instanceof Authorizer)) {
				continue;
			}

			if (realmNames.contains(realm.getName())) {
				return ((Authorizer) realm).isPermitted(principals, permission);
			}
		}
		return false;
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		// return super.isPermitted(principals, permission);

		assertRealmsConfigured();

		// 认证通过的Realm的名字
		Set<String> realmNames = principals.getRealmNames();
		for (String realmName : realmNames) {
			log.debug("realmName ----------> {}", realmName);
		}

		for (Realm realm : getRealms()) {

			//log.debug("getRealms() ----------> {}", realm.getName());

			if (!(realm instanceof Authorizer)) {
				continue;
			}

			if (realmNames.contains(realm.getName())) {
				return ((Authorizer) realm).isPermitted(principals, permission);
			}
		}
		return false;
	}

	@Override
	public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
		// return super.hasRole(principals, roleIdentifier);

		assertRealmsConfigured();

		// 认证通过的Realm的名字
		Set<String> realmNames = principals.getRealmNames();

		/*
		for (String realmName : realmNames) {
			log.debug("realmName ----------> {}", realmName);
		}
		*/

		for (Realm realm : getRealms()) {

			//log.debug("getRealms() ----------> {}", realm.getName());

			if (!(realm instanceof Authorizer)) {
				continue;
			}

			if (realmNames.contains(realm.getName())) {
				return ((Authorizer) realm).hasRole(principals, roleIdentifier);
			}
		}
		return false;
	}

}
