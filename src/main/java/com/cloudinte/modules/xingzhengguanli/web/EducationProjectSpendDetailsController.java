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
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectDescription;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectSpendDetails;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectDescriptionService;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectSpendDetailsService;

/**
 * 项目支出明细Controller
 * @author dcl
 * @version 2019-12-14
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectSpendDetails")
public class EducationProjectSpendDetailsController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "项目支出明细";

	@Autowired
	private EducationProjectSpendDetailsService educationProjectSpendDetailsService;
	
	@Autowired
	private EducationProjectDescriptionService educationProjectDescriptionService;
	
	@ModelAttribute
	public EducationProjectSpendDetails get(@RequestParam(required=false) String id) {
		EducationProjectSpendDetails entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectSpendDetailsService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectSpendDetails();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectSpendDetails educationProjectSpendDetails, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			educationProjectSpendDetails.setUser(opUser);
		}
		
		Page<EducationProjectSpendDetails> page = educationProjectSpendDetailsService.findPage(new Page<EducationProjectSpendDetails>(request, response), educationProjectSpendDetails); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectSpendDetails");
		setBase64EncodedQueryStringToEntity(request, educationProjectSpendDetails);
		return "modules/xingzhengguanli/educationProjectSpendDetails/educationProjectSpendDetailsList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectSpendDetails educationProjectSpendDetails, Model model) {
		
		if(StringUtils.isBlank(educationProjectSpendDetails.getId())){
			User opUser = UserUtils.getUser();
			educationProjectSpendDetails.setUser(opUser);
		}
		
		EducationProjectDescription educationProjectDescription = new EducationProjectDescription();
		List<EducationProjectDescription> descriptionList = educationProjectDescriptionService.findList(educationProjectDescription);
		model.addAttribute("descriptionList", descriptionList);
		
		model.addAttribute("educationProjectSpendDetails", educationProjectSpendDetails);
		model.addAttribute("ename", "educationProjectSpendDetails");
		return "modules/xingzhengguanli/educationProjectSpendDetails/educationProjectSpendDetailsForm";
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:view")
	@RequestMapping(value = "batchform")
	public String batchform(EducationProjectSpendDetails educationProjectSpendDetails, Model model) {
		
		if(StringUtils.isBlank(educationProjectSpendDetails.getId())){
			User opUser = UserUtils.getUser();
			educationProjectSpendDetails.setUser(opUser);
			
			String currentYear = DateUtils.formatDate(new Date(), "yyyy");
			String projectStartYear = String.valueOf(Integer.valueOf(currentYear) + 1) ;
			educationProjectSpendDetails.setYear(projectStartYear);
		}
		
		EducationProjectDescription educationProjectDescription = new EducationProjectDescription();
		List<EducationProjectDescription> descriptionList = educationProjectDescriptionService.findList(educationProjectDescription);
		model.addAttribute("descriptionList", descriptionList);
		
		model.addAttribute("educationProjectSpendDetails", educationProjectSpendDetails);
		model.addAttribute("ename", "educationProjectSpendDetails");
		return "modules/xingzhengguanli/educationProjectSpendDetails/batchAddSpendDetailsForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectSpendDetails educationProjectSpendDetails, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectSpendDetails)){
			return form(educationProjectSpendDetails, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectSpendDetails.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectSpendDetailsService.save(educationProjectSpendDetails);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
	}
	
	@RequestMapping(value = "batchsave")
	public String batchsave(EducationProjectSpendDetails educationProjectSpendDetails,String addDetails,
			Model model, RedirectAttributes redirectAttributes) {
		
		EducationProjectSpendDetails spendDetails= new EducationProjectSpendDetails();
		spendDetails.setUser(educationProjectSpendDetails.getUser());
		spendDetails.setYear(educationProjectSpendDetails.getYear());
		
		educationProjectSpendDetailsService.batchSave(spendDetails, addDetails);
		
		addMessage(redirectAttributes,"保存"+FUNCTION_NAME_SIMPLE+"成功");
		
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&";
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectSpendDetails educationProjectSpendDetails, RedirectAttributes redirectAttributes) {
		educationProjectSpendDetailsService.disable(educationProjectSpendDetails);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectSpendDetails educationProjectSpendDetails, RedirectAttributes redirectAttributes) {
		educationProjectSpendDetailsService.delete(educationProjectSpendDetails);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectSpendDetails educationProjectSpendDetails, RedirectAttributes redirectAttributes) {
		educationProjectSpendDetailsService.deleteByIds(educationProjectSpendDetails);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectSpendDetails educationProjectSpendDetails, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			
			User opUser = UserUtils.getUser();
			if("6".equals(opUser.getUserType())){//科室
				educationProjectSpendDetails.setUser(opUser);
			}
			
			List<EducationProjectSpendDetails> detailsList = educationProjectSpendDetailsService.findExportSpendDetails(educationProjectSpendDetails);
			
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectSpendDetails.setPage(new Page<EducationProjectSpendDetails>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectSpendDetails.class).setDataList(detailsList).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectSpendDetails educationProjectSpendDetails, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectSpendDetails> list = educationProjectSpendDetailsService.findPage(new Page<EducationProjectSpendDetails>(1, 5), new EducationProjectSpendDetails()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectSpendDetails());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectSpendDetails.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectSpendDetails:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectSpendDetails educationProjectSpendDetails, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectSpendDetails> list = ei.getDataList(EducationProjectSpendDetails.class);
			List<EducationProjectSpendDetails> insertList=new ArrayList<EducationProjectSpendDetails>();
			List<EducationProjectSpendDetails> subList=new ArrayList<EducationProjectSpendDetails>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectSpendDetails>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectSpendDetails zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectSpendDetailsService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectSpendDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectSpendDetails));
		}
	
	
	
	
}