/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web.front;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.web.util.WebUtils;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.common.utils.MD5Utils;
import com.cloudinte.modules.log.entity.BusinessLog;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.SendEmailUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.UserAgentUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.UsernamePasswordToken;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.AccountValidatorUtil;
import com.thinkgem.jeesite.modules.sys.utils.BusinessLogUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${frontPath}/sys/user")
public class FrontUserController extends BaseController {

	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public User get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return systemService.getUser(id);
		} else {
			return new User();
		}
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(User user, Model model) {
		boolean isPhone = SettingsUtils.getSysConfig("isPhone", true);
		model.addAttribute("isPhone", isPhone);
		return this.basicDir + "/user/registerForm";
	}

	/**
	 * 验证登录名是否有效
	 */
	@ResponseBody
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(User user, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		// 注册失败，返回注册页面，提示错误信息
		String email = user.getLoginName();
		if (StringUtils.isBlank(email)) {
			addMessage(model, "邮箱不能为空");
			return register(user, model);
		}
		if (!AccountValidatorUtil.isEmail(email)) {
			addMessage(model, "邮箱格式不对");
			// TODO 如果邮箱含有非法字符，将其设为空不回显，避免撑包前台的验证js代码   encodeURIComponent('${user.loginName}')
			user.setEmail(null);
			//
			return register(user, model);
		}
		if (!checkEmail(email)) {
			addMessage(model, "邮箱已被使用");
			return register(user, model);
		}
		//北化要求保留手机号
		//	if("buct".equals(Global.getConfig("custmor.code"))){

		//	}
		//是否需要预留手机号
		if (SettingsUtils.getSysConfig("isPhone", true)) {

			if (StringUtils.isBlank(user.getMobile())) {
				addMessage(model, "手机不能为空");
				return register(user, model);
			}
			if (!AccountValidatorUtil.isMobile(user.getMobile())) {
				addMessage(model, "手机格式不对");
				return register(user, model);
			}
		}

		if (StringUtils.isBlank(user.getPassword())) {
			addMessage(model, "密码不能为空");
			return register(user, model);
		}
		if (!AccountValidatorUtil.isPassword(user.getPassword())) {
			addMessage(model, "密码格式不对");
			return register(user, model);
		}

		//验证码服务器端校验
		if (!checkValidateCode(user, model, request)) {
			addMessage(model, "验证码错误");
			return register(user, model);
		}

		//创建保存用户
		User u = new User();
		u.setLoginName(email); // 用户名
		u.setEmail(email); // 邮箱
		u.setPassword(SystemService.entryptPassword(user.getPassword())); // 密码
		u.setMobile(user.getMobile()); // 手机
		u.setName(email);
		if (StringUtils.isBlank(u.getUserType())) {
			u.setUserType("3"); // 默认设置为考生
		}

		//
		boolean needActive = SettingsUtils.getSysConfig("sys.register.needActive", true);

		u.setActive(needActive ? Global.NO : Global.YES); // 待激活 ，账号在后台审核通过或用户邮件激活后，变成正常状态

		systemService.register(u);
		
        // 获取并清除session中的url
        String url = Servlets.getAndClearRedirectUrlFromSession(request);
		if (needActive) {
			//
			sendActiveCodeEmail(u, request);
			
			// 注册成功，跳转到登录界面 ？ 或者 提示让其打开注册邮箱去激活。
	        /*Subject subject = UserUtils.getSubject();
	        if (subject != null) {
	            subject.logout();
	        }*/

			addMessage(redirectAttributes, "注册成功，激活邮件已发送，请激活账号 ！");
			return "redirect:" + frontPath + "/sys/user/registerSuccess";
		} else { 
		    
		    UsernamePasswordToken token = new UsernamePasswordToken();
	        token.setUsername(user.getLoginName());
	        token.setIsdirect(true); // 直接登录，系统不再验证密码
	        SecurityUtils.getSubject().login(token);
	        SecurityUtils.getSubject().getSession().setAttribute("deviceType", UserAgentUtils.isMobileOrTablet(request) ? "4" : "1");
		    
		    return "redirect:" + frontPath + "/sys/user/perfectInfo";
		}

	}

	@RequestMapping(value = "registerSuccess", method = RequestMethod.GET)
	public String registerSuccess(User user, Model model) {
		return this.basicDir + "/user/registerSuccess";
	}

	private boolean checkValidateCode(User user, Model model, HttpServletRequest request) {
		String requestValidateCode = WebUtils.getCleanParam(request, ValidateCodeServlet.VALIDATE_CODE);
		String sessionValidateCode = (String) UserUtils.getSession().getAttribute(ValidateCodeServlet.VALIDATE_CODE);
		if (requestValidateCode == null || !requestValidateCode.toUpperCase().equals(sessionValidateCode)) {
			logger.debug("验证码错误，request validateCode ： {} , session validate code : {} ", requestValidateCode, sessionValidateCode);
			return false;
		}
		return true;
	}

	/**
	 * 发送激活邮件，html邮件
	 */
	private void sendActiveCodeEmail(User user, HttpServletRequest request) {
		String code = createActiveCode(user, request);
		String mailto = user.getEmail();
		String mailtitle = "招生咨询账号激活";

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + (80 == request.getServerPort() ? "" : ":" + request.getServerPort()) + path;

		String url = basePath + frontPath + "/sys/user/active?username=" + user.getLoginName() + "&code=" + StringUtils.trimToEmpty(code);

		String mailcontent = "系统自动发送，请勿回复本邮件。<br/>点击下面的链接,激活账号<br/><a href=" + url + " target='_BLANK'>" + url + "</a>";

		SendEmailUtil.sendCommonMail(mailto, mailtitle, mailcontent);
	}

	/**
	 * 产生激活码
	 */
	private String createActiveCode(User user, HttpServletRequest request) {
		String code = IdGen.uuid();
		user.setFindPassKey(code); // find_pass_key 也存放账号激活码
		systemService.updateFindPassData(user);
		return code;
	}

	/**
	 * 点击链接激活账号
	 */
	@RequestMapping(value = "active", method = RequestMethod.GET)
	public String active(String username, String code, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!StringUtils.isBlank(code) && !StringUtils.isBlank(username)) {
			User user = new User();
			user.setLoginName(username);
			user = systemService.getFindPassData(user);
			if (user != null && code.equals(user.getFindPassKey())) {
				user.setActive(Global.YES);
				user.setFindPassKey(null);
				systemService.active(user);
				addMessage(redirectAttributes, "激活成功！");
				
				UsernamePasswordToken token = new UsernamePasswordToken();
	            token.setUsername(username);
	            token.setIsdirect(true); // 直接登录，系统不再验证密码
	            SecurityUtils.getSubject().login(token);
				return "redirect:" + frontPath + "/sys/user/perfectInfo";
			}
		}
		addMessage(redirectAttributes, "激活失败！");
		return "redirect:" + frontPath + "/sys/user/register";
	}

	/**
	 * 邮箱是否可用作账号
	 */
	private boolean checkEmail(String email) {
		return "true".equals(checkLoginName("", email));
	}

	/**
	 * 用户信息显示
	 */
	@RequiresUser
	@RequestMapping(value = "info", method = RequestMethod.GET)
	public String info(User user, HttpServletRequest request, Model model) {
		if (StringUtils.isBlank(user.getId())) {
			user = UserUtils.getUser();
		}
		model.addAttribute("user", user);
		return this.basicDir + "/user/userInfo";
	}

	/**
	 * 用户信息保存
	 */
	@RequiresUser
	@RequestMapping(value = "info", method = RequestMethod.POST)
	public String info(User user, HttpServletResponse response, HttpServletRequest request, Model model) {

		//验证码服务器端校验
		if (!checkValidateCode(user, model, request)) {
			addMessage(model, "验证码错误");
			return register(user, model);
		}

		User currentUser = systemService.getUserByLoginName(UserUtils.getUser().getLoginName());
		boolean needUpdate = false;

		logger.debug("user.getName()-->{}", user.getName());
		logger.debug("user.getProvince()-->{}", user.getProvince());
		logger.debug("user.getUserType()-->{}", user.getUserType());
		logger.debug("user.getKelei()-->{}", user.getKelei());
		logger.debug("user.getEmail()-->{}", user.getEmail());
		logger.debug("user.getMobile()-->{}", user.getMobile());

		logger.debug("currentUser.getName()-->{}", currentUser.getName());
		logger.debug("currentUser.getProvince()-->{}", currentUser.getProvince());
		logger.debug("currentUser.getUserType()-->{}", currentUser.getUserType());
		logger.debug("currentUser.getKelei()-->{}", currentUser.getKelei());
		logger.debug("currentUser.getEmail()-->{}", currentUser.getEmail());
		logger.debug("currentUser.getMobile()-->{}", currentUser.getMobile());

		if (StringUtils.isNotBlank(user.getName()) && !user.getName().equals(currentUser.getName())) {
			currentUser.setName(user.getName());
			needUpdate = true;
		}
		if (StringUtils.isNotBlank(user.getProvince()) && !user.getProvince().equals(currentUser.getProvince())) {
			currentUser.setProvince(user.getProvince());
			needUpdate = true;
		}
		if (StringUtils.isNotBlank(user.getUserType()) && !user.getUserType().equals(currentUser.getUserType())) {
			currentUser.setUserType(user.getUserType());
			needUpdate = true;
		}
		if (StringUtils.isNotBlank(user.getKelei()) && !user.getKelei().equals(currentUser.getKelei())) {
			currentUser.setKelei(user.getKelei());
			needUpdate = true;
		}

		// 邮箱不能修改
		/*if (StringUtils.isNotBlank(user.getEmail()) && !user.getEmail().equals(currentUser.getEmail())) {

			//
			if (!AccountValidatorUtil.isEmail(user.getEmail())) {
				addMessage(model, "邮箱格式不对");
				return info(currentUser, response, request, model);
			}
			if (!checkEmail(user.getEmail())) {
				addMessage(model, "邮箱已被使用");
				return info(currentUser, response, request, model);
			}

			currentUser.setEmail(user.getEmail());
			needUpdate = true;
		}*/
		/*if (StringUtils.isNotBlank(user.getMobile()) && !user.getMobile().equals(currentUser.getMobile())) {

			//
			if (!AccountValidatorUtil.isMobile(user.getMobile())) {
				addMessage(model, "手机格式不对");
				return info(currentUser, response, request, model);
			}

			currentUser.setMobile(user.getMobile());
			needUpdate = true;
		}*/

		if (needUpdate) {
			systemService.updateUserInfo(currentUser);
			model.addAttribute("message", "保存个人信息成功");
			BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_ADD, "保存个人信息成功");
		}

		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());

		// return "modules/sys/front/user/userInfo";

		return "redirect:" + frontPath + "/qa/question/list?flag=true";
	}
	
	@RequestMapping(value = "perfectInfo", method = RequestMethod.GET)
    public String perfectInfo (Model model) {
	    User user = UserUtils.getUser();
	    model.addAttribute("id", user.getId());
	    return this.basicDir + "/user/perfectInfo";
    }
	
	@RequiresUser
	@RequestMapping(value = "perfectInfo", method = RequestMethod.POST)
	public String perfectInfo(User user, HttpServletResponse response, HttpServletRequest request, Model model) {
	    
	    if (!checkValidateCode(user, model, request)) {
	        addMessage(model, "验证码不正确");
	        return this.basicDir + "/user/perfectInfo";
        }

        //
        if (StringUtils.isBlank(user.getName())) {
            addMessage(model, "姓名不能为空");
            return this.basicDir + "/user/perfectInfo";
        }
        //
        if (StringUtils.isBlank(user.getProvince())) {
            addMessage(model, "所在省市不能为空");
            return this.basicDir + "/user/perfectInfo";
        }
        //
        if (StringUtils.isBlank(user.getUserType())) {
            addMessage(model, "用户身份不能为空");
            return this.basicDir + "/user/perfectInfo";
        }
        //
        if (StringUtils.isBlank(user.getKelei())) {
            addMessage(model, "科类名称不能为空");
            return this.basicDir + "/user/perfectInfo";
        }
        
        systemService.updateUserInfo(user);
	    return this.basicDir + "/user/perfectInfoSuccess";
	}

	/**
	 * 咨询时，要求完善个人信息。所有的都必填。  未使用
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "infoAjax", method = RequestMethod.POST)
	public Map<String, Object> infoAjax(User user, HttpServletResponse response, HttpServletRequest request, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (!checkValidateCode(user, model, request)) {
			map.put("success", false);
			map.put("message", "验证码错误");
			return map;
		}

		User currentUser = UserUtils.getUser();
		if (StringUtils.isBlank(currentUser.getId())) {
			map.put("success", false);
			map.put("message", "未登录或登录超时，请重新登录");
			return map;
		}

		boolean needUpdate = false;

		//
		if (StringUtils.isBlank(user.getName())) {
			if (StringUtils.isBlank(currentUser.getName())) {
				map.put("success", false);
				map.put("message", "姓名不能为空");
				return map;
			}
		} else {
			currentUser.setName(user.getName());
			needUpdate = true;
		}
		//
		if (StringUtils.isBlank(user.getProvince())) {
			if (currentUser.getProvince() == null || StringUtils.isBlank(currentUser.getProvince())) {
				map.put("success", false);
				map.put("message", "所在省市不能为空");
				return map;
			}
		} else {
			currentUser.setProvince(user.getProvince());
			needUpdate = true;
		}
		//
		if (StringUtils.isBlank(user.getUserType())) {
			if (StringUtils.isBlank(currentUser.getUserType())) {
				map.put("success", false);
				map.put("message", "用户身份不能为空");
				return map;
			}
		} else {
			currentUser.setUserType(user.getUserType());
			needUpdate = true;
		}
		//
		if (StringUtils.isBlank(user.getKelei())) {
			if (StringUtils.isBlank(currentUser.getKelei())) {
				map.put("success", false);
				map.put("message", "科类名称不能为空");
				return map;
			}
		} else {
			currentUser.setKelei(user.getKelei());
			needUpdate = true;
		}
		// 邮箱注册的时候填了，作为账号，不让修改。
		/*if (StringUtils.isBlank(user.getEmail())) {
			if (StringUtils.isBlank(currentUser.getEmail())) {
				map.put("success", false);
				map.put("message", "邮箱不能为空");
				return map;
			}
		} else {

			//
			if (!AccountValidatorUtil.isEmail(user.getEmail())) {
				map.put("success", false);
				map.put("message", "邮箱格式不对");
				return map;
			}
			if (!checkEmail(user.getEmail())) {
				map.put("success", false);
				map.put("message", "邮箱已被使用");
				return map;
			}

			currentUser.setEmail(user.getEmail());
			needUpdate = true;
		}*/
		//
		/*if (StringUtils.isBlank(user.getMobile())) {
			if (StringUtils.isBlank(currentUser.getMobile())) {
				map.put("success", false);
				map.put("message", "手机不能为空");
				return map;
			}
		} else {

			//
			if (!AccountValidatorUtil.isMobile(user.getMobile())) {
				map.put("success", false);
				map.put("message", "手机格式不对");
				return map;
			}

			currentUser.setMobile(user.getMobile());
			needUpdate = true;
		}*/

		if (needUpdate) {
			systemService.updateUserInfo(currentUser);
			BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_ADD, "保存个人信息成功");
		}

		map.put("success", true);
		map.put("message", "保存个人信息成功");
		return map;
	}

	/**
	 * 找回密码、界面
	 */
	@RequestMapping(value = "findPassword", method = RequestMethod.GET)
	public String findPassword(User user, Model model) {
	    String email = user.getEmail();
        if (!StringUtils.isBlank(email)) {
            //匹配email的正则
            if (!new EmailValidator().isValid(email, null)) {
                user.setEmail(null);
            }
        }
		return this.basicDir + "/user/findPasswordForm";
	}

	/**
	 * 找回密码
	 */
	@RequestMapping(value = "findPassword", method = RequestMethod.POST)
	public String findPassword(User user, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		//验证码服务器端校验
		if (!checkValidateCode(user, model, request)) {
			addMessage(model, "操作失败：验证码错误");
			return findPassword(user, model);
		}

		if (StringUtils.isBlank(user.getEmail())) {
			addMessage(model, "操作失败：请填写邮箱");
			return findPassword(user, model);
		}
		
		if (!new EmailValidator().isValid(user.getEmail(), null)) {
            addMessage(model, new String[] { "操作失败：请邮箱错误" });
            return findPassword(user, model);
        }

		//TODO 发送密码修改邮件
		user.setLoginName(user.getEmail());
		User findPassData = systemService.getFindPassData(user); // 以邮箱为账号查找
		if (findPassData == null || StringUtils.isBlank(findPassData.getEmail())) {
			addMessage(model, "操作失败：邮箱不存在");
			return findPassword(user, model);
		}

		sendFindPasswordEmail(findPassData, request);

		//
		addMessage(redirectAttributes, "密码找回邮件已发送");
		return "redirect:" + frontPath + "/sys/user/findPasswordSendMaiSuccess";
	}

	/**
	 * 发送密码找回邮件，html邮件
	 */
	private void sendFindPasswordEmail(User user, HttpServletRequest request) {
		String mailto = user.getEmail();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);//不存毫秒
		calendar.add(Calendar.MINUTE, 30); // 30分钟有效期
		Date date = calendar.getTime();

		String key = IdGen.uuid();
		String code = mailto + "$" + date.getTime() + "$" + key;
		logger.debug("code--->{}", code);
		code = MD5Utils.getMd5(mailto + "$" + date.getTime() + "$" + key);
		logger.debug("code--->{}", code);
		//
		User findPassData = new User();
		findPassData.setLoginName(mailto);
		findPassData.setFindPassDate(date);
		findPassData.setFindPassKey(key);
		systemService.updateFindPassData(findPassData);

		String mailtitle = "招生咨询密码找回";

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + (80 == request.getServerPort() ? "" : ":" + request.getServerPort()) + path;

		String url = basePath + frontPath + "/sys/user/findPasswordCheck?email=" + mailto + "&code=" + code;

		String mailcontent = "系统自动发送，请勿回复本邮件。<br/>点击下面的链接,重设密码<br/><a href=" + url + " target='_BLANK'>" + url + "</a><br/>提示:本邮件超过30分钟,链接将会失效，需要重新申请'找回密码'";

		SendEmailUtil.sendCommonMail(mailto, mailtitle, mailcontent);
	}

	/**
	 * 找回密码、界面
	 */
	@RequestMapping(value = "findPasswordSendMaiSuccess", method = RequestMethod.GET)
	public String findPasswordSendMaiSuccess(User user, Model model) {
		return this.basicDir + "/user/findPasswordSendMaiSuccess";
	}

	/**
	 * 找回密码、界面
	 */

	/*@RequestMapping(value = "findPasswordqqqq", method = RequestMethod.GET)
	public String findPasswordSendqq(User user, Model model) {
		return this.basicDir + "/user/modifyPasswordSuccess";
	}*/

	/**
	 * 找回密码，邮件链接验证
	 */
	@RequestMapping(value = "findPasswordCheck", method = RequestMethod.GET)
	public String findPasswordCheck(String email, String code, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		// 验证成功，跳转到修改密码界面，不需要提供原始密码就能修改密码
		// 验证失败，跳转到找回密码界面。
		try {
			User user = checkFindPasswordCode(email, code);
			user = UserUtils.getByLoginName(user.getEmail());
			session.setAttribute("u", user);
			session.setAttribute("code", IdGen.uuid());
			addMessage(model, "链接验证成功，请直接修改密码。");
			return this.basicDir + "/user/findPasswordModifyPassForm";
		} catch (Exception e) {
			addMessage(redirectAttributes, e.getMessage());
			return "redirect:" + frontPath + "/sys/user/findPassword";
		}
	}

	@RequestMapping(value = "findPasswordModifyPasswd", method = RequestMethod.POST)
	public String findPasswordModifyPasswd(String newPassword, String code, Model model, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (StringUtils.isBlank(newPassword) || StringUtils.isBlank(code) || !code.equals(session.getAttribute("code"))) {
			return "redirect:" + frontPath + "/sys/user/findPassword";
		}
		User user = (User) session.getAttribute("u");
		if (user != null) {
			systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
		}

		addMessage(redirectAttributes, "密码修改成功");
		//return "redirect:" + adminPath + "/login";
		return this.basicDir + "/user/modifyPasswordSuccess";
	}

	private User checkFindPasswordCode(String email, String code) throws Exception {
		if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
			throw new Exception("无效链接");
		}

		// TODO 验证
		User u = new User();
		u.setLoginName(email);
		User findPassData = systemService.getFindPassData(u);
		logger.debug("findPassData == null ? {}", findPassData == null);
		if (findPassData == null) {
			throw new Exception("无效链接");
		}
		Date date = findPassData.getFindPassDate();

		logger.debug("date.after(new Date()) ? {}", date.after(new Date()));
		if (!date.after(new Date())) { // 超时
			throw new Exception("链接失效");
		}

		String k = email + "$" + date.getTime() + "$" + findPassData.getFindPassKey();
		logger.debug("k--->{}", k);
		k = MD5Utils.getMd5(k);
		logger.debug("k = {},code = {}", k, code);
		if (!k.equals(code)) { // code不对
			throw new Exception("无效链接");
		}

		return findPassData;
	}

	// 修改个人用户密码
	@RequestMapping(value = "modifyPasswd", method = RequestMethod.GET)
	public String modifyPasswd(User user, Model model) {
		if (StringUtils.isBlank(user.getId())) {
			user = UserUtils.getUser();
		}
		model.addAttribute("user", user);
		return this.basicDir + "/user/modifyPasswdForm";
	}

	/**
	 * 修改个人用户密码
	 */
	@RequestMapping(value = "modifyPasswd", method = RequestMethod.POST)
	public String modifyPasswd(String oldPassword, String newPassword, Model model, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();

		// 指定登录地址
		if (user == null || StringUtils.isBlank(user.getId())) {
			addMessage(redirectAttributes, "请先登录");
			return "redirect:" + adminPath + "/loginf?url=" + frontPath + "/sys/user/modifyPasswd";
		}
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_UPDATEPWD, "修改密码成功");

				return "redirect:" + frontPath + "/qa/question";

			} else {
				addMessage(model, "修改密码失败，旧密码错误");
				return modifyPasswd(user, model);
			}
		}
		addMessage(model, "修改密码失败，请填写密码");
		return modifyPasswd(user, model);
	}

	/**
	 * 修改个人用户密码
	 */
	@ResponseBody
	@RequestMapping(value = "modifyPasswdAjax", method = RequestMethod.POST)
	public Map<String, Object> modifyPasswdAjax(String oldPassword, String newPassword, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		if (StringUtils.isBlank(user.getId())) {
			map.put("success", false);
			map.put("message", "未登录或登录超时，请重新登录");
		}
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				map.put("success", true);
				map.put("message", "修改密码成功");
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_UPDATEPWD, "修改密码成功");
			} else {
				map.put("success", false);
				map.put("message", "修改密码失败，旧密码错误");
			}
		}
		return map;
	}
}
