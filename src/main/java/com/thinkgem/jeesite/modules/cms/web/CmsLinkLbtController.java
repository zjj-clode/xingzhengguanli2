package com.thinkgem.jeesite.modules.cms.web;
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
import com.thinkgem.jeesite.modules.cms.entity.CmsLinkLbt;
import com.thinkgem.jeesite.modules.cms.service.CmsLinkLbtService;

/**
 * 轮播图Controller
 * @author gf
 * @version 2018-01-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsLinkLbt")
public class CmsLinkLbtController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "图片";

	@Autowired
	private CmsLinkLbtService cmsLinkLbtService;
	
	@ModelAttribute
	public CmsLinkLbt get(@RequestParam(required=false) String id) {
		CmsLinkLbt entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsLinkLbtService.get(id);
		}
		if (entity == null){
			entity = new CmsLinkLbt();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsLinkLbt:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsLinkLbt cmsLinkLbt, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsLinkLbt> page = cmsLinkLbtService.findPage(new Page<CmsLinkLbt>(request, response), cmsLinkLbt); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "cmsLinkLbt");
		setBase64EncodedQueryStringToEntity(request, cmsLinkLbt);
		return "modules/cms/cmsLinkLbt/cmsLinkLbtList";
	}

	@RequiresPermissions("cms:cmsLinkLbt:view")
	@RequestMapping(value = "form")
	public String form(CmsLinkLbt cmsLinkLbt, Model model) {
		model.addAttribute("cmsLinkLbt", cmsLinkLbt);
		model.addAttribute("ename", "cmsLinkLbt");
		return "modules/cms/cmsLinkLbt/cmsLinkLbtForm";
	}

	@RequiresPermissions("cms:cmsLinkLbt:edit")
	@RequestMapping(value = "save")
	public String save(CmsLinkLbt cmsLinkLbt, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsLinkLbt)){
			return form(cmsLinkLbt, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(cmsLinkLbt.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		cmsLinkLbtService.save(cmsLinkLbt);
		return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
	}
	
	@RequiresPermissions("cms:cmsLinkLbt:edit")
	@RequestMapping(value = "disable")
	public String disable(CmsLinkLbt cmsLinkLbt, RedirectAttributes redirectAttributes) {
		cmsLinkLbtService.disable(cmsLinkLbt);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
	}
	
	@RequiresPermissions("cms:cmsLinkLbt:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsLinkLbt cmsLinkLbt, RedirectAttributes redirectAttributes) {
		cmsLinkLbtService.delete(cmsLinkLbt);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
	}
	
	@RequiresPermissions("cms:cmsLinkLbt:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(CmsLinkLbt cmsLinkLbt, RedirectAttributes redirectAttributes) {
		cmsLinkLbtService.deleteByIds(cmsLinkLbt);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("cms:cmsLinkLbt:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(CmsLinkLbt cmsLinkLbt, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			new ExportExcel(FUNCTION_NAME_SIMPLE, CmsLinkLbt.class).setDataList(cmsLinkLbtService.findList(cmsLinkLbt)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("cms:cmsLinkLbt:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(CmsLinkLbt cmsLinkLbt, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<CmsLinkLbt> list = cmsLinkLbtService.findPage(new Page<CmsLinkLbt>(1, 5), new CmsLinkLbt()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new CmsLinkLbt());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, CmsLinkLbt.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("cms:cmsLinkLbt:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(CmsLinkLbt cmsLinkLbt, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CmsLinkLbt> list = ei.getDataList(CmsLinkLbt.class);
			List<CmsLinkLbt> insertList=new ArrayList<CmsLinkLbt>();
			List<CmsLinkLbt> subList=new ArrayList<CmsLinkLbt>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<CmsLinkLbt>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(CmsLinkLbt zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					//System.out.println("insertList size is :"+insertList.size());
					cmsLinkLbtService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			//System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/cms/cmsLinkLbt/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsLinkLbt));
		}
	
	
	
	
}