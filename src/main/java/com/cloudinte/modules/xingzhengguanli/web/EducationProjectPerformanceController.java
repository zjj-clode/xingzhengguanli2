package com.cloudinte.modules.xingzhengguanli.web;
import com.thinkgem.jeesite.common.config.Global;

import java.util.Date;
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
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cloudinte.modules.xingzhengguanli.entity.EducationPerformanceIndicators;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectPerformance;
import com.cloudinte.modules.xingzhengguanli.service.EducationPerformanceIndicatorsService;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectPerformanceService;

/**
 * 项目支出绩效目标申报Controller
 * @author dcl
 * @version 2019-12-16
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectPerformance")
public class EducationProjectPerformanceController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "项目支出绩效目标申报";

	@Autowired
	private EducationProjectPerformanceService educationProjectPerformanceService;
	
	@Autowired
	private EducationPerformanceIndicatorsService educationPerformanceIndicatorsService;
	
	@ModelAttribute
	public EducationProjectPerformance get(@RequestParam(required=false) String id) {
		EducationProjectPerformance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectPerformanceService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectPerformance();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectPerformance educationProjectPerformance, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			educationProjectPerformance.setUser(opUser);
		}
		
		
		Page<EducationProjectPerformance> page = educationProjectPerformanceService.findPage(new Page<EducationProjectPerformance>(request, response), educationProjectPerformance); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectPerformance");
		setBase64EncodedQueryStringToEntity(request, educationProjectPerformance);
		return "modules/xingzhengguanli/educationProjectPerformance/educationProjectPerformanceList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectPerformance educationProjectPerformance, Model model) {
		
		User opUser = UserUtils.getUser();
		if(StringUtils.isNotBlank(educationProjectPerformance.getId())){
			educationProjectPerformance.setUser(opUser);
			
			String currentYear = DateUtils.formatDate(new Date(), "yyyy");
			String projectStartYear = String.valueOf(Integer.valueOf(currentYear) + 1) ;
			educationProjectPerformance.setYear(projectStartYear);
			
			educationProjectPerformance.setProjectName(projectStartYear + SettingsUtils.getSysConfig("projectImplementationName", "年253教育教学改革专项"));
			educationProjectPerformance.setProjectCode(SettingsUtils.getSysConfig("projectImplementationCode", "[105]教育部"));
			educationProjectPerformance.setProjectUnit(SettingsUtils.getSysConfig("projectUnit", "中国政法大学"));
			educationProjectPerformance.setProjectCategory(SettingsUtils.getSysConfig("projectAttribute", "新增项目"));
			educationProjectPerformance.setProjectCycle(SettingsUtils.getSysConfig("projectCycle", "1年"));
			
			
		}
		
		model.addAttribute("educationProjectPerformance", educationProjectPerformance);
		model.addAttribute("ename", "educationProjectPerformance");
		return "modules/xingzhengguanli/educationProjectPerformance/educationProjectPerformanceForm";
	}
	
	@RequestMapping(value = "batchform")
	public String batchform(EducationProjectPerformance educationProjectPerformance, Model model) {
		
		User opUser = UserUtils.getUser();
		if(StringUtils.isNotBlank(educationProjectPerformance.getId())){
			educationProjectPerformance.setUser(opUser);
			
			String currentYear = DateUtils.formatDate(new Date(), "yyyy");
			String projectStartYear = String.valueOf(Integer.valueOf(currentYear) + 1) ;
			educationProjectPerformance.setYear(projectStartYear);
			
			educationProjectPerformance.setProjectName(projectStartYear + SettingsUtils.getSysConfig("projectImplementationName", "年253教育教学改革专项"));
			educationProjectPerformance.setProjectCode(SettingsUtils.getSysConfig("projectImplementationCode", "[105]教育部"));
			educationProjectPerformance.setProjectUnit(SettingsUtils.getSysConfig("projectUnit", "中国政法大学"));
			educationProjectPerformance.setProjectCategory(SettingsUtils.getSysConfig("projectAttribute", "新增项目"));
			educationProjectPerformance.setProjectCycle(SettingsUtils.getSysConfig("projectCycle", "1年"));
		}
		
		EducationPerformanceIndicators performanceIndicators = new EducationPerformanceIndicators();
		List<EducationPerformanceIndicators> performanceIndicatorsList = educationPerformanceIndicatorsService.findList(performanceIndicators);
		model.addAttribute("performanceIndicatorsList", performanceIndicatorsList);
		
		model.addAttribute("educationProjectPerformance", educationProjectPerformance);
		model.addAttribute("ename", "educationProjectPerformance");
		return "modules/xingzhengguanli/educationProjectPerformance/batchEditPerformanceForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectPerformance educationProjectPerformance, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectPerformance)){
			return form(educationProjectPerformance, model);
		}
		
		if(StringUtils.isNotBlank(educationProjectPerformance.getShortTermFirstTarget())){
			educationProjectPerformance.setShortTermFirstTargetName(DictUtils.getDictLabel(educationProjectPerformance.getShortTermFirstTarget(), "first_target", "无"));
		}
		if(StringUtils.isNotBlank(educationProjectPerformance.getShortTermTargetType())){
			educationProjectPerformance.setShortTermSecondTargetName(DictUtils.getDictLabel(educationProjectPerformance.getShortTermTargetType(), "target_type", "无"));
		}
		if(StringUtils.isNotBlank(educationProjectPerformance.getMetaphaseTargetType())){
			educationProjectPerformance.setMetaphaseSecondTargetName(DictUtils.getDictLabel(educationProjectPerformance.getMetaphaseTargetType(), "target_type", "无"));
		}
		
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectPerformance.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectPerformanceService.save(educationProjectPerformance);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectPerformance educationProjectPerformance, RedirectAttributes redirectAttributes) {
		educationProjectPerformanceService.disable(educationProjectPerformance);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectPerformance educationProjectPerformance, RedirectAttributes redirectAttributes) {
		educationProjectPerformanceService.delete(educationProjectPerformance);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectPerformance educationProjectPerformance, RedirectAttributes redirectAttributes) {
		educationProjectPerformanceService.deleteByIds(educationProjectPerformance);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectPerformance educationProjectPerformance, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectPerformance.setPage(new Page<EducationProjectPerformance>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectPerformance.class).setDataList(educationProjectPerformanceService.findList(educationProjectPerformance)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectPerformance educationProjectPerformance, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectPerformance> list = educationProjectPerformanceService.findPage(new Page<EducationProjectPerformance>(1, 5), new EducationProjectPerformance()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectPerformance());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectPerformance.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectPerformance:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectPerformance educationProjectPerformance, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectPerformance> list = ei.getDataList(EducationProjectPerformance.class);
			List<EducationProjectPerformance> insertList=new ArrayList<EducationProjectPerformance>();
			List<EducationProjectPerformance> subList=new ArrayList<EducationProjectPerformance>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectPerformance>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectPerformance zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectPerformanceService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectPerformance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectPerformance));
		}
	
	
	
	
}