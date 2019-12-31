package com.thinkgem.jeesite.modules.sys.security;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * @author 廖水平<br>
 *         处理UsernamePasswordToken
 */
// 暂未启用
//@Service
public class JdbcRealm extends org.apache.shiro.realm.jdbc.JdbcRealm {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private SystemService systemService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();

		logger.debug("username={}", username);

		// Null username is invalid
		if (username == null) {
			throw new AccountException("Null usernames are not allowed by this realm.");
		}

		Connection conn = null;
		SimpleAuthenticationInfo info = null;
		try {
			conn = dataSource.getConnection();

			String password = null;
			String salt = null;
			switch (saltStyle) {
				case NO_SALT:
					password = getPasswordForUser(conn, username)[0];
					break;
				case CRYPT:
					// TODO: separate password and hash from getPasswordForUser[0]
					throw new ConfigurationException("Not implemented yet");
					//break;
				case COLUMN:
					String[] queryResults = getPasswordForUser(conn, username);
					password = queryResults[0];
					salt = queryResults[1];
					break;
				case EXTERNAL:
					password = getPasswordForUser(conn, username)[0];
					salt = getSaltForUser(username);
			}

			if (password == null) {
				throw new UnknownAccountException("No account found for user [" + username + "]");
			}

			//
			User user = getSystemService().getUserByLoginName(username);
			if (user == null) {
				return null;
			}

			/*//info = new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
			info = new SimpleAuthenticationInfo(new Principal(user, false, null), password.toCharArray(), getName());

			if (salt != null) {
				info.setCredentialsSalt(ByteSource.Util.bytes(salt));
			}*/

			//密码串中，前16位盐加密串
			info = new SimpleAuthenticationInfo(new Principal(user, false, ""), password.substring(16), ByteSource.Util.bytes(salt), getName());

			SimpleCredentialsMatcher credentialsMatcher = (SimpleCredentialsMatcher) getCredentialsMatcher();
			Object tokenCredentials = token.getCredentials();
			Object accountCredentials = info.getCredentials();
			logger.debug("tokenCredentials={},accountCredentials={},doCredentialsMatch={}", tokenCredentials, accountCredentials, credentialsMatcher.doCredentialsMatch(upToken, info));

		} catch (SQLException e) {
			final String message = "There was a SQL error while authenticating user [" + username + "]";
			if (logger.isErrorEnabled()) {
				logger.error(message, e);
			}

			// Rethrow any SQL errors as an authentication exception
			throw new AuthenticationException(message, e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeConnection(conn);
		}

		return info;
	}

	private String[] getPasswordForUser(Connection conn, String username) throws SQLException {
		logger.debug("saltStyle={}", saltStyle);
		String[] result;
		boolean returningSeparatedSalt = false;
		switch (saltStyle) {
			case NO_SALT:
			case CRYPT:
			case EXTERNAL:
				result = new String[1];
				break;
			default:
				result = new String[2];
				returningSeparatedSalt = true;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			logger.debug("authenticationQuery={}", authenticationQuery);
			ps = conn.prepareStatement(authenticationQuery);
			ps.setString(1, username);

			// Execute query
			rs = ps.executeQuery();

			// Loop over results - although we are only expecting one result, since usernames should be unique
			boolean foundResult = false;
			while (rs.next()) {

				// Check to ensure only one row is processed
				if (foundResult) {
					throw new AuthenticationException("More than one user row found for user [" + username + "]. Usernames must be unique.");
				}

				result[0] = rs.getString(1);
				if (returningSeparatedSalt) {
					result[1] = rs.getString(2);
				}

				foundResult = true;
			}
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}
		logger.debug("result={}", result[0]);
		return result;
	}

	@Override
	protected String getSaltForUser(String username) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			String password = getPasswordForUser(conn, username)[0];
			byte[] bytes = Encodes.decodeHex(password.substring(0, 16));
			return new String(bytes, "UTF-8");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeConnection(conn);
		}
		return null;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		Principal principal = (Principal) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
			Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
			if (sessions.size() > 0) {
				// 如果是登录进来的，则踢出已在线用户
				if (UserUtils.getSubject().isAuthenticated()) {
					for (Session session : sessions) {
						getSystemService().getSessionDao().delete(session);
					}
				}
				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
				else {
					UserUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
				}
			}
		}
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list) {
				if (StringUtils.isNotBlank(menu.getPermission())) {
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(), ",")) {
						info.addStringPermission(permission);
					}
				}
			}
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			for (Role role : user.getRoleList()) {
				info.addRole(role.getEnname());
			}
			// 更新登录IP和时间
			getSystemService().updateUserLoginInfo(user);
			// 记录登录日志
			LogUtils.saveLog(Servlets.getRequest(), "通过JDBC认证登录");
			return info;
		} else {
			return null;
		}
	}

	public SystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
}
