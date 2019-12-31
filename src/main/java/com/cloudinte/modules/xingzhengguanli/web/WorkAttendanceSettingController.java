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
import com.cloudinte.modules.xingzhengguanli.entity.WorkAttendanceSetting;
import com.cloudinte.modules.xingzhengguanli.service.WorkAttendanceSettingService;

/**
 * 加班天数设置Controller
 * @author dcl
 * @version 2019-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/workAttendanceSetting")
public class WorkAttendanceSettingController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "加班天数设置";

	@Autowired
	private WorkAttendanceSettingService workAttendanceSettingService;
	
	@ModelAttribute
	public WorkAttendanceSetting get(@RequestParam(required=false) String id) {
		WorkAttendanceSetting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workAttendanceSettingService.get(id);
		}
		if (entity == null){
			entity = new WorkAttendanceSetting();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkAttendanceSetting workAttendanceSetting, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkAttendanceSetting> page = workAttendanceSettingService.findPage(new Page<WorkAttendanceSetting>(request, response), workAttendanceSetting); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "workAttendanceSetting");
		setBase64EncodedQueryStringToEntity(request, workAttendanceSetting);
		return "modules/xingzhengguanli/workAttendanceSetting/workAttendanceSettingList";
	}

	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:view")
	@RequestMapping(value = "form")
	public String form(WorkAttendanceSetting workAttendanceSetting, Model model) {
		model.addAttribute("workAttendanceSetting", workAttendanceSetting);
		model.addAttribute("ename", "workAttendanceSetting");
		return "modules/xingzhengguanli/workAttendanceSetting/workAttendanceSettingForm";
	}

	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:edit")
	@RequestMapping(value = "save")
	public String save(WorkAttendanceSetting workAttendanceSetting, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workAttendanceSetting)){
			return form(workAttendanceSetting, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(workAttendanceSetting.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		workAttendanceSettingService.save(workAttendanceSetting);
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:edit")
	@RequestMapping(value = "disable")
	public String disable(WorkAttendanceSetting workAttendanceSetting, RedirectAttributes redirectAttributes) {
		workAttendanceSettingService.disable(workAttendanceSetting);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkAttendanceSetting workAttendanceSetting, RedirectAttributes redirectAttributes) {
		workAttendanceSettingService.delete(workAttendanceSetting);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(WorkAttendanceSetting workAttendanceSetting, RedirectAttributes redirectAttributes) {
		workAttendanceSettingService.deleteByIds(workAttendanceSetting);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WorkAttendanceSetting workAttendanceSetting, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			workAttendanceSetting.setPage(new Page<WorkAttendanceSetting>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkAttendanceSetting.class).setDataList(workAttendanceSettingService.findList(workAttendanceSetting)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(WorkAttendanceSetting workAttendanceSetting, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<WorkAttendanceSetting> list = workAttendanceSettingService.findPage(new Page<WorkAttendanceSetting>(1, 5), new WorkAttendanceSetting()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new WorkAttendanceSetting());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkAttendanceSetting.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:workAttendanceSetting:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(WorkAttendanceSetting workAttendanceSetting, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkAttendanceSetting> list = ei.getDataList(WorkAttendanceSetting.class);
			List<WorkAttendanceSetting> insertList=new ArrayList<WorkAttendanceSetting>();
			List<WorkAttendanceSetting> subList=new ArrayList<WorkAttendanceSetting>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<WorkAttendanceSetting>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(WorkAttendanceSetting zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					workAttendanceSettingService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendanceSetting/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendanceSetting));
		}
	
	
	
	
}