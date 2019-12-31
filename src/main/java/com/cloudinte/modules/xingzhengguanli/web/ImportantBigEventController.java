package com.cloudinte.modules.xingzhengguanli.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.common.utils.Zip;
import com.cloudinte.modules.xingzhengguanli.entity.ImportantBigEvent;
import com.cloudinte.modules.xingzhengguanli.service.ImportantBigEventService;
import com.cloudinte.modules.xingzhengguanli.utils.CalendarUtils;
import com.cloudinte.modules.xingzhengguanli.utils.WordUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 三重一大事项Controller
 * @author dcl
 * @version 2019-12-04
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/importantBigEvent")
public class ImportantBigEventController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "三重一大事项";

	@Autowired
	private ImportantBigEventService importantBigEventService;
	
	@ModelAttribute
	public ImportantBigEvent get(@RequestParam(required=false) String id) {
		ImportantBigEvent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = importantBigEventService.get(id);
		}
		if (entity == null){
			entity = new ImportantBigEvent();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:importantBigEvent:view")
	@RequestMapping(value = {"list", ""})
	public String list(ImportantBigEvent importantBigEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			importantBigEvent.setCreateBy(opUser);
		}
		
		Page<ImportantBigEvent> page = importantBigEventService.findPage(new Page<ImportantBigEvent>(request, response), importantBigEvent); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "importantBigEvent");
		setBase64EncodedQueryStringToEntity(request, importantBigEvent);
		return "modules/xingzhengguanli/importantBigEvent/importantBigEventList";
	}

	@RequiresPermissions("xingzhengguanli:importantBigEvent:view")
	@RequestMapping(value = "form")
	public String form(ImportantBigEvent importantBigEvent, Model model) {
		model.addAttribute("importantBigEvent", importantBigEvent);
		model.addAttribute("ename", "importantBigEvent");
		return "modules/xingzhengguanli/importantBigEvent/importantBigEventForm";
	}

	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "save")
	public String save(ImportantBigEvent importantBigEvent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, importantBigEvent)){
			return form(importantBigEvent, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(importantBigEvent.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		importantBigEventService.save(importantBigEvent);
		return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
	}
	
	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "disable")
	public String disable(ImportantBigEvent importantBigEvent, RedirectAttributes redirectAttributes) {
		importantBigEventService.disable(importantBigEvent);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
	}
	
	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "delete")
	public String delete(ImportantBigEvent importantBigEvent, RedirectAttributes redirectAttributes) {
		importantBigEventService.delete(importantBigEvent);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
	}
	
	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(ImportantBigEvent importantBigEvent, RedirectAttributes redirectAttributes) {
		importantBigEventService.deleteByIds(importantBigEvent);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(ImportantBigEvent importantBigEvent, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User opUser = UserUtils.getUser();
			if("6".equals(opUser.getUserType())){//科室
				importantBigEvent.setCreateBy(opUser);
			}
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			importantBigEvent.setPage(new Page<ImportantBigEvent>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, ImportantBigEvent.class).setDataList(importantBigEventService.findList(importantBigEvent)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:importantBigEvent:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(ImportantBigEvent importantBigEvent, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<ImportantBigEvent> list = importantBigEventService.findPage(new Page<ImportantBigEvent>(1, 5), new ImportantBigEvent()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new ImportantBigEvent());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, ImportantBigEvent.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(ImportantBigEvent importantBigEvent, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ImportantBigEvent> list = ei.getDataList(ImportantBigEvent.class);
			List<ImportantBigEvent> insertList=new ArrayList<ImportantBigEvent>();
			List<ImportantBigEvent> subList=new ArrayList<ImportantBigEvent>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<ImportantBigEvent>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(ImportantBigEvent zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					importantBigEventService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
		}
	
	/**
	 * 下载会议纪要
	 * @param importantBigEvent
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "downloadMinutesOfMeeting")
	public String downloadMinutesOfMeeting(ImportantBigEvent importantBigEvent, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			
			if(StringUtils.isNotBlank(importantBigEvent.getId())){
				
				List<ImportantBigEvent> eventList = new ArrayList<ImportantBigEvent>();
				if(importantBigEvent.getMeetingDate() != null){
					importantBigEvent.setEventdate(DateUtils.formatDate(importantBigEvent.getMeetingDate(), "yyyy年MM月dd日  HH:mm"));
				}
				String meeingContent = importantBigEvent.getMeeingContent();
				if(StringUtils.isNotBlank(meeingContent)){
					meeingContent = meeingContent.replaceAll("&mdash;", "-");
					meeingContent = meeingContent.replaceAll("&ldquo;", "“");
					meeingContent = meeingContent.replaceAll("&rdquo;", "”");
					importantBigEvent.setMeeingContent(meeingContent);
				}
				eventList.add(importantBigEvent);
				Map<String, Object> map = Maps.newConcurrentMap();
				map.put("eventList", eventList);
				
				String templateFile = "/WEB-INF/classes/templates/modules/word/";
				String ftlPath = Servlets.getRealPath(request, templateFile);
				String ftlname = "MinutesOfMeeting.ftl";
				String filename = StringEscapeUtils.unescapeHtml4("会议纪要");
				
				WordUtils.exportMillCertificateWord(request, response, map, filename, ftlname,ftlPath);
				
			}else{
				return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
			}
			
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
		}
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:importantBigEvent:edit")
	@RequestMapping(value = "batchDownloadMinutesOfMeeting", method = RequestMethod.POST)
	public String batchDownloadMinutesOfMeeting(ImportantBigEvent importantBigEvent, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User opUser = UserUtils.getUser();
			if("6".equals(opUser.getUserType())){//科室
				importantBigEvent.setCreateBy(opUser);
			}
			List<ImportantBigEvent> eventList = importantBigEventService.findList(importantBigEvent);
			if(eventList != null && eventList.size() != 0){
				Map<String, List<ImportantBigEvent>> resultMap = new HashMap<String, List<ImportantBigEvent>>();
				for (ImportantBigEvent importantBigEvent2 : eventList) {
					
					String meetingDate = "";
					if(importantBigEvent2.getMeetingDate() != null){
						meetingDate = DateUtils.formatDate(importantBigEvent2.getMeetingDate(), "yyyy-MM-dd");
					}
					
					if(importantBigEvent2.getMeetingDate() != null){
						importantBigEvent2.setEventdate(DateUtils.formatDate(importantBigEvent2.getMeetingDate(), "yyyy年MM月dd日  HH:mm"));
					}
					String meeingContent = importantBigEvent2.getMeeingContent();
					if(StringUtils.isNotBlank(meeingContent)){
						meeingContent = meeingContent.replaceAll("&mdash;", "-");
						meeingContent = meeingContent.replaceAll("&ldquo;", "“");
						meeingContent = meeingContent.replaceAll("&rdquo;", "”");
						importantBigEvent2.setMeeingContent(meeingContent);
					}
					
					if(StringUtils.isNotBlank(meetingDate)){
						
						List<ImportantBigEvent> dateList = resultMap.get(meetingDate);
						if(dateList == null || dateList.size() == 0){
							dateList = new ArrayList<ImportantBigEvent>();
						}
						dateList.add(importantBigEvent2);
						resultMap.put(meetingDate, dateList);
						
					}
				}
				
				 String path = request.getSession().getServletContext().getRealPath("/"); 
		    	 String directory = path  +"upload"+File.separator +"meeting"+File.separator +"zip";
		    	 
		    	 String zipname=path+"upload"+File.separator +"meeting"+File.separator +"会议纪要.zip";
				
				Iterator<Entry<String, List<ImportantBigEvent>>> entries = resultMap.entrySet().iterator();
				while(entries.hasNext()){
				    Entry<String, List<ImportantBigEvent>> entry = entries.next();
				    String key = entry.getKey();
				    List<ImportantBigEvent> value = entry.getValue();
				    
				    Map<String, Object> map = Maps.newConcurrentMap();
					map.put("eventList", value);
					
					String templateFile = "/WEB-INF/classes/templates/modules/word/";
					String ftlPath = Servlets.getRealPath(request, templateFile);
					String ftlname = "MinutesOfMeeting.ftl";
					String filename =  StringEscapeUtils.unescapeHtml4(key+"会议纪要");
					
					String filePath = path  +"upload"+File.separator +"meeting"+File.separator +"zip"+File.separator + filename + ".doc";
					
					WordUtils.createDoc(map, filePath, ftlname,ftlPath);
				    
				}
				try {
						zipname = StringEscapeUtils.unescapeHtml4(zipname);
					
					   Zip zip=new Zip();
					   zip.doZip(path+"upload"+File.separator +"meeting"+File.separator +"zip"+File.separator,zipname);
						
					   File previewFile = new File(zipname);   
			           InputStream is = new FileInputStream(previewFile);   
			           response.reset();   
			           response.setContentType("application/vnd.ms-word;charset=UTF-8"); 
			           String filename="会议纪要.zip";
			          
			           response.addHeader("Content-Disposition","attachment; filename=\"" + new String(filename.getBytes("gb2312"), "ISO8859-1" )  + "\""); 
			           byte[] b = new byte[1024];   
			           int len;   
			           while ((len=is.read(b)) >0) {   
			        	   response.getOutputStream().write(b,0,len);   
			           }   
			           is.close();   
			           response.getOutputStream().flush();   
			           response.getOutputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					FileUtils.deleteFileByDirectory(directory);
					FileUtils.deleteFile(zipname);
				}
			}else{
				addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：没有数据！" );
				return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));

			}
			
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			importantBigEvent.setPage(new Page<ImportantBigEvent>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, ImportantBigEvent.class).setDataList(importantBigEventService.findList(importantBigEvent)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/importantBigEvent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(importantBigEvent));
		}
	}
	
	
	@RequestMapping(value = "checkMeetingTime", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkMeetingTime(String meetingDate,String eventId,ImportantBigEvent importantBigEvent, HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(meetingDate)){
			
			try {
				meetingDate += ":00";
				int maxMeetingDate = SettingsUtils.getSysConfig("max.meeting.date", 3);
				Date currentDate = DateUtils.parseDate(meetingDate, "yyyy-MM-dd HH:mm:ss");
				String statrtDate = CalendarUtils.getMondayOfThisWeek(currentDate)+" 00:00:00";
				String endDate = CalendarUtils.getSundayOfThisWeek(currentDate)+" 23:59:59";
				
				Date meetingStartDate = DateUtils.parseDate(statrtDate, "yyyy-MM-dd HH:mm:ss");
				Date meetingEndDate = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
				
				importantBigEvent.setMeetingStartDate(meetingStartDate);
				importantBigEvent.setMeetingEndDate(meetingEndDate);
				
				List<ImportantBigEvent> eventList = importantBigEventService.findList(importantBigEvent);
				if(eventList != null && eventList.size() != 0){
					if( eventList.size() < maxMeetingDate){
						result.put("re", "ok");
						result.put("message", "时间填写在该周内没超过三次");
					}else if(eventList.size() == maxMeetingDate){
						
						String compareDate = DateUtils.formatDate(currentDate, "yyyy-MM-dd");
						boolean isInThreeDays = false;
						String dates = "";
						for (ImportantBigEvent importantBigEvent2 : eventList) {
							if(importantBigEvent2.getMeetingDate() != null){
								String meetingTime = DateUtils.formatDate(importantBigEvent2.getMeetingDate(), "yyyy-MM-dd");
								dates += meetingTime+"，";
								if(compareDate.equals(meetingTime)){
									isInThreeDays = true;
									break;
								}
								if(StringUtils.isNotBlank(eventId) && eventId.equals(importantBigEvent2.getId())){
									isInThreeDays = true;
									break;
								}
							}
						}
						if(isInThreeDays){
							result.put("re", "ok");
							result.put("message", "时间填写在该周内填写的三天时间之内");
						}else{
							result.put("re", "error");
							result.put("message", "时间填写在该周内填写的三天时间之外，请选择以下几天中的任意一天："+dates);
						}
					}else{
						
						String dates = "";
						for (ImportantBigEvent importantBigEvent2 : eventList) {
							if(importantBigEvent2.getMeetingDate() != null){
								String meetingTime = DateUtils.formatDate(importantBigEvent2.getMeetingDate(), "yyyy-MM-dd");
								dates += meetingTime+"，";
								
							}
						}
						result.put("re", "error");
						result.put("message", "时间填写在该周内填写的三天时间之外，请选择以下几天中的任意一天："+dates);
					}
					
				}else{
					result.put("re", "ok");
					result.put("message", "时间填写在该周内没超过三天");
				}
				
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			
			
		}else{
			result.put("re", "error");
			result.put("message", "参数错误");
		}
		
		return result;
	 
	}
	
}