package com.thinkgem.jeesite.modules.sys.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 自定义密码验证规则<br/>
 * 当前此类的规则跟{@link SystemService}相同
 *
 * @see SystemService#entryptPassword(String) SystemService的密码加密
 * @see SystemService#validatePassword(String, String) SystemService的密码验证
 *
 * @author 廖水平
 */
public class SimpleCredentialsMatcher extends org.apache.shiro.authc.credential.SimpleCredentialsMatcher {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 密码验证是否通过。<br/>
	 * 验证方法：验证输入密码和已存密码是否一样
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		//return super.doCredentialsMatch(token, info);

		// 比较前处理一下
		if (!(token instanceof UsernamePasswordToken)) { // 账号密码方式登陆
			return false;
		}
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String plainPassword = new String((char[]) token.getCredentials()); // 用户输入的原始密码
		User user = getSystemService().getUserByLoginName(usernamePasswordToken.getUsername());// 获取账号对应的用户
		if (user == null) { // 账号不存在
			return false;
		}
		String pwd = user.getPassword();
		if (StringUtils.isBlank(pwd) || pwd.length() < 16) {
			return false;
		}
		byte[] salt = Encodes.decodeHex(pwd.substring(0, 16));//取得账号对应的用户的加密密码，前16位还原后就是盐
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, SystemService.HASH_INTERATIONS);//SHA-1加密，盐，1024次循环
		String tokenCredentials = Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);//最终的密码字符串：处理后的盐 + 处理后的密码
		//log.debug("token.getCredentials()={},plainPassword={},salt={},hashPassword={},tokenCredentials={}", token.getCredentials(), plainPassword, salt, hashPassword, tokenCredentials);
		//

		Object accountCredentials = getCredentials(info); //doGetAuthenticationInfo返回的AuthenticationInfo中保存的密码， 即从数据库中读取的密码，通常是字符串
		return equals(tokenCredentials, accountCredentials);
	}

	private SystemService systemService;

	private SystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}

}
