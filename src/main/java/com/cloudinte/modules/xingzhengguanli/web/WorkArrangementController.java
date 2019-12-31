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
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangement;
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementContent;
import com.cloudinte.modules.xingzhengguanli.service.WorkArrangementContentService;
import com.cloudinte.modules.xingzhengguanli.service.WorkArrangementService;
import com.cloudinte.modules.xingzhengguanli.utils.CalendarUtils;

/**
 * 工作安排Controller
 * @author dcl
 * @version 2019-12-06
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/workArrangement")
public class WorkArrangementController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "工作安排";

	@Autowired
	private WorkArrangementService workArrangementService;
	
	@Autowired
	private WorkArrangementContentService workArrangementContentService;
	
	@ModelAttribute
	public WorkArrangement get(@RequestParam(required=false) String id) {
		WorkArrangement entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workArrangementService.get(id);
		}
		if (entity == null){
			entity = new WorkArrangement();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangement:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkArrangement workArrangement, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			workArrangement.setCreateBy(opUser);
		}
		
		Page<WorkArrangement> page = workArrangementService.findPage(new Page<WorkArrangement>(request, response), workArrangement); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "workArrangement");
		setBase64EncodedQueryStringToEntity(request, workArrangement);
		return "modules/xingzhengguanli/workArrangement/workArrangementList";
	}

	@RequiresPermissions("xingzhengguanli:workArrangement:view")
	@RequestMapping(value = "form")
	public String form(WorkArrangement workArrangement, Model model) {
		
		User opUser = UserUtils.getUser();
		WorkArrangementContent workArrangementContent = new WorkArrangementContent();
		workArrangementContent.setUser(opUser);
		List<WorkArrangementContent> arrangementContentList = workArrangementContentService.findList(workArrangementContent);
		if(arrangementContentList != null && arrangementContentList.size() != 0){
			
			workArrangementContent = arrangementContentList.get(0);
			model.addAttribute("workArrangementContent", workArrangementContent);
		}else{
			workArrangementContentService.save(workArrangementContent);
			model.addAttribute("workArrangementContent", workArrangementContent);
		}
		
		if(StringUtils.isBlank(workArrangement.getId())){
			workArrangement.setStartDate(CalendarUtils.getMondayOfThisWeekDate());
			workArrangement.setEndDate(CalendarUtils.getSundayOfThisWeekDate());
			workArrangement.setOtherJob(workArrangementContent.getOtherJob());
			workArrangement.setContentTitle("本周重点工作");
		}
		
		model.addAttribute("workArrangement", workArrangement);
		model.addAttribute("ename", "workArrangement");
		return "modules/xingzhengguanli/workArrangement/workArrangementForm";
	}

	@RequiresPermissions("xingzhengguanli:workArrangement:edit")
	@RequestMapping(value = "save")
	public String save(WorkArrangement workArrangement, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workArrangement)){
			return form(workArrangement, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(workArrangement.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		workArrangementService.saveWorkArrangement(workArrangement);
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangement:edit")
	@RequestMapping(value = "disable")
	public String disable(WorkArrangement workArrangement, RedirectAttributes redirectAttributes) {
		workArrangementService.disable(workArrangement);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangement:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkArrangement workArrangement, RedirectAttributes redirectAttributes) {
		workArrangementService.delete(workArrangement);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangement:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(WorkArrangement workArrangement, RedirectAttributes redirectAttributes) {
		workArrangementService.deleteByIds(workArrangement);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:workArrangement:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WorkArrangement workArrangement, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			workArrangement.setPage(new Page<WorkArrangement>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkArrangement.class).setDataList(workArrangementService.findList(workArrangement)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:workArrangement:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(WorkArrangement workArrangement, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<WorkArrangement> list = workArrangementService.findPage(new Page<WorkArrangement>(1, 5), new WorkArrangement()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new WorkArrangement());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkArrangement.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:workArrangement:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(WorkArrangement workArrangement, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkArrangement> list = ei.getDataList(WorkArrangement.class);
			List<WorkArrangement> insertList=new ArrayList<WorkArrangement>();
			List<WorkArrangement> subList=new ArrayList<WorkArrangement>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<WorkArrangement>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(WorkArrangement zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					workArrangementService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangement/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangement));
		}
	
	
	
	
}