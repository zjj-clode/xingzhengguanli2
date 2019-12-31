package com.cloudinte.modules.xingzhengguanli.web;
import com.thinkgem.jeesite.common.config.Global;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangement;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementSetting;
import com.cloudinte.modules.xingzhengguanli.service.WorkArrangementService;
import com.cloudinte.modules.xingzhengguanli.service.WorkArrangementSettingService;
import com.cloudinte.modules.xingzhengguanli.utils.CalendarUtils;
import com.cloudinte.modules.xingzhengguanli.utils.WordUtils;

/**
 * 工作安排设置Controller
 * @author dcl
 * @version 2019-12-06
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/workArrangementSetting")
public class WorkArrangementSettingController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "工作安排设置";

	@Autowired
	private WorkArrangementSettingService workArrangementSettingService;
	
	@Autowired
	private WorkArrangementService workArrangementService;
	
	@ModelAttribute
	public WorkArrangementSetting get(@RequestParam(required=false) String id) {
		WorkArrangementSetting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workArrangementSettingService.get(id);
		}
		if (entity == null){
			entity = new WorkArrangementSetting();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkArrangementSetting workArrangementSetting, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			workArrangementSetting.setCreateBy(opUser);
		}
		Page<WorkArrangementSetting> page = workArrangementSettingService.findPage(new Page<WorkArrangementSetting>(request, response), workArrangementSetting); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "workArrangementSetting");
		setBase64EncodedQueryStringToEntity(request, workArrangementSetting);
		return "modules/xingzhengguanli/workArrangementSetting/workArrangementSettingList";
	}

	@RequiresPermissions("xingzhengguanli:workArrangementSetting:view")
	@RequestMapping(value = "form")
	public String form(WorkArrangementSetting workArrangementSetting, Model model) {
		if(StringUtils.isBlank(workArrangementSetting.getId())){
			workArrangementSetting.setStartDate(CalendarUtils.getMondayOfThisWeekDate());
			workArrangementSetting.setEndDate(CalendarUtils.getSundayOfThisWeekDate());
		}
		model.addAttribute("workArrangementSetting", workArrangementSetting);
		model.addAttribute("ename", "workArrangementSetting");
		return "modules/xingzhengguanli/workArrangementSetting/workArrangementSettingForm";
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:view")
	@RequestMapping(value = "showWorkArrangement")
	public String showWorkArrangement(WorkArrangementSetting workArrangementSetting, Model model) {
		
		WorkArrangement workArrangement = new WorkArrangement();
		workArrangement.setStartDate(workArrangementSetting.getStartDate());
		workArrangement.setEndDate(workArrangementSetting.getEndDate());
		List<WorkArrangement> arrangmentList = workArrangementService.findList(workArrangement);
		model.addAttribute("arrangmentList", arrangmentList);
		model.addAttribute("workArrangementSetting", workArrangementSetting);
		model.addAttribute("ename", "workArrangementSetting");
		return "modules/xingzhengguanli/workArrangementSetting/showWorkArrangementForm";
	}
	
	@RequestMapping(value = "downloadWorkArrangement")
	public String downloadWorkArrangement(WorkArrangementSetting workArrangementSetting,HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		WorkArrangement workArrangement = new WorkArrangement();
		workArrangement.setStartDate(workArrangementSetting.getStartDate());
		workArrangement.setEndDate(workArrangementSetting.getEndDate());
		List<WorkArrangement> arrangmentList = workArrangementService.findList(workArrangement);
		
		if(arrangmentList != null && arrangmentList.size() != 0){
			for (WorkArrangement workArrangement2 : arrangmentList) {
				String importantJob = workArrangement2.getImportantJob(); 
				if(StringUtils.isNotBlank(importantJob)){
					
					importantJob = importantJob.replaceAll("\r\n", "<w:br/>");
					importantJob = importantJob.replaceAll("&mdash;", "-");
					importantJob = importantJob.replaceAll("&ldquo;", "“");
					importantJob = importantJob.replaceAll("&rdquo;", "”");
					workArrangement2.setImportantJob(importantJob);
				}
				
				String otherJob = workArrangement2.getOtherJob();
				if(StringUtils.isNotBlank(otherJob)){
					otherJob = otherJob.replaceAll("\r\n", "<w:br/>");
					otherJob = otherJob.replaceAll("&mdash;", "-");
					otherJob = otherJob.replaceAll("&ldquo;", "“");
					otherJob = otherJob.replaceAll("&rdquo;", "”");
					workArrangement2.setOtherJob(otherJob);
				}
			}
		}
		
		Map<String, Object> map = Maps.newConcurrentMap();
		map.put("arrangmentList", arrangmentList);
		map.put("workArrangementSetting", workArrangementSetting);
		
		String templateFile = "/WEB-INF/classes/templates/modules/word/";
		String ftlPath = Servlets.getRealPath(request, templateFile);
		String ftlname = "workarrangement.ftl";
		String filename = StringEscapeUtils.unescapeHtml4(workArrangementSetting.getTitle());
		
		try {
			WordUtils.exportMillCertificateWord(request, response, map, filename, ftlname,ftlPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

	@RequiresPermissions("xingzhengguanli:workArrangementSetting:edit")
	@RequestMapping(value = "save")
	public String save(WorkArrangementSetting workArrangementSetting, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workArrangementSetting)){
			return form(workArrangementSetting, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(workArrangementSetting.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		workArrangementSettingService.save(workArrangementSetting);
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:edit")
	@RequestMapping(value = "disable")
	public String disable(WorkArrangementSetting workArrangementSetting, RedirectAttributes redirectAttributes) {
		workArrangementSettingService.disable(workArrangementSetting);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkArrangementSetting workArrangementSetting, RedirectAttributes redirectAttributes) {
		workArrangementSettingService.delete(workArrangementSetting);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(WorkArrangementSetting workArrangementSetting, RedirectAttributes redirectAttributes) {
		workArrangementSettingService.deleteByIds(workArrangementSetting);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WorkArrangementSetting workArrangementSetting, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			workArrangementSetting.setPage(new Page<WorkArrangementSetting>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkArrangementSetting.class).setDataList(workArrangementSettingService.findList(workArrangementSetting)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(WorkArrangementSetting workArrangementSetting, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<WorkArrangementSetting> list = workArrangementSettingService.findPage(new Page<WorkArrangementSetting>(1, 5), new WorkArrangementSetting()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new WorkArrangementSetting());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkArrangementSetting.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:workArrangementSetting:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(WorkArrangementSetting workArrangementSetting, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkArrangementSetting> list = ei.getDataList(WorkArrangementSetting.class);
			List<WorkArrangementSetting> insertList=new ArrayList<WorkArrangementSetting>();
			List<WorkArrangementSetting> subList=new ArrayList<WorkArrangementSetting>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<WorkArrangementSetting>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(WorkArrangementSetting zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					workArrangementSettingService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementSetting));
		}
	
	
	
	
}