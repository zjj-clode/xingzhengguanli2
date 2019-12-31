/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils.excel.fieldtype;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 根据学号转换为用户对象
 */
public class UserType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		UserDao userDao = SpringContextHolder.getBean(UserDao.class);
		User user =new User();
		user.setLoginName(val);
		user=userDao.getByLoginName(user);
		return user;
	}

}
