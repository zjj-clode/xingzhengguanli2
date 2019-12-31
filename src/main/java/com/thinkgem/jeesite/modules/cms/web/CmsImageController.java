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
import com.thinkgem.jeesite.modules.cms.entity.CmsImage;
import com.thinkgem.jeesite.modules.cms.service.CmsImageService;

/**
 * 图片管理Controller
 * @author G6
 * @version 2019-01-11
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsImage")
public class CmsImageController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "图片";

	@Autowired
	private CmsImageService cmsImageService;
	
	@ModelAttribute
	public CmsImage get(@RequestParam(required=false) String id) {
		CmsImage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsImageService.get(id);
		}
		if (entity == null){
			entity = new CmsImage();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsImage:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsImage cmsImage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsImage> page = cmsImageService.findPage(new Page<CmsImage>(request, response), cmsImage); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "cmsImage");
		setBase64EncodedQueryStringToEntity(request, cmsImage);
		return "modules/cms/cmsImage/cmsImageList";
	}

	@RequiresPermissions("cms:cmsImage:view")
	@RequestMapping(value = "form")
	public String form(CmsImage cmsImage, Model model) {
		model.addAttribute("cmsImage", cmsImage);
		model.addAttribute("ename", "cmsImage");
		return "modules/cms/cmsImage/cmsImageForm";
	}

	@RequiresPermissions("cms:cmsImage:edit")
	@RequestMapping(value = "save")
	public String save(CmsImage cmsImage, Model model, RedirectAttributes redirectAttributes) {
	    if (!beanValidator(model, cmsImage)){
			return form(cmsImage, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(cmsImage.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		cmsImageService.save(cmsImage);
		return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
	}
	
	@RequiresPermissions("cms:cmsImage:edit")
	@RequestMapping(value = "disable")
	public String disable(CmsImage cmsImage, RedirectAttributes redirectAttributes) {
		cmsImageService.disable(cmsImage);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
	}
	
	@RequiresPermissions("cms:cmsImage:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsImage cmsImage, RedirectAttributes redirectAttributes) {
		cmsImageService.delete(cmsImage);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
	}
	
	@RequiresPermissions("cms:cmsImage:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(CmsImage cmsImage, RedirectAttributes redirectAttributes) {
		cmsImageService.deleteByIds(cmsImage);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("cms:cmsImage:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(CmsImage cmsImage, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			cmsImage.setPage(new Page<CmsImage>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, CmsImage.class).setDataList(cmsImageService.findList(cmsImage)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("cms:cmsImage:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(CmsImage cmsImage, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<CmsImage> list = cmsImageService.findPage(new Page<CmsImage>(1, 5), new CmsImage()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new CmsImage());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, CmsImage.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("cms:cmsImage:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(CmsImage cmsImage, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CmsImage> list = ei.getDataList(CmsImage.class);
			List<CmsImage> insertList=new ArrayList<CmsImage>();
			List<CmsImage> subList=new ArrayList<CmsImage>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<CmsImage>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(CmsImage zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					cmsImageService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/cms/cmsImage/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsImage));
		}
	
	
	
	
}