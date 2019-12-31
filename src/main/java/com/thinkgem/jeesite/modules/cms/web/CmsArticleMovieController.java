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
import com.thinkgem.jeesite.modules.cms.entity.CmsArticleMovie;
import com.thinkgem.jeesite.modules.cms.service.CmsArticleMovieService;

/**
 * 文章视频Controller
 * @author gff
 * @version 2018-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsArticleMovie")
public class CmsArticleMovieController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "文章视频";

	@Autowired
	private CmsArticleMovieService cmsArticleMovieService;
	
	@ModelAttribute
	public CmsArticleMovie get(@RequestParam(required=false) String id) {
		CmsArticleMovie entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsArticleMovieService.get(id);
		}
		if (entity == null){
			entity = new CmsArticleMovie();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsArticleMovie:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsArticleMovie cmsArticleMovie, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsArticleMovie> page = cmsArticleMovieService.findPage(new Page<CmsArticleMovie>(request, response), cmsArticleMovie); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "cmsArticleMovie");
		setBase64EncodedQueryStringToEntity(request, cmsArticleMovie);
		return "modules/cms/cmsArticleMovie/cmsArticleMovieList";
	}

	@RequiresPermissions("cms:cmsArticleMovie:view")
	@RequestMapping(value = "form")
	public String form(CmsArticleMovie cmsArticleMovie, Model model) {
		model.addAttribute("cmsArticleMovie", cmsArticleMovie);
		model.addAttribute("ename", "cmsArticleMovie");
		return "modules/cms/cmsArticleMovie/cmsArticleMovieForm";
	}

	@RequiresPermissions("cms:cmsArticleMovie:edit")
	@RequestMapping(value = "save")
	public String save(CmsArticleMovie cmsArticleMovie, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsArticleMovie)){
			return form(cmsArticleMovie, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(cmsArticleMovie.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		cmsArticleMovieService.save(cmsArticleMovie);
		return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
	}
	
	@RequiresPermissions("cms:cmsArticleMovie:edit")
	@RequestMapping(value = "disable")
	public String disable(CmsArticleMovie cmsArticleMovie, RedirectAttributes redirectAttributes) {
		cmsArticleMovieService.disable(cmsArticleMovie);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
	}
	
	@RequiresPermissions("cms:cmsArticleMovie:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsArticleMovie cmsArticleMovie, RedirectAttributes redirectAttributes) {
		cmsArticleMovieService.delete(cmsArticleMovie);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
	}
	
	@RequiresPermissions("cms:cmsArticleMovie:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(CmsArticleMovie cmsArticleMovie, RedirectAttributes redirectAttributes) {
		cmsArticleMovieService.deleteByIds(cmsArticleMovie);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("cms:cmsArticleMovie:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(CmsArticleMovie cmsArticleMovie, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			new ExportExcel(FUNCTION_NAME_SIMPLE, CmsArticleMovie.class).setDataList(cmsArticleMovieService.findList(cmsArticleMovie)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("cms:cmsArticleMovie:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(CmsArticleMovie cmsArticleMovie, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<CmsArticleMovie> list = cmsArticleMovieService.findPage(new Page<CmsArticleMovie>(1, 5), new CmsArticleMovie()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new CmsArticleMovie());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, CmsArticleMovie.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("cms:cmsArticleMovie:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(CmsArticleMovie cmsArticleMovie, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CmsArticleMovie> list = ei.getDataList(CmsArticleMovie.class);
			List<CmsArticleMovie> insertList=new ArrayList<CmsArticleMovie>();
			List<CmsArticleMovie> subList=new ArrayList<CmsArticleMovie>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<CmsArticleMovie>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(CmsArticleMovie zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					//System.out.println("insertList size is :"+insertList.size());
					cmsArticleMovieService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			//System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/cms/cmsArticleMovie/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(cmsArticleMovie));
		}
	
	
	
	
}