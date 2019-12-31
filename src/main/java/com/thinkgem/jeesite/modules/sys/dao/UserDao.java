/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserRole;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
/**
 * @author Administrator
 * 
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	/**
	 * 更新用户头像
	 */
	public void updatePhoto(User user);

	/**
	 * 根据登录名称查询用户，不关联其他表。
	 */
	public User getByLoginNameWithoutJoins(User user);

	/**
	 * 根据登录名称查询用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * 根据openid获得用户信息
	 * 
	 * @param loginName
	 * @return
	 */
	public User getByWeixinOpenID(User user);

	/**
	 * 根据openid获得用户信息
	 * 
	 * @param loginName
	 * @return
	 */
	public User getByQyWeixinOpenID(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);

	/**
	 * 查询全部用户数目
	 * 
	 * @return
	 */
	public long findAllCount(User user);

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);

	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * 
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 更新错误登录信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateErrorLogininfById(User user);

	/**
	 * 更新微信关联id
	 * 
	 * @param user
	 * @return
	 */
	public int updateOpenidById(User user);

	/**
	 * 删除用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);

	/**
	 * 插入用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);

	/**
	 * 查询用户数目
	 * 
	 * @param user
	 * @return
	 */
	public long findCount(User user);

	//add by lsp 2016.3.20
	public User findCampusBuilding(User user);

	public int insertUserCampus(User user);

	public int deleteUserCampus(User user);

	public int insertUserBuilding(User user);

	public int deleteUserBuilding(User user);

	//add by lsp 2017.5.26
	public int insertUserStuClassname(User user);

	public int deleteUserStuClassname(User user);

	public int insertUserStuDep(User user);

	public int deleteUserStuDep(User user);

	public int insertUserStuGrade(User user);

	public int deleteUserStuGrade(User user);

	public int insertUserStuSex(User user);

	public int deleteUserStuSex(User user);

	public int insertUserStuType(User user);

	public int deleteUserStuType(User user);

	/**
	 * 查公寓管理员（固定限制条件：角色英文名以gy_sgy开头）
	 */
	public List<User> findGyUserList(User user);

	public long findGyUserCount(User user);

	/**
	 * 根据登录名称查询用户（用于判断有无，实体对象信息不全）
	 * 
	 * @param loginName
	 * @return
	 */
	public User getByLoginName2(User user);

	public List<User> findUserByRoleId(User user);

	/**
	 * 根据ID批量查询用户信息
	 */
	public List<User> batchFind(String[] ids);

	//// 密码找回相关
	public User getFindPassData(User user);

	public void updateFindPassData(User user);

	public void active(User user);
	public List<User> findNewList(User user);
	
	
	public User getByLoginName3(User user);
	
	public void batchInsertUserRole(List<UserRole> roleList);
	public void batchInsertUpdate(List<User> userList);
	
	public void updateBasicUserInfo(User user);
	
	void deleteUserArea(User user);
	void insertUserArea(User user);
}
