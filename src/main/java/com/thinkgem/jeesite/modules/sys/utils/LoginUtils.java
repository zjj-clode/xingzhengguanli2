/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.UserAgentUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.security.RSACoder;

public class LoginUtils {
	private static Logger logger = LoggerFactory.getLogger(LoginUtils.class);

	/**
	 * 是否需要验证码校验
	 *
	 * @param username
	 *            账号
	 * @param isFail
	 *            当前状态登录失败 ？
	 * @param clean
	 *            清除登录失败次数的计数 ？
	 * @return 登录失败次数是否大于等于某个限制，比如3次
	 */
	public static boolean isValidateCodeLogin(String username, boolean isFail, boolean clean) {
		@SuppressWarnings("unchecked")
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(username);
		logger.debug("loginFailNum----------->{}", loginFailNum);
		if (loginFailNum == null) {
			loginFailNum = Integer.valueOf(0);
		}
		if (isFail) {
			loginFailNum = Integer.valueOf(loginFailNum.intValue() + 1);
			loginFailMap.put(username, loginFailNum);

			// 登录失败，判断是否到了锁定账号的次数
			int times = SettingsUtils.getSysConfig("sys.login.lockAccount.try_times", 5);
			// times 小于1时表示没有启用账号锁定功能
			if (times > 0) {
				if (loginFailNum.intValue() > 0 && loginFailNum.intValue() % times == 0) { // 如果每3次锁定一次，那么在 3、6、9...时更新锁定时间
					isLockedLogin(username, true, false);
				}
			}
		}
		if (clean) {
			loginFailMap.remove(username);

			// 登录成功，清除锁定账号时间
			isLockedLogin(username, false, true);
		}

		//
		int times = SettingsUtils.getSysConfig("sys.login.needValidateCode.try_times", 3);
		if (times < 0) { // times 小于0时表示不要求验证码
			return false;
		}

		return loginFailNum.intValue() >= times;
	}

	/**
	 * 连续登录失败超过n次，账号锁定m小时。
	 *
	 * @param username
	 *            账号
	 * @param lock
	 *            是否锁定
	 * @param clean
	 *            是否清除锁定
	 * @return
	 */
	public static boolean isLockedLogin(String username, boolean lock, boolean clean) {
		@SuppressWarnings("unchecked")
		Map<String, Date> loginFailLockedMap = (Map<String, Date>) CacheUtils.get("loginFailLockedMap");
		if (loginFailLockedMap == null) {
			loginFailLockedMap = Maps.newHashMap();
			CacheUtils.put("loginFailLockedMap", loginFailLockedMap);
		}
		Date loginFailLockedTime = loginFailLockedMap.get(username); // 保存的时间
		logger.debug("保存的时间 -------->{}", loginFailLockedTime);
		if (lock) {
			int lockMinutes = SettingsUtils.getSysConfig("sys.login.lockAccount.lock_minutes", 5); // 锁定几分钟，默认5分钟
			loginFailLockedTime = DateUtils.addMinutes(new Date(), lockMinutes); // 锁定到某个时间
			loginFailLockedMap.put(username, loginFailLockedTime);
			logger.debug("锁定到某个时间 -------->{}", loginFailLockedTime);
		}
		if (clean) {
			loginFailLockedMap.remove(username);
			logger.debug("清除锁定时间 -------->{}", username);
		}
		//
		return loginFailLockedTime != null && loginFailLockedTime.after(new Date());
	}

	/**
	 * 是否可以安全的重定向：本地系统路径，包括相对路径和本地系统绝对路径
	 */
	public static boolean isSecureUrl(HttpServletRequest request, String url) {
		if (StringUtils.isBlank(url)) {
			return false;
		}
		if (!StringUtils.startsWithIgnoreCase(url, "http://") && !StringUtils.startsWithIgnoreCase(url, "https://")) { // 相对路径
			return true;
		} else {// 绝对路径
			return url.startsWith(Servlets.getBasePath(request));
		}
	}

	/**
     * 解密前台JS加密后的密码<br/>
     * 解密私钥配置项： sys.login.decrypt.privateKey<br/>
     * 加密公钥配置项： sys.login.encrypt.publicKey<br/>
     * 注意：<b>加密公钥或者解密私钥配未配置或者解密失败时，返回原始密码字符串</b>
     *
     * @param encryptStr
     *            前台JS加密后的密码
     * @return 解密后密码
     */
    public static String decrypt(String encryptStr) {
        logger.debug("解密前密码： ---> {}", encryptStr);
        // 解密前台JS加密后的密码
        // 私钥
        String privateKey = SettingsUtils.getSysConfig("sys.login.decrypt.privateKey");
        String decryptStr = encryptStr;
        if (jsencryptEnable()) {
            try {
                decryptStr = new String(RSACoder.decryptByPrivateKey(encryptStr, privateKey));
            } catch (Exception e) {
                logger.warn("解密出错了", e);
            }
        }
        //logger.debug("解密后： ---> {}", decryptStr);
        return decryptStr;
    }


	/**
     * 是否启用js加密解密
     */
    public static boolean jsencryptEnable() {
        boolean jsencryptEnable = //
                SettingsUtils.getSysConfig("sys.login.decrypt.enable", false) // 加密总开关，默认关闭
                        && StringUtils.isNotBlank(SettingsUtils.getSysConfig("sys.login.decrypt.privateKey")) //加密公钥不为空
                        && StringUtils.isNotBlank(SettingsUtils.getSysConfig("sys.login.encrypt.publicKey")) //解密私钥不为空
                        && !UserAgentUtils.isLteIE8(Servlets.getRequest()) //不支持IE8及以下浏览器
        ;
        return jsencryptEnable;
    }

}
