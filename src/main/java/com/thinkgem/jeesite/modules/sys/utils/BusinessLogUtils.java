/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cloudinte.modules.log.dao.BusinessLogDao;
import com.cloudinte.modules.log.entity.BusinessLog;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2014-11-7
 */
public class BusinessLogUtils {
	
	//public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
	
	private static BusinessLogDao businesslogDao = SpringContextHolder.getBean(BusinessLogDao.class);
	private static ThreadPoolTaskExecutor taskExecutor = SpringContextHolder.getBean("taskExecutor");
	/*private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);*/
	
	
	/**
	 * 保存日志
	 */
	public static void saveBusinessLog(HttpServletRequest request, String type, String title){
		User user = UserUtils.getUser();
		if (user != null && user.getId() != null){
			BusinessLog log = new BusinessLog();
			log.setTitle(title);
			log.setType(type);
			log.setIp(StringUtils.getRemoteAddr(request));
			/*log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			log.setParams(request.getParameterMap());
			log.setMethod(request.getMethod());*/
			// 异步保存日志
			taskExecutor.execute(new SaveBusinessLogThread(log));
		}
	}

	/**
	 * 保存日志线程
	 */
	public static class SaveBusinessLogThread extends Thread{
		
		private BusinessLog businesslog;
		
		public SaveBusinessLogThread(BusinessLog businesslog){
			super(SaveBusinessLogThread.class.getSimpleName());
			this.businesslog = businesslog;
			//start();
		}
		
		@Override
		public void run() {
			// 保存日志信息
			businesslog.preInsert();
			businesslogDao.insert(businesslog);
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	/*public static String getMenuNamePath(String requestUri, String permission){
		String href = StringUtils.substringAfter(requestUri, Global.getAdminPath());
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			List<Menu> menuList = menuDao.findAllList(new Menu());
			for (Menu menu : menuList){
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null){
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtils.split(menu.getParentIds(), ",")){
						if (Menu.getRootId().equals(id)){
							continue; // 过滤跟节点
						}
						for (Menu m : menuList){
							if (m.getId().equals(id)){
								namePathList.add(m.getName());
								break;
							}
						}
					}
					namePathList.add(menu.getName());
					namePath = StringUtils.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					menuMap.put(menu.getHref(), namePath);
				}else if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						menuMap.put(p, namePath);
					}
				}
				
			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
		}
		String menuNamePath = menuMap.get(href);
		if (menuNamePath == null){
			for (String p : StringUtils.split(permission)){
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}*/

	
}
