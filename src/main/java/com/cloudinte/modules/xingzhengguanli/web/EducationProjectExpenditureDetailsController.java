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
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpenditureDetails;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectExpenditureDetailsService;

/**
 * 项目支出明细Controller
 * @author dcl
 * @version 2019-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectExpenditureDetails")
public class EducationProjectExpenditureDetailsController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "细化经济分类";

	@Autowired
	private EducationProjectExpenditureDetailsService educationProjectExpenditureDetailsService;
	
	@ModelAttribute
	public EducationProjectExpenditureDetails get(@RequestParam(required=false) String id) {
		EducationProjectExpenditureDetails entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectExpenditureDetailsService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectExpenditureDetails();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectExpenditureDetails educationProjectExpenditureDetails, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EducationProjectExpenditureDetails> page = educationProjectExpenditureDetailsService.findPage(new Page<EducationProjectExpenditureDetails>(request, response), educationProjectExpenditureDetails); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectExpenditureDetails");
		setBase64EncodedQueryStringToEntity(request, educationProjectExpenditureDetails);
		return "modules/xingzhengguanli/educationProjectExpenditureDetails/educationProjectExpenditureDetailsList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectExpenditureDetails educationProjectExpenditureDetails, Model model) {
		model.addAttribute("educationProjectExpenditureDetails", educationProjectExpenditureDetails);
		model.addAttribute("ename", "educationProjectExpenditureDetails");
		return "modules/xingzhengguanli/educationProjectExpenditureDetails/educationProjectExpenditureDetailsForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectExpenditureDetails educationProjectExpenditureDetails, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectExpenditureDetails)){
			return form(educationProjectExpenditureDetails, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectExpenditureDetails.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectExpenditureDetailsService.save(educationProjectExpenditureDetails);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectExpenditureDetails educationProjectExpenditureDetails, RedirectAttributes redirectAttributes) {
		educationProjectExpenditureDetailsService.disable(educationProjectExpenditureDetails);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectExpenditureDetails educationProjectExpenditureDetails, RedirectAttributes redirectAttributes) {
		educationProjectExpenditureDetailsService.delete(educationProjectExpenditureDetails);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectExpenditureDetails educationProjectExpenditureDetails, RedirectAttributes redirectAttributes) {
		educationProjectExpenditureDetailsService.deleteByIds(educationProjectExpenditureDetails);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectExpenditureDetails educationProjectExpenditureDetails, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectExpenditureDetails.setPage(new Page<EducationProjectExpenditureDetails>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectExpenditureDetails.class).setDataList(educationProjectExpenditureDetailsService.findList(educationProjectExpenditureDetails)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectExpenditureDetails educationProjectExpenditureDetails, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectExpenditureDetails> list = educationProjectExpenditureDetailsService.findPage(new Page<EducationProjectExpenditureDetails>(1, 5), new EducationProjectExpenditureDetails()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectExpenditureDetails());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectExpenditureDetails.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpenditureDetails:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectExpenditureDetails educationProjectExpenditureDetails, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectExpenditureDetails> list = ei.getDataList(EducationProjectExpenditureDetails.class);
			List<EducationProjectExpenditureDetails> insertList=new ArrayList<EducationProjectExpenditureDetails>();
			List<EducationProjectExpenditureDetails> subList=new ArrayList<EducationProjectExpenditureDetails>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectExpenditureDetails>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectExpenditureDetails zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectExpenditureDetailsService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpenditureDetails/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpenditureDetails));
		}
	
	
	
	
}