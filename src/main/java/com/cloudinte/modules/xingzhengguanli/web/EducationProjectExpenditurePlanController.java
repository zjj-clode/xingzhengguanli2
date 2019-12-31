package com.cloudinte.modules.xingzhengguanli.web;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpenditureDetails;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpenditurePlan;
import com.cloudinte.modules.xingzhengguanli.entity.ExportExpenditurePlan;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectExpenditureDetailsService;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectExpenditurePlanService;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 项目支出计划Controller
 * @author dcl
 * @version 2019-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectExpenditurePlan")
public class EducationProjectExpenditurePlanController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "项目支出计划";

	@Autowired
	private EducationProjectExpenditurePlanService educationProjectExpenditurePlanService;
	
	@Autowired
	private EducationProjectExpenditureDetailsService educationProjectExpenditureDetailsService;
	
	@ModelAttribute
	public EducationProjectExpenditurePlan get(@RequestParam(required=false) String id) {
		EducationProjectExpenditurePlan entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectExpenditurePlanService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectExpenditurePlan();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectExpenditurePlan educationProjectExpenditurePlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			educationProjectExpenditurePlan.setUser(opUser);
		}
		
		EducationProjectExpenditureDetails details = new EducationProjectExpenditureDetails();
		List<EducationProjectExpenditureDetails> detailsList = educationProjectExpenditureDetailsService.findList(details);
		model.addAttribute("detailsList", detailsList);
		
		Page<EducationProjectExpenditurePlan> page = educationProjectExpenditurePlanService.findPage(new Page<EducationProjectExpenditurePlan>(request, response), educationProjectExpenditurePlan); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectExpenditurePlan");
		setBase64EncodedQueryStringToEntity(request, educationProjectExpenditurePlan);
		return "modules/xingzhengguanli/educationProjectExpenditurePlan/educationProjectExpenditurePlanList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectExpenditurePlan educationProjectExpenditurePlan, Model model) {
		
		if(StringUtils.isBlank(educationProjectExpenditurePlan.getId())){
			User opUser = UserUtils.getUser();
			educationProjectExpenditurePlan.setUser(opUser);
			
			String currentYear = DateUtils.formatDate(new Date(), "yyyy");
			String projectStartYear = String.valueOf(Integer.valueOf(currentYear) + 1) ;
			educationProjectExpenditurePlan.setExpenditureYear(projectStartYear);
			
		}
		
		EducationProjectExpenditureDetails details = new EducationProjectExpenditureDetails();
		List<EducationProjectExpenditureDetails> detailsList = educationProjectExpenditureDetailsService.findList(details);
		model.addAttribute("detailsList", detailsList);
		
		model.addAttribute("educationProjectExpenditurePlan", educationProjectExpenditurePlan);
		model.addAttribute("ename", "educationProjectExpenditurePlan");
		return "modules/xingzhengguanli/educationProjectExpenditurePlan/educationProjectExpenditurePlanForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectExpenditurePlan educationProjectExpenditurePlan, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectExpenditurePlan)){
			return form(educationProjectExpenditurePlan, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectExpenditurePlan.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectExpenditurePlanService.save(educationProjectExpenditurePlan);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectExpenditurePlan educationProjectExpenditurePlan, RedirectAttributes redirectAttributes) {
		educationProjectExpenditurePlanService.disable(educationProjectExpenditurePlan);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectExpenditurePlan educationProjectExpenditurePlan, RedirectAttributes redirectAttributes) {
		educationProjectExpenditurePlanService.delete(educationProjectExpenditurePlan);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectExpenditurePlan educationProjectExpenditurePlan, RedirectAttributes redirectAttributes) {
		educationProjectExpenditurePlanService.deleteByIds(educationProjectExpenditurePlan);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectExpenditurePlan educationProjectExpenditurePlan, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User opUser = UserUtils.getUser();
			if("6".equals(opUser.getUserType())){//科室
				educationProjectExpenditurePlan.setUser(opUser);
			}
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectExpenditurePlan.setPage(new Page<EducationProjectExpenditurePlan>(request, response, -1));
			List<ExportExpenditurePlan> planList = educationProjectExpenditurePlanService.findExpenditurePlanList(educationProjectExpenditurePlan);
			if(planList != null && planList.size() != 0){
				for (ExportExpenditurePlan exportExpenditurePlan : planList) {
					if(StringUtils.isBlank(exportExpenditurePlan.getExpenditureYear())){
						if(StringUtils.isBlank(educationProjectExpenditurePlan.getExpenditureYear())){
							exportExpenditurePlan.setExpenditureYear(String.valueOf(Integer.valueOf(DateUtils.formatDate(new Date(), "yyyy")) + 1));
						}else{
							exportExpenditurePlan.setExpenditureYear(exportExpenditurePlan.getExpenditureYear());
						}
					}
				}
			}
			
			new ExportExcel(FUNCTION_NAME_SIMPLE, ExportExpenditurePlan.class).setDataList(planList).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectExpenditurePlan educationProjectExpenditurePlan, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectExpenditurePlan> list = educationProjectExpenditurePlanService.findPage(new Page<EducationProjectExpenditurePlan>(1, 5), new EducationProjectExpenditurePlan()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectExpenditurePlan());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectExpenditurePlan.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditurePlan:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectExpenditurePlan educationProjectExpenditurePlan, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectExpenditurePlan> list = ei.getDataList(EducationProjectExpenditurePlan.class);
			List<EducationProjectExpenditurePlan> insertList=new ArrayList<EducationProjectExpenditurePlan>();
			List<EducationProjectExpenditurePlan> subList=new ArrayList<EducationProjectExpenditurePlan>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
		//	int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectExpenditurePlan>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectExpenditurePlan zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectExpenditurePlanService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditurePlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditurePlan));
		}
	
	
	
	
}