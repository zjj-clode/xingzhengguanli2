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
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectInfo;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectInfoService;

/**
 * 教育教学项目基本信息Controller
 * @author dcl
 * @version 2019-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectInfo")
public class EducationProjectInfoController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "教育教学项目基本信息";

	@Autowired
	private EducationProjectInfoService educationProjectInfoService;
	
	@ModelAttribute
	public EducationProjectInfo get(@RequestParam(required=false) String id) {
		EducationProjectInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectInfoService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectInfo educationProjectInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EducationProjectInfo> page = educationProjectInfoService.findPage(new Page<EducationProjectInfo>(request, response), educationProjectInfo); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectInfo");
		setBase64EncodedQueryStringToEntity(request, educationProjectInfo);
		return "modules/xingzhengguanli/educationProjectInfo/educationProjectInfoList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectInfo:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectInfo educationProjectInfo, Model model) {
		model.addAttribute("educationProjectInfo", educationProjectInfo);
		model.addAttribute("ename", "educationProjectInfo");
		return "modules/xingzhengguanli/educationProjectInfo/educationProjectInfoForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectInfo:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectInfo educationProjectInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectInfo)){
			return form(educationProjectInfo, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectInfo.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectInfoService.save(educationProjectInfo);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectInfo:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectInfo educationProjectInfo, RedirectAttributes redirectAttributes) {
		educationProjectInfoService.disable(educationProjectInfo);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectInfo educationProjectInfo, RedirectAttributes redirectAttributes) {
		educationProjectInfoService.delete(educationProjectInfo);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectInfo:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectInfo educationProjectInfo, RedirectAttributes redirectAttributes) {
		educationProjectInfoService.deleteByIds(educationProjectInfo);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectInfo:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectInfo educationProjectInfo, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectInfo.setPage(new Page<EducationProjectInfo>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectInfo.class).setDataList(educationProjectInfoService.findList(educationProjectInfo)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectInfo:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectInfo educationProjectInfo, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectInfo> list = educationProjectInfoService.findPage(new Page<EducationProjectInfo>(1, 5), new EducationProjectInfo()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectInfo());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectInfo.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectInfo:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectInfo educationProjectInfo, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectInfo> list = ei.getDataList(EducationProjectInfo.class);
			List<EducationProjectInfo> insertList=new ArrayList<EducationProjectInfo>();
			List<EducationProjectInfo> subList=new ArrayList<EducationProjectInfo>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectInfo>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectInfo zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectInfoService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectInfo/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectInfo));
		}
	
	
	
	
}