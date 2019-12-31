package com.cloudinte.modules.xingzhengguanli.web;
import com.thinkgem.jeesite.common.config.Global;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeeting;
import com.cloudinte.modules.xingzhengguanli.entity.WorkRegularMeetingContent;
import com.cloudinte.modules.xingzhengguanli.service.WorkRegularMeetingContentService;
import com.cloudinte.modules.xingzhengguanli.service.WorkRegularMeetingService;

/**
 * 例会通报工作事项Controller
 * @author dcl
 * @version 2019-12-11
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/workRegularMeetingContent")
public class WorkRegularMeetingContentController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "例会通报工作事项";

	@Autowired
	private WorkRegularMeetingContentService workRegularMeetingContentService;
	
	@Autowired
	private WorkRegularMeetingService workRegularMeetingService;
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public WorkRegularMeetingContent get(@RequestParam(required=false) String id) {
		WorkRegularMeetingContent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workRegularMeetingContentService.get(id);
		}
		if (entity == null){
			entity = new WorkRegularMeetingContent();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkRegularMeetingContent workRegularMeetingContent, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		WorkRegularMeeting workRegularMeeting = new WorkRegularMeeting();
		List<WorkRegularMeeting> regularMeetingList = workRegularMeetingService.findList(workRegularMeeting);
		model.addAttribute("regularMeetingList", regularMeetingList);
		
		
		User user = new User();
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){
			workRegularMeetingContent.setCreateBy(opUser);
			user.setOffice(opUser.getOffice());
		}
		List<User> userList = systemService.findUser(user);
		model.addAttribute("userList", userList);
		Page<WorkRegularMeetingContent> page = workRegularMeetingContentService.findPage(new Page<WorkRegularMeetingContent>(request, response), workRegularMeetingContent); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "workRegularMeetingContent");
		setBase64EncodedQueryStringToEntity(request, workRegularMeetingContent);
		return "modules/xingzhengguanli/workRegularMeetingContent/workRegularMeetingContentList";
	}

	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:view")
	@RequestMapping(value = "form")
	public String form(WorkRegularMeetingContent workRegularMeetingContent, Model model) {
		
		WorkRegularMeeting workRegularMeeting = new WorkRegularMeeting();
		List<WorkRegularMeeting> regularMeetingList = workRegularMeetingService.findList(workRegularMeeting);
		model.addAttribute("regularMeetingList", regularMeetingList);
		
		User opUser = UserUtils.getUser();
		if(StringUtils.isBlank(workRegularMeetingContent.getId())){
			workRegularMeetingContent.setUser(opUser);
		}
		model.addAttribute("workRegularMeetingContent", workRegularMeetingContent);
		model.addAttribute("ename", "workRegularMeetingContent");
		return "modules/xingzhengguanli/workRegularMeetingContent/workRegularMeetingContentForm";
	}

	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:edit")
	@RequestMapping(value = "save")
	public String save(WorkRegularMeetingContent workRegularMeetingContent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workRegularMeetingContent)){
			return form(workRegularMeetingContent, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(workRegularMeetingContent.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		workRegularMeetingContentService.save(workRegularMeetingContent);
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:edit")
	@RequestMapping(value = "disable")
	public String disable(WorkRegularMeetingContent workRegularMeetingContent, RedirectAttributes redirectAttributes) {
		workRegularMeetingContentService.disable(workRegularMeetingContent);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkRegularMeetingContent workRegularMeetingContent, RedirectAttributes redirectAttributes) {
		workRegularMeetingContentService.delete(workRegularMeetingContent);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
	}
	
	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(WorkRegularMeetingContent workRegularMeetingContent, RedirectAttributes redirectAttributes) {
		workRegularMeetingContentService.deleteByIds(workRegularMeetingContent);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WorkRegularMeetingContent workRegularMeetingContent, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			workRegularMeetingContent.setPage(new Page<WorkRegularMeetingContent>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkRegularMeetingContent.class).setDataList(workRegularMeetingContentService.findList(workRegularMeetingContent)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(WorkRegularMeetingContent workRegularMeetingContent, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<WorkRegularMeetingContent> list = workRegularMeetingContentService.findPage(new Page<WorkRegularMeetingContent>(1, 5), new WorkRegularMeetingContent()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new WorkRegularMeetingContent());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkRegularMeetingContent.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:workRegularMeetingContent:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(WorkRegularMeetingContent workRegularMeetingContent, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkRegularMeetingContent> list = ei.getDataList(WorkRegularMeetingContent.class);
			List<WorkRegularMeetingContent> insertList=new ArrayList<WorkRegularMeetingContent>();
			List<WorkRegularMeetingContent> subList=new ArrayList<WorkRegularMeetingContent>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<WorkRegularMeetingContent>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(WorkRegularMeetingContent zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					workRegularMeetingContentService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/workRegularMeetingContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workRegularMeetingContent));
		}
	
	
	
	
}