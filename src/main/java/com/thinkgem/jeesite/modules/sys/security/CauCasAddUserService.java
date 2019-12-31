package com.thinkgem.jeesite.modules.sys.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.cas.CasAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 通过CAS认证后往本地系统增加用户——中国农业大学
 *
 * @author lsp
 *
 */
public class CauCasAddUserService implements CasAddUserService {

	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	@Transactional(readOnly = false)
	@Override
	public User addUser(Map<String, Object> attributes, String userId) throws CasAuthenticationException {
		// 创建user对象
		 User user = createUser(attributes, userId);
		//User user = getuserbywiscom(userId);
		if (user == null) {
			throw new CasAuthenticationException("创建用户出错了");
		}
		// 保存user
		user.setRemarks("通过CAS认证添加");
		systemService.saveUser(user);

		// 保存学生
		/*StudentEmploymentImport student = new StudentEmploymentImport();
		student.setStudentName(user.getName());
		student.setStudentId(userId);
		student.setEmail(user.getEmail());
		student.setCollege(user.getCompany());
		student.setMajor(user.getOffice());
		studentEmploymentImportDao.insert(student);*/

		//
		return user;
	}

	/*
	cas attributes {
	 UNIT_NAME=信息中心,
	 USER_ID=20150330000002,
	 UNIT_ID=3003,
	 ID_TYPE=3,
	 USER_SEX=2,
	 IS_ACTIVE=1,
	 PHONE=13800138000,
	 ID_NUMBER=infotest,
	 TYPE_NAME=教工,
	 USER_PWD=`c>I;%N2)r?f2x/u?a&jb*.w,
	 BEGIN_TIME=2015-03-30 00:00:00.0,
	 USER_NAME=信息中心测试账号,
	 USERNAME=信息中心测试账号,
	 PROTOCOL=JDBC,
	 END_TIME=2099-03-31 00:00:00.0,
	 tgtCookie=TGT-12051-LAefmh0LakiKrEi66GbWQmhkLEOqMSJCNYZ6ITEdxyzPD7krU1-cas
	}
	 */
	private User createUser(Map<String, Object> attributes, String userId) {
		String UNIT_NAME = getSafe(attributes, "UNIT_NAME");//信息中心
		String UNIT_ID = getSafe(attributes, "UNIT_ID");//3003
		String ID_TYPE = getSafe(attributes, "ID_TYPE");//3
		String USER_SEX = getSafe(attributes, "USER_SEX");//2
		String IS_ACTIVE = getSafe(attributes, "IS_ACTIVE");//1
		String PHONE = getSafe(attributes, "PHONE");//13800138000
		String USER_ID = getSafe(attributes, "USER_ID");//20150330000002
		String ID_NUMBER = getSafe(attributes, "ID_NUMBER");//infotest
		String TYPE_NAME = getSafe(attributes, "TYPE_NAME");//教工
		String USER_PWD = getSafe(attributes, "USER_PWD");//`c>I;%N2)r?f2x/u?a&jb*.w
		String USER_NAME = getSafe(attributes, "USER_NAME");//信息中心测试账号
		String USERNAME = getSafe(attributes, "USERNAME");//信息中心测试账号
		String BEGIN_TIME = getSafe(attributes, "BEGIN_TIME");//2015-03-30 00:00:00.0
		String END_TIME = getSafe(attributes, "END_TIME");//2099-03-31 00:00:00.0

		/* 往数据库增加User */
		// TODO CAS获取的信息可能不全、具体含义需要确认。
		User user = new User();
		if (!StringUtils.isBlank(PHONE)) {
			user.setPhone(PHONE);
			if (PHONE.length() == 11) {
				user.setMobile(PHONE);
			}
		}
		user.setPassword(SystemService.entryptPassword(USER_ID)); //TODO 需要解码才行
		user.setNo(USER_ID);
		user.setLoginName(USER_ID);
		user.setName(StringUtils.isEmpty(USER_NAME) ? USERNAME : USER_NAME);
		user.setOffice(new Office("1"));
		user.setCompany(new Office("1"));
		
		String roleid = SettingsUtils.getSysConfig("kaoshengroleid", "aab0fb7fb60d4b749b9ce328881e2737");
		Role role = new Role(roleid);
		List<Role> roleList = new ArrayList<Role>();
		roleList.add(role);
		user.setRoleList(roleList);

		return user;
	}

	private String getSafe(Map<String, Object> attributes, String key) {
		if (attributes != null && attributes.containsKey(key)) {
			Object object = attributes.get(key);
			return object == null ? "" : object.toString();
		}
		return "";
	}

	/**
	 * 金智数字校园一卡通信息
	 */
	private User getuserbywiscom(String userid) {
		Connection con = null;
		PreparedStatement pre = null;
		ResultSet result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@202.205.80.234:1521:URPDB1";
			String username = "USR_PORTAL";
			String password = "wiscom";
			con = DriverManager.getConnection(url, username, password);
			String sql = "select * from v_milestone_user_info where USERID=?";
			pre = con.prepareStatement(sql);
			pre.setString(1, userid);
			result = pre.executeQuery();
			if (result.next()) {
				User user = new User();
				Office office = new Office();
				office.setName(result.getString("DEPTNAME"));
				office = officeService.findOfficeByOfficeInf(office);
				user.setCompany(office);
				user.setOffice(office);
				String name = result.getString("NAME");
				user.setName(StringUtils.isBlank(name) ? userid : name);
				user.setEmail(result.getString("EMAIL"));
				String userType = result.getString("METIER");
				user.setUserType(userType);
				user.setLoginName(userid);
				//
				//user.setPassword(systemService.entryptPassword(userid));
				user.setPassword("******");
				Role sturole = systemService.getRoleByEnname("stuuser");
				user.setRoleList(Lists.newArrayList(sturole));

				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (pre != null) {
					pre.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
