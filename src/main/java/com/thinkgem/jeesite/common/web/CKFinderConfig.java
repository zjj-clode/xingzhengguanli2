/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.web;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ckfinder.connector.configuration.Configuration;
import com.ckfinder.connector.data.AccessControlLevel;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * CKFinder配置
 *
 * @author ThinkGem
 * @version 2014-06-25
 */
public class CKFinderConfig extends Configuration {

	private static Logger logger = LoggerFactory.getLogger(CKFinderConfig.class);

	public CKFinderConfig(ServletConfig servletConfig) {
		super(servletConfig);
	}

	@Override
	protected Configuration createConfigurationInstance() {
		Principal principal = UserUtils.getPrincipal();
		if (principal == null) {
			return new CKFinderConfig(this.servletConf);
		}

		// TODO 此处根据用户实际被授予权限来设置
		boolean isView = false;
		boolean isUpload = false;
		boolean isEdit = false;
		User user = UserUtils.getUser();
		if (user != null) {
			if (user.isAdmin()) {
				isView = true;
				isUpload = true;
				isEdit = true;
			} else {
				isView = UserUtils.getSubject().isPermitted("cms:ckfinder:view");
				isUpload = UserUtils.getSubject().isPermitted("cms:ckfinder:upload");
				isEdit = UserUtils.getSubject().isPermitted("cms:ckfinder:edit");
			}
		}

		// ckfinder.xml 中配置了一条不限角色的ACL，全部权限被设置为false
		AccessControlLevel alc = this.getAccessConrolLevels().get(0);
		alc.setFolderView(isView);
		alc.setFolderCreate(isEdit);
		alc.setFolderRename(isEdit);
		alc.setFolderDelete(isEdit);
		alc.setFileView(isView);
		alc.setFileUpload(isUpload);
		alc.setFileRename(isEdit);
		alc.setFileDelete(isEdit);

		//AccessControlUtil.getInstance(this).loadACLConfig();
		//修改为：
		AccessControlUtil.getInstance().resetConfiguration(); // 重新
		AccessControlUtil.getInstance().loadConfiguration(this);

		try {
			this.baseURL = FileUtils.path(Servlets.getRequest().getContextPath() + Global.USERFILES_BASE_URL + principal + "/");
			this.baseDir = FileUtils.path(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + principal + "/");

			logger.debug("baseURL------>" + baseURL);
			logger.debug("baseDir------>" + baseDir);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new CKFinderConfig(this.servletConf);
	}

	@Override
	public boolean checkAuthentication(final HttpServletRequest request) {
		return UserUtils.getPrincipal() != null;
	}

}
