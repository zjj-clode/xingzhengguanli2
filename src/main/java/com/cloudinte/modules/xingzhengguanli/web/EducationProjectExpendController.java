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
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpend;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectExpendService;

/**
 * 项目支出立项Controller
 * @author dcl
 * @version 2019-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectExpend")
public class EducationProjectExpendController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "项目支出立项";

	@Autowired
	private EducationProjectExpendService educationProjectExpendService;
	
	@ModelAttribute
	public EducationProjectExpend get(@RequestParam(required=false) String id) {
		EducationProjectExpend entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectExpendService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectExpend();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpend:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectExpend educationProjectExpend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EducationProjectExpend> page = educationProjectExpendService.findPage(new Page<EducationProjectExpend>(request, response), educationProjectExpend); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectExpend");
		setBase64EncodedQueryStringToEntity(request, educationProjectExpend);
		return "modules/xingzhengguanli/educationProjectExpend/educationProjectExpendList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpend:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectExpend educationProjectExpend, Model model) {
		model.addAttribute("educationProjectExpend", educationProjectExpend);
		model.addAttribute("ename", "educationProjectExpend");
		return "modules/xingzhengguanli/educationProjectExpend/educationProjectExpendForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpend:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectExpend educationProjectExpend, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectExpend)){
			return form(educationProjectExpend, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectExpend.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectExpendService.save(educationProjectExpend);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpend:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectExpend educationProjectExpend, RedirectAttributes redirectAttributes) {
		educationProjectExpendService.disable(educationProjectExpend);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpend:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectExpend educationProjectExpend, RedirectAttributes redirectAttributes) {
		educationProjectExpendService.delete(educationProjectExpend);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpend:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectExpend educationProjectExpend, RedirectAttributes redirectAttributes) {
		educationProjectExpendService.deleteByIds(educationProjectExpend);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpend:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectExpend educationProjectExpend, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectExpend.setPage(new Page<EducationProjectExpend>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectExpend.class).setDataList(educationProjectExpendService.findList(educationProjectExpend)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpend:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectExpend educationProjectExpend, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectExpend> list = educationProjectExpendService.findPage(new Page<EducationProjectExpend>(1, 5), new EducationProjectExpend()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectExpend());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectExpend.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpend:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectExpend educationProjectExpend, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectExpend> list = ei.getDataList(EducationProjectExpend.class);
			List<EducationProjectExpend> insertList=new ArrayList<EducationProjectExpend>();
			List<EducationProjectExpend> subList=new ArrayList<EducationProjectExpend>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectExpend>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectExpend zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectExpendService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpend/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpend));
		}
	
	
	
	
}