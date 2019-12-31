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
import com.cloudinte.modules.xingzhengguanli.entity.WorkArrangementContent;
import com.cloudinte.modules.xingzhengguanli.service.WorkArrangementContentService;

/**
 * 固定值记录Controller
 * @author dcl
 * @version 2019-12-11
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/workArrangementContent")
public class WorkArrangementContentController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "固定值记录";

	@Autowired
	private WorkArrangementContentService workArrangementContentService;
	
	@ModelAttribute
	public WorkArrangementContent get(@RequestParam(required=false) String id) {
		WorkArrangementContent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workArrangementContentService.get(id);
		}
		if (entity == null){
			entity = new WorkArrangementContent();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementContent:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkArrangementContent workArrangementContent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkArrangementContent> page = workArrangementContentService.findPage(new Page<WorkArrangementContent>(request, response), workArrangementContent); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "workArrangementContent");
		setBase64EncodedQueryStringToEntity(request, workArrangementContent);
		return "modules/xingzhengguanli/workArrangementContent/workArrangementContentList";
	}

	@RequiresPermissions("xingzhengguanli:workArrangementContent:view")
	@RequestMapping(value = "form")
	public String form(WorkArrangementContent workArrangementContent, Model model) {
		model.addAttribute("workArrangementContent", workArrangementContent);
		model.addAttribute("ename", "workArrangementContent");
		return "modules/xingzhengguanli/workArrangementContent/workArrangementContentForm";
	}

	@RequiresPermissions("xingzhengguanli:workArrangementContent:edit")
	@RequestMapping(value = "save")
	public String save(WorkArrangementContent workArrangementContent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workArrangementContent)){
			return form(workArrangementContent, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(workArrangementContent.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		workArrangementContentService.save(workArrangementContent);
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementContent:edit")
	@RequestMapping(value = "disable")
	public String disable(WorkArrangementContent workArrangementContent, RedirectAttributes redirectAttributes) {
		workArrangementContentService.disable(workArrangementContent);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementContent:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkArrangementContent workArrangementContent, RedirectAttributes redirectAttributes) {
		workArrangementContentService.delete(workArrangementContent);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
	}
	
	@RequiresPermissions("xingzhengguanli:workArrangementContent:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(WorkArrangementContent workArrangementContent, RedirectAttributes redirectAttributes) {
		workArrangementContentService.deleteByIds(workArrangementContent);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:workArrangementContent:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WorkArrangementContent workArrangementContent, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			workArrangementContent.setPage(new Page<WorkArrangementContent>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkArrangementContent.class).setDataList(workArrangementContentService.findList(workArrangementContent)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:workArrangementContent:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(WorkArrangementContent workArrangementContent, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<WorkArrangementContent> list = workArrangementContentService.findPage(new Page<WorkArrangementContent>(1, 5), new WorkArrangementContent()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new WorkArrangementContent());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkArrangementContent.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:workArrangementContent:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(WorkArrangementContent workArrangementContent, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkArrangementContent> list = ei.getDataList(WorkArrangementContent.class);
			List<WorkArrangementContent> insertList=new ArrayList<WorkArrangementContent>();
			List<WorkArrangementContent> subList=new ArrayList<WorkArrangementContent>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<WorkArrangementContent>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(WorkArrangementContent zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					workArrangementContentService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/workArrangementContent/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workArrangementContent));
		}
	
	
	
	
}