/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ckfinder.connector.ConnectorServlet;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * CKFinderConnectorServlet
 *
 * @author ThinkGem
 * @version 2014-06-25
 */
public class CKFinderConnectorServlet extends ConnectorServlet {

	private static final long serialVersionUID = 1L;

	//private static Logger logger = LoggerFactory.getLogger(CKFinderConnectorServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, false);
		super.doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, true);
		super.doPost(request, response);
	}

	private void prepareGetResponse(final HttpServletRequest request, final HttpServletResponse response, final boolean post) throws ServletException {
		Principal principal = UserUtils.getPrincipal();
		if (principal == null) {
			return;
		}
		String command = request.getParameter("command");
		String type = request.getParameter("type");

		//logger.debug("command ---> {},type ---> {}", command, type);

		// 初始化时，如果startupPath文件夹不存在，则自动创建startupPath文件夹
		if ("Init".equals(command)) {
			String startupPath = request.getParameter("startupPath");// 当前文件夹可指定为模块名
			//logger.debug("Init，startupPath====>" + startupPath);
			if (startupPath != null) {
				String[] ss = startupPath.split(":");
				if (ss.length == 2) {
					String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + principal + "/" + ss[0] + ss[1];
					//logger.debug("Init，realPath====>" + realPath);
					FileUtils.createDirectory(FileUtils.path(realPath));
				}
			}
		}
		// 快捷上传，自动创建当前文件夹，并上传到该路径
		else if ("QuickUpload".equals(command) && type != null) {
			String currentFolder = request.getParameter("currentFolder");// 当前文件夹可指定为模块名
			String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + principal + "/" + type + (currentFolder != null ? currentFolder : "");
			//logger.debug("QuickUpload，realPath====>" + realPath);
			FileUtils.createDirectory(FileUtils.path(realPath));
		}
		//		//System.out.println("------------------------");
		//		for (Object key : request.getParameterMap().keySet()){
		//			//logger.debug(key + ": " + request.getParameter(key.toString()));
		//		}
	}

}
