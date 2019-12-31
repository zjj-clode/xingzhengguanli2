package com.thinkgem.jeesite.modules.sys.security;

import java.security.MessageDigest;

/**
 * 简单md5加密一次，并且没有盐
 *
 * @author lsp
 *
 */
public class EncryptDecryptUtil {
	public static String encrypt(String str) {
		if (str == null) {
			return null;
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; ++i) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(encrypt("admin2015"));
		// System.out.println(MD5Util.getMD5Format("admin2015"));
	}
}
