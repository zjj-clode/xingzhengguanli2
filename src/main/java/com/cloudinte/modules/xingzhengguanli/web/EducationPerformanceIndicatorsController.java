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
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.cloudinte.modules.xingzhengguanli.entity.EducationPerformanceIndicators;
import com.cloudinte.modules.xingzhengguanli.service.EducationPerformanceIndicatorsService;

/**
 * 绩效指标信息Controller
 * @author dcl
 * @version 2019-12-16
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationPerformanceIndicators")
public class EducationPerformanceIndicatorsController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "绩效指标信息";

	@Autowired
	private EducationPerformanceIndicatorsService educationPerformanceIndicatorsService;
	
	@ModelAttribute
	public EducationPerformanceIndicators get(@RequestParam(required=false) String id) {
		EducationPerformanceIndicators entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationPerformanceIndicatorsService.get(id);
		}
		if (entity == null){
			entity = new EducationPerformanceIndicators();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationPerformanceIndicators educationPerformanceIndicators, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EducationPerformanceIndicators> page = educationPerformanceIndicatorsService.findPage(new Page<EducationPerformanceIndicators>(request, response), educationPerformanceIndicators); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationPerformanceIndicators");
		setBase64EncodedQueryStringToEntity(request, educationPerformanceIndicators);
		return "modules/xingzhengguanli/educationPerformanceIndicators/educationPerformanceIndicatorsList";
	}

	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:view")
	@RequestMapping(value = "form")
	public String form(EducationPerformanceIndicators educationPerformanceIndicators, Model model) {
		model.addAttribute("educationPerformanceIndicators", educationPerformanceIndicators);
		model.addAttribute("ename", "educationPerformanceIndicators");
		return "modules/xingzhengguanli/educationPerformanceIndicators/educationPerformanceIndicatorsForm";
	}

	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:edit")
	@RequestMapping(value = "save")
	public String save(EducationPerformanceIndicators educationPerformanceIndicators, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationPerformanceIndicators)){
			return form(educationPerformanceIndicators, model);
		}
		if(StringUtils.isNotBlank(educationPerformanceIndicators.getFirstTarget())){
			educationPerformanceIndicators.setFirstTargetName(DictUtils.getDictLabel(educationPerformanceIndicators.getFirstTarget(), "first_target", "无"));
		}
		if(StringUtils.isNotBlank(educationPerformanceIndicators.getTargetType())){
			educationPerformanceIndicators.setSecondTargetName(DictUtils.getDictLabel(educationPerformanceIndicators.getTargetType(), "target_type", "无"));
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationPerformanceIndicators.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationPerformanceIndicatorsService.save(educationPerformanceIndicators);
		return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
	}
	
	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationPerformanceIndicators educationPerformanceIndicators, RedirectAttributes redirectAttributes) {
		educationPerformanceIndicatorsService.disable(educationPerformanceIndicators);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
	}
	
	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationPerformanceIndicators educationPerformanceIndicators, RedirectAttributes redirectAttributes) {
		educationPerformanceIndicatorsService.delete(educationPerformanceIndicators);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
	}
	
	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationPerformanceIndicators educationPerformanceIndicators, RedirectAttributes redirectAttributes) {
		educationPerformanceIndicatorsService.deleteByIds(educationPerformanceIndicators);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationPerformanceIndicators educationPerformanceIndicators, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationPerformanceIndicators.setPage(new Page<EducationPerformanceIndicators>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationPerformanceIndicators.class).setDataList(educationPerformanceIndicatorsService.findList(educationPerformanceIndicators)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationPerformanceIndicators educationPerformanceIndicators, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationPerformanceIndicators> list = educationPerformanceIndicatorsService.findPage(new Page<EducationPerformanceIndicators>(1, 5), new EducationPerformanceIndicators()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationPerformanceIndicators());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationPerformanceIndicators.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationPerformanceIndicators:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationPerformanceIndicators educationPerformanceIndicators, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationPerformanceIndicators> list = ei.getDataList(EducationPerformanceIndicators.class);
			List<EducationPerformanceIndicators> insertList=new ArrayList<EducationPerformanceIndicators>();
			List<EducationPerformanceIndicators> subList=new ArrayList<EducationPerformanceIndicators>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationPerformanceIndicators>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationPerformanceIndicators zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationPerformanceIndicatorsService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationPerformanceIndicators/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationPerformanceIndicators));
		}
	
	
	
	
}