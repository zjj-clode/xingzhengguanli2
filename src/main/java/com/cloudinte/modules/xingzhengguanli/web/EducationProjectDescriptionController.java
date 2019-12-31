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
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectDescription;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectDescriptionService;

/**
 * 项目描述Controller
 * @author dcl
 * @version 2019-12-14
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectDescription")
public class EducationProjectDescriptionController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "项目描述";

	@Autowired
	private EducationProjectDescriptionService educationProjectDescriptionService;
	
	@ModelAttribute
	public EducationProjectDescription get(@RequestParam(required=false) String id) {
		EducationProjectDescription entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectDescriptionService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectDescription();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectDescription:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectDescription educationProjectDescription, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EducationProjectDescription> page = educationProjectDescriptionService.findPage(new Page<EducationProjectDescription>(request, response), educationProjectDescription); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectDescription");
		setBase64EncodedQueryStringToEntity(request, educationProjectDescription);
		return "modules/xingzhengguanli/educationProjectDescription/educationProjectDescriptionList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectDescription:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectDescription educationProjectDescription, Model model) {
		model.addAttribute("educationProjectDescription", educationProjectDescription);
		model.addAttribute("ename", "educationProjectDescription");
		return "modules/xingzhengguanli/educationProjectDescription/educationProjectDescriptionForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectDescription:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectDescription educationProjectDescription, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectDescription)){
			return form(educationProjectDescription, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectDescription.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectDescriptionService.save(educationProjectDescription);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectDescription:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectDescription educationProjectDescription, RedirectAttributes redirectAttributes) {
		educationProjectDescriptionService.disable(educationProjectDescription);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectDescription:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectDescription educationProjectDescription, RedirectAttributes redirectAttributes) {
		educationProjectDescriptionService.delete(educationProjectDescription);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectDescription:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectDescription educationProjectDescription, RedirectAttributes redirectAttributes) {
		educationProjectDescriptionService.deleteByIds(educationProjectDescription);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectDescription:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectDescription educationProjectDescription, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectDescription.setPage(new Page<EducationProjectDescription>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectDescription.class).setDataList(educationProjectDescriptionService.findList(educationProjectDescription)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectDescription:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectDescription educationProjectDescription, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectDescription> list = educationProjectDescriptionService.findPage(new Page<EducationProjectDescription>(1, 5), new EducationProjectDescription()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectDescription());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectDescription.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectDescription:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectDescription educationProjectDescription, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectDescription> list = ei.getDataList(EducationProjectDescription.class);
			List<EducationProjectDescription> insertList=new ArrayList<EducationProjectDescription>();
			List<EducationProjectDescription> subList=new ArrayList<EducationProjectDescription>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectDescription>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectDescription zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectDescriptionService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectDescription/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectDescription));
		}
	
	
	
	
}