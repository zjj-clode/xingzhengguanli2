/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.LoginUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.web.SlideController;

/**
 * 系统安全认证实现类<br>
 * 处理 UsernamePasswordToken
 *
 * @author ThinkGem
 * @version 2014-7-5
 */
@Service
// @DependsOn({"userDao","roleDao","menuDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SystemService systemService;

    @Autowired
    private HashedCredentialsMatcher credentialsMatcher;

    private Collection<OtherAuthorizingLogic> otherAuthorizingLogics;

    public void setOtherAuthorizingLogics(Collection<OtherAuthorizingLogic> otherAuthorizingLogics) {
        this.otherAuthorizingLogics = otherAuthorizingLogics;
    }

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {

        ////////////////////////////////////////////////////////////
        if (!Collections3.isEmpty(otherAuthorizingLogics)) {
            for (OtherAuthorizingLogic otherAuthorizingLogic : otherAuthorizingLogics) {
                if (otherAuthorizingLogic != null) {
                    otherAuthorizingLogic.beforeDoGetAuthenticationInfo(authcToken);
                }
            }
        }
        ////////////////////////////////////////////////////////////

        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

        // 如果前端加过密，解密密码
        if (token.getPassword() != null && LoginUtils.jsencryptEnable()) {
            String passwordStr = new String(token.getPassword());
            String decryptPasswordStr = LoginUtils.decrypt(passwordStr);
            token.setPassword(decryptPasswordStr.toCharArray());
        }

        int activeSessionSize = getSystemService().getSessionDao().getActiveSessions(false).size();
        if (logger.isDebugEnabled()) {
            logger.debug("login submit, active session size: {}, username: {}, userpasswd:{}, url:{}",
                activeSessionSize, token.getUsername(),
                // token.getPassword(),
                token.getPassword() == null ? "" : StringUtils.leftPad("", token.getPassword().length, "*"), // 日志中只显示与密码相同位数的*号
                token.getUrl1());
        }

        // 校验是否锁定账号
        if (SettingsUtils.getSysConfig("sys.login.lockAccount.try_times", 0) > 0) {
            if (LoginUtils.isLockedLogin(token.getUsername(), false, false)) {
                int lockMinutes = SettingsUtils.getSysConfig("sys.login.lockAccount.lock_minutes", 5);
                logger.debug("尝试次数过多，账号被锁定{}分钟", lockMinutes);
                throw new AuthenticationException("msg:尝试次数过多，账号被锁定" + lockMinutes + "分钟");
            }
        }

        // 校验用户名密码
        User user = null;
        try {
            user = getSystemService().getUserByLoginName(token.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果非用户直接登录
        if (!token.isIsdirect()) {

            // 校验登录验证码 ----- 后台登录/a/login，验证码为SlideController的滑块方式
            if (SettingsUtils.getSysConfig("sys.login.needValidateCode.try_times", 0) > 0) {
                if (LoginUtils.isValidateCodeLogin(token.getUsername(), false, false)) {
                    Session session = UserUtils.getSession();
                    logger.debug("session.getAttribute(SlideController.SESSION_KEY_SLIDE_VALID)---->{}",
                        session.getAttribute(SlideController.SESSION_KEY_SLIDE_VALID));
                    if (!Boolean.TRUE.equals(session.getAttribute(SlideController.SESSION_KEY_SLIDE_VALID))) {
                        logger.debug("验证码错误");
                        throw new AuthenticationException("msg:验证码错误, 请重试.");
                    }
                }
            }

            if (user != null) {
                if (Global.NO.equals(user.getLoginFlag())) {
                    throw new AuthenticationException("msg:该帐号禁止登录.");
                }
                if (SettingsUtils.getSysConfig("sys.register.needActive", false)
                    && Global.NO.equals(user.getActive())) {
                    throw new AuthenticationException("msg:该帐号还未激活.");
                }
                //
                byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
                SimpleAuthenticationInfo authenticationInfo =
                    new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin(), token.getUrl1()),
                        user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());

                ////////////////////////////////////////////////////////////
                if (!Collections3.isEmpty(otherAuthorizingLogics)) {
                    for (OtherAuthorizingLogic otherAuthorizingLogic : otherAuthorizingLogics) {
                        if (otherAuthorizingLogic != null) {
                            otherAuthorizingLogic.afterDoGetAuthenticationInfo(authcToken, authenticationInfo, user);
                        }
                    }
                }
                ////////////////////////////////////////////////////////////

                return authenticationInfo;

            } else {
                return null;
            }
        } else // 如果用户直接登录，外部ssologin
        {

            /*// 校验登录验证码 ----- 前台登录/a/loginf，验证码为ValidateCodeServlet的
            Session session = UserUtils.getSession();
            String code = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
            if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)) {
            	logger.debug("验证码错误，token.getCaptcha() ： {} , session validate code : {} ", token.getCaptcha(), code);
            	throw new AuthenticationException("msg:验证码错误, 请重试.");
            }*/
            // 前台登录验证码校验在LoginController中已经处理了，此处不需要

            if (user != null) {
                if (Global.NO.equals(user.getLoginFlag())) {
                    throw new AuthenticationException("msg:该帐号禁止登录.");
                }
                /*byte[] salt = Encodes.decodeHex("02a3f0772fcca9f4");
                String password = "admin";
                token.setPassword(password.toCharArray());
                return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin(), token.getUrl1()), "15adc990734b45c6f059c7d33ee28362c4852032", ByteSource.Util.bytes(salt), getName());*/

                // 让认证检查恒通过
                String hashAlgorithmName = credentialsMatcher.getHashAlgorithmName();// 算法：MD5、SHA-1
                int hashIterations = credentialsMatcher.getHashIterations();// 循环加密次数
                String password = token.getPassword() == null ? token.getUsername() : new String(token.getPassword());// 原始密码随便取一个值
                token.setPassword(password.toCharArray());// 设置token密码为我们取的密码，必须！
                byte[] salt = Digests.generateSalt(8); // 盐，随机数，也可以是空： byte[] salt = null
                byte[] hashPassword = Digests.digest(password.getBytes(), hashAlgorithmName, salt, hashIterations);// 加密。同org.apache.shiro.crypto.hash.SimpleHash.hash(byte[]
                                                                                                                   // bytes,
                                                                                                                   // byte[]
                                                                                                                   // salt,
                                                                                                                   // int
                                                                                                                   // hashIterations)方法
                // 密码验证时，是否使用盐以及使用什么盐，由此处SimpleAuthenticationInfo构造方法参数credentialsSalt传入的盐决定。
                SimpleAuthenticationInfo authenticationInfo =
                    new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin(), token.getUrl1()),
                        Encodes.encodeHex(hashPassword), salt == null ? null : ByteSource.Util.bytes(salt), getName());

                ////////////////////////////////////////////////////////////
                if (!Collections3.isEmpty(otherAuthorizingLogics)) {
                    for (OtherAuthorizingLogic otherAuthorizingLogic : otherAuthorizingLogics) {
                        if (otherAuthorizingLogic != null) {
                            otherAuthorizingLogic.afterDoGetAuthenticationInfo(authcToken, authenticationInfo, user);
                        }
                    }
                }
                ////////////////////////////////////////////////////////////

                return authenticationInfo;

            } else {
                return null;
            }
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用<br>
     * <b>注意：APP登录接口不会触发此方法！随后执行其他操作的时候才会触发。</b>
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal)getAvailablePrincipal(principals);
        // 如果不允许账号同时登录
        if (!SettingsUtils.getSysConfig("sys.login.multiAccountLogin", true)) {
            // 获取当前已登录的用户
            logger.debug("删除用户 principal={}", principal);
            Collection<Session> sessions =
                getSystemService().getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
            if (sessions.size() > 0) {
                // 如果当前用户是登录(账号密码认证)进来的，则踢出其他已在线用户（跟当前登录的用户同账号）。
                if (UserUtils.getSubject().isAuthenticated()) {
                    for (Session session : sessions) {
                        logger.debug("删除 session.id={},session.ip={}", session.getId(), session.getHost());
                        getSystemService().getSessionDao().delete(session);
                    }
                }
                // 如果当前用户是记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
                else {
                    logger.debug("账号已在其它地方登录，请重新登录。");
                    UserUtils.getSubject().logout();
                    throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
                }
            }
        }
        //
        logger.debug("授权查询回调函数........");
        //
        //
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

            // TODO 此处与登录相关的日志，应该写到登录成功过滤器中。因为这个方法会在授权缓存过期的时候也会调用，不能准确记录登录日志的！
            // 更新登录IP和时间
            getSystemService().updateUserLoginInfo(user);
            // 记录登录日志
            LogUtils.saveLog(Servlets.getRequest(), "系统登录");

            return info;
        } else {
            return null;
        }
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     *
     * @param permission
     */
    private void authorizationValidate(Permission permission) {
        // 模块授权预留接口
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    /*@PostConstruct
    public void initCredentialsMatcher() {
    	HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
    	matcher.setHashIterations(SystemService.HASH_INTERATIONS);
    	setCredentialsMatcher(matcher);
    }*/

    /**
     * 清空用户关联权限认证，待下次使用时重新加载
     */
    public void clearCachedAuthorizationInfo(Principal principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清空所有关联认证
     *
     * @Deprecated 不需要清空，授权缓存保存到session中
     */
    @Deprecated
    public void clearAllCachedAuthorizationInfo() {
        // Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        // if (cache != null) {
        // for (Object key : cache.keys()) {
        // cache.remove(key);
        // }
        // }
    }

    /**
     * 获取系统业务对象
     */
    public SystemService getSystemService() {
        if (systemService == null) {
            systemService = SpringContextHolder.getBean(SystemService.class);
        }
        return systemService;
    }

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String id; // 编号
        private final String loginName; // 登录名
        private final String name; // 姓名
        private final boolean mobileLogin; // 是否手机登录
        private String url1;
        private String openID;// 微信登录ID

        // private Map<String, Object> cacheMap;

        public Principal(User user) {
            id = user.getId();
            loginName = user.getLoginName();
            name = user.getName();
            mobileLogin = false;
        }

        public Principal(User user, boolean mobileLogin, String url1) {
            id = user.getId();
            loginName = user.getLoginName();
            name = user.getName();
            this.mobileLogin = mobileLogin;
            this.url1 = url1;
        }

        public String getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        public boolean isMobileLogin() {
            return mobileLogin;
        }

        // @JsonIgnore
        // public Map<String, Object> getCacheMap() {
        // if (cacheMap==null){
        // cacheMap = new HashMap<String, Object>();
        // }
        // return cacheMap;
        // }

        /**
         * 获取SESSIONID
         */
        public String getSessionid() {
            try {
                return (String)UserUtils.getSession().getId();
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        public String toString() {
            return id;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getOpenID() {
            return openID;
        }

        public void setOpenID(String openID) {
            this.openID = openID;
        }

    }
}
