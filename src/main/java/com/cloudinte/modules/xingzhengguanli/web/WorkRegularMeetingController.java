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
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeeting;
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeetingContent;
import com.cloudinte.modules.xingzhengguanli.service.WorkRegularMeetingContentService;
import com.cloudinte.modules.xingzhengguanli.service.WorkRegularMeetingService;
import com.cloudinte.modules.xingzhengguanli.utils.NumberUtils;
import com.cloudinte.modules.xingzhengguanli.utils.WordUtils;

/**
 * 工作例会征集Controller
 * @author dcl
 * @version 2019-12-11
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/workRegularMeeting")
public class WorkRegularMeetingController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "工作例会征集";

	@Autowired
	private WorkRegularMeetingService workRegularMeetingService;
	
	@Autowired
	private WorkRegularMeetingContentService workRegularMeetingContentService;
	
	@ModelAttribute
	public WorkRegularMeeting get(@RequestParam(required=false) String id) {
		WorkRegularMeeting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workRegularMeetingService.get(id);
		}
		if (entity == null){
			entity = new WorkRegularMeeting();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkRegularMeeting workRegularMeeting, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkRegularMeeting> page = workRegularMeetingService.findPage(new Page<WorkRegularMeeting>(request, response), workRegularMeeting); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "workRegularMeeting");
		setBase64EncodedQueryStringToEntity(request, workRegularMeeting);
		return "modules/xingzhengguanli/workRegularMeeting/workRegularMeetingList";
	}

	@RequiresPermissions("xingzhengguanli:workRegularMeeting:view")
	@RequestMapping(value = "form")
	public String form(WorkRegularMeeting workRegularMeeting, Model model) {
		model.addAttribute("workRegularMeeting", workRegularMeeting);
		model.addAttribute("ename", "workRegularMeeting");
		return "modules/xingzhengguanli/workRegularMeeting/workRegularMeetingForm";
	}
	
	
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:view")
	@RequestMapping(value = "showRegularMeetingContent")
	public String showRegularMeetingContent(WorkRegularMeeting workRegularMeeting, Model model) {
		
		WorkRegularMeetingContent workRegularMeetingContent = new WorkRegularMeetingContent();
		workRegularMeetingContent.setRegularMeeting(workRegularMeeting);
		List<WorkRegularMeetingContent> contentList = workRegularMeetingContentService.findList(workRegularMeetingContent);
		
		model.addAttribute("contentList", contentList);
		
		model.addAttribute("workRegularMeeting", workRegularMeeting);
		model.addAttribute("ename", "workRegularMeeting");
		return "modules/xingzhengguanli/workRegularMeeting/showRegularMeetingContent";
	}
	
	@RequestMapping(value = "downloadWorkArrangement")
	public String downloadWorkArrangement(WorkRegularMeeting workRegularMeeting, Model model,
			HttpServletRequest request,HttpServletResponse response) {
		
		WorkRegularMeetingContent workRegularMeetingContent = new WorkRegularMeetingContent();
		workRegularMeetingContent.setRegularMeeting(workRegularMeeting);
		List<WorkRegularMeetingContent> contentList = workRegularMeetingContentService.findList(workRegularMeetingContent);
		String content = "";
		if(contentList != null && contentList.size() != 0){
			for (int i = 0; i < contentList.size(); i++) {
				WorkRegularMeetingContent meetingContent = contentList.get(i);
				String con = meetingContent.getContent();
				if(StringUtils.isNotBlank(con)){
					con = con.replaceAll("\r\n", "<w:br/>");
					con = con.replaceAll("&mdash;", "-");
					con = con.replaceAll("&ldquo;", "“");
					con = con.replaceAll("&rdquo;", "”");
					
					content += "事项"+NumberUtils.foematInteger(i+1)+"："+"<w:br/>"+con+"<w:br/>";
				}
			}
		}
		
		if(workRegularMeeting.getMeetingDate() != null){
			workRegularMeeting.setMeetingTime(DateUtils.formatDate(workRegularMeeting.getMeetingDate(), "yyyy-MM-dd HH:mm:ss"));
		}
		
		Map<String, Object> map = Maps.newConcurrentMap();
		map.put("workRegularMeeting", workRegularMeeting);
		map.put("content", content);
		
		String templateFile = "/WEB-INF/classes/templates/modules/word/";
		String ftlPath = Servlets.getRealPath(request, templateFile);
		String ftlname = "regularmeeting.ftl";
		String filename = StringEscapeUtils.unescapeHtml4(workRegularMeeting.getTitle());
		
		try {
			WordUtils.exportMillCertificateWord(request, response, map, filename, ftlname,ftlPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	@RequiresPermissions("xingzhengguanli:workRegularMeeting:edit")
	@RequestMapping(value = "save")
	public String save(WorkRegularMeeting workRegularMeeting, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workRegularMeeting)){
			return form(workRegularMeeting, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(workRegularMeeting.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		workRegularMeetingService.save(workRegularMeeting);
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:edit")
	@RequestMapping(value = "disable")
	public String disable(WorkRegularMeeting workRegularMeeting, RedirectAttributes redirectAttributes) {
		workRegularMeetingService.disable(workRegularMeeting);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkRegularMeeting workRegularMeeting, RedirectAttributes redirectAttributes) {
		workRegularMeetingService.delete(workRegularMeeting);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(WorkRegularMeeting workRegularMeeting, RedirectAttributes redirectAttributes) {
		workRegularMeetingService.deleteByIds(workRegularMeeting);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WorkRegularMeeting workRegularMeeting, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			workRegularMeeting.setPage(new Page<WorkRegularMeeting>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkRegularMeeting.class).setDataList(workRegularMeetingService.findList(workRegularMeeting)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(WorkRegularMeeting workRegularMeeting, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<WorkRegularMeeting> list = workRegularMeetingService.findPage(new Page<WorkRegularMeeting>(1, 5), new WorkRegularMeeting()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new WorkRegularMeeting());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkRegularMeeting.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:workRegularMeeting:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(WorkRegularMeeting workRegularMeeting, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkRegularMeeting> list = ei.getDataList(WorkRegularMeeting.class);
			List<WorkRegularMeeting> insertList=new ArrayList<WorkRegularMeeting>();
			List<WorkRegularMeeting> subList=new ArrayList<WorkRegularMeeting>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<WorkRegularMeeting>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(WorkRegularMeeting zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					workRegularMeetingService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeeting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeeting));
		}
	
	
	
	
}