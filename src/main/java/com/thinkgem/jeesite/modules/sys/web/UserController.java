/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.modules.log.entity.BusinessLog;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.BusinessLogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 *
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

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

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = { "index" })
	public String index(User user, Model model) {
		model.addAttribute("ename", "user");
		return "modules/sys/userIndex";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = { "list", "" })
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		model.addAttribute("page", page);
		model.addAttribute("ename", "user");
		setBase64EncodedQueryStringToEntity(request, user);
		return "modules/sys/userList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping("form")
	public String form(User user, Model model) {
		if (user.getCompany() == null || user.getCompany().getId() == null) {
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice() == null || user.getOffice().getId() == null) {
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		model.addAttribute("ename", "user");
		return "modules/sys/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return redirectToList(user);
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		//user.setCompany(new Office(request.getParameter("company.id")));
		//user.setOffice(new Office(request.getParameter("office.id")));

		logger.debug("user.getCompany().getId()--->{},user.getOffice().getId()--->{}", user.getCompany().getId(), user.getOffice().getId());

		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		if (!beanValidator(model, user)) {
			return form(user, model);
		}
		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))) {
			addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()) {
			if (roleIdList.contains(r.getId())) {
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);

		//
		String photo = user.getPhoto();
		if (!StringUtils.isBlank(photo)) {
			String contextPath = request.getContextPath();
			logger.debug("contextPath--->{},photo--->{}", contextPath, photo);
			if (!StringUtils.isBlank(contextPath) && photo.startsWith(contextPath)) {
				photo = photo.substring(contextPath.length());
				user.setPhoto(photo);
			}
		}

		// 保存用户信息
		systemService.saveUser(user);

		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
			UserUtils.clearCache();
			UserUtils.getSubject().logout();
		}

		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return redirectToList(user);
	}

	private String redirectToList(User user) {
		return "redirect:" + adminPath + "/sys/user/list?repage&" + (user == null ? "" : getBase64DecodedQueryStringFromEntity(user));
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return redirectToList(user);
		}
		if (UserUtils.getUser().getId().equals(user.getId())) {
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		} else if (User.isAdmin(user.getId())) {
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		} else {
			systemService.deleteUser(user);
			addMessage(redirectAttributes, "删除用户成功");
		}
		return redirectToList(user);
	}

	/**
	 * 解除锁定用户
	 *
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	/*@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "unlock")
	public String unlock(User user, RedirectAttributes redirectAttributes) {
		user.setErrlogincount(0);
		//systemService.updateUserEerrorLoginInfo(user);
		addMessage(redirectAttributes, "删除用户成功");
		return redirectToList(user);
	}*/

	/**
	 * 导出用户数据
	 *
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
			new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return redirectToList(user);
	}

	/**
	 * 导入用户数据
	 *
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return redirectToList(null);
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list) {
				try {
					if ("true".equals(checkLoginName("", user.getLoginName()))) {
						user.setPassword(SystemService.entryptPassword(user.getLoginName()));
						BeanValidators.validateWithException(validator, user);
						systemService.saveUser(user);
						successNum++;
					} else {
						failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
			}

			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条用户" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
		}
		return redirectToList(null);
	}

	/**
	 * 下载导入用户数据模板
	 *
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据导入模板.xlsx";
			List<User> list = Lists.newArrayList();
			list.add(UserUtils.getUser());
			new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return redirectToList(null);
	}

	/**
	 * 验证登录名是否有效
	 *
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 用户信息显示及保存
	 *
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info")
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			model.addAttribute("message", "保存用户信息成功");
			BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_ADD, "保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("ename", "userinfo");
		model.addAttribute("Global", new Global());
		return "modules/sys/userInfo";
	}

	@RequiresPermissions("sys:user:edit")
	@ResponseBody
	@RequestMapping(value = "uploadphoto")
	public Map<String, Object> uploadPhoto(String id, String base64Image, HttpServletRequest request, Model model) {
		Map<String, Object> map = Maps.newHashMap();
		String dataPrix = "";
		String data = "";
		if (base64Image == null || "".equals(base64Image)) {
			map.put("message", "头像上传失败，上传图片数据为空");
			map.put("result", "error");
			return map;
		} else {
			String[] d = base64Image.split("base64,");
			if (d != null && d.length == 2) {
				dataPrix = d[0];
				data = d[1];
			} else {
				map.put("message", "头像上传失败，数据不合法");
				map.put("result", "error");
				return map;
			}
		}
		String suffix = "";
		if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {// data:image/jpeg;base64,base64编码的jpeg图片数据
			suffix = ".jpg";
		} else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {// data:image/x-icon;base64,base64编码的icon图片数据
			suffix = ".ico";
		} else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {// data:image/gif;base64,base64编码的gif图片数据
			suffix = ".gif";
		} else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {// data:image/png;base64,base64编码的png图片数据
			suffix = ".png";
		} else {
			map.put("message", "头像上传图片格式不合法");
			map.put("result", "error");
			return map;
		}
		String tempFileName = id + suffix;

		String filePath = request.getSession().getServletContext().getRealPath("/") + "userfiles/1/user/photo/";
		byte[] imageBytes = Encodes.decodeBase64(data); // 将字符串格式的image转为二进制流（biye[])的decodedBytes
		try {// 指定图片要存放的位置
			FileUtils.writeByteArrayToFile(new File(filePath, tempFileName), imageBytes);
			/*Resumeinfo resumeinfo = new Resumeinfo();
			resumeinfo.setId(id);
			resumeinfo.setPhoto("/upload/resumeinfo/photo/" + tempFileName);
			resumeinfoService.updatePhoto(resumeinfo);*/
			User user = UserUtils.getUser();
			user.setPhoto("/userfiles/1/user/photo/" + tempFileName);
			systemService.updatePhoto(user);
			map.put("file", "/userfiles/1/user/photo/" + tempFileName);
			map.put("message", "头像上传成功");
			map.put("result", "ok");
		} catch (Exception e) {
			map.put("message", "头像上传失败");
			map.put("result", "error");
		}
		return map;
	}

	/**
	 * 用户信息显示及保存
	 *
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "uinfo")
	public String uinfo(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();

		if (StringUtils.isNotBlank(user.getName())) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/uiInfo";
			}

			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			model.addAttribute("message", "保存用户信息成功");
			BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_ADD, "保存用户信息成功");
		}

		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/uinfo";
	}

	/**
	 * 返回用户信息
	 *
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "infoData")
	public User infoData() {
		return UserUtils.getUser();
	}

	/**
	 * 返回用户信息
	 *
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getUserByLoginName")
	public User getUserByLoginName(String loginName) {
		return systemService.getUserByLoginName(loginName);
	}

	/**
	 * 修改个人用户密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {

			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_UPDATEPWD, "修改密码成功");
			} else {
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("ename", "userpwd");
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}

	/**
	 * 修改个人用户密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "shoChangePwd")
	public String shoChangePwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userModifyPwd";
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_UPDATEPWD, "修改密码成功");
			} else {
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}

		model.addAttribute("user", user);
		//		return "modules/shopping/front/themes/basic/shoPassword";
		return "modules/shopping/front/themes/weixin/shoPassword";
	}

	@RequestMapping(value = "changepwdshop", method = RequestMethod.POST)
	public @ResponseBody String changepwd(String oldpwd, String newpwd, String newpwd1, Model model, String validateCode, HttpServletRequest request, HttpServletResponse response) {
		User user = UserUtils.getUser();
		//System.out.println(oldpwd + ":" + newpwd + ":" + newpwd1 + ":" + user.getPassword());
		//System.out.println(SystemService.validatePassword(oldpwd, user.getPassword()));

		if (user != null && user.getId() != null) {

			if (StringUtils.isNotBlank(oldpwd) && StringUtils.isNotBlank(newpwd)) {
				if (newpwd.equals(newpwd1)) {
					if (SystemService.validatePassword(oldpwd, user.getPassword())) {
						systemService.updatePasswordById(user.getId(), user.getLoginName(), newpwd);
						return "SUCCESS";
					} else {
						return "ERROR3";
					}
				} else {
					return "ERROR2";
				}
			} else {
				return "ERROR1";
			}
		} else {
			return "needLogin";
		}

	}

	/**
	 * 修改个人用户密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPasswd")
	public String modifyPasswd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userModifyPasswd";
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
				BusinessLogUtils.saveBusinessLog(Servlets.getRequest(), BusinessLog.BUSINESS_LOG_TYPE_UPDATEPWD, "修改密码成功");
			} else {
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "modules/sys/userModifyPasswd";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i = 0; i < list.size(); i++) {
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_" + e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}

	//	@InitBinder
	//	public void initBinder(WebDataBinder b) {
	//		b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
	//			@Autowired
	//			private SystemService systemService;
	//			@Override
	//			public void setAsText(String text) throws IllegalArgumentException {
	//				String[] ids = StringUtils.split(text, ",");
	//				List<Role> roles = new ArrayList<Role>();
	//				for (String id : ids) {
	//					Role role = systemService.getRole(Long.valueOf(id));
	//					roles.add(role);
	//				}
	//				setValue(roles);
	//			}
	//			@Override
	//			public String getAsText() {
	//				return Collections3.extractToString((List) getValue(), "id", ",");
	//			}
	//		});
	//	}
}
