package com.thinkgem.jeesite.modules.sys.web;
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
import com.thinkgem.jeesite.modules.sys.entity.GuestAccess;
import com.thinkgem.jeesite.modules.sys.service.GuestAccessService;

/**
 * 访问信息记录模块Controller
 * @author gyl
 * @version 2019-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/guestAccess")
public class GuestAccessController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "访问日志功能";

	@Autowired
	private GuestAccessService guestAccessService;
	
	@ModelAttribute
	public GuestAccess get(@RequestParam(required=false) String id) {
		GuestAccess entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = guestAccessService.get(id);
		}
		if (entity == null){
			entity = new GuestAccess();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:guestAccess:view")
	@RequestMapping(value = {"list", ""})
	public String list(GuestAccess guestAccess, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GuestAccess> page = guestAccessService.findPage(new Page<GuestAccess>(request, response), guestAccess); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "guestAccess");
		setBase64EncodedQueryStringToEntity(request, guestAccess);
		return "modules/sys/guestAccess/guestAccessList";
	}

	@RequiresPermissions("sys:guestAccess:edit")
	@RequestMapping(value = "form")
	public String form(GuestAccess guestAccess, Model model) {
		model.addAttribute("guestAccess", guestAccess);
		model.addAttribute("ename", "guestAccess");
		return "modules/sys/guestAccess/guestAccessForm";
	}

	@RequiresPermissions("sys:guestAccess:edit")
	@RequestMapping(value = "save")
	public String save(GuestAccess guestAccess, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, guestAccess)){
			return form(guestAccess, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(guestAccess.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		guestAccessService.save(guestAccess);
		return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
	}
	
	@RequiresPermissions("sys:guestAccess:edit")
	@RequestMapping(value = "disable")
	public String disable(GuestAccess guestAccess, RedirectAttributes redirectAttributes) {
		guestAccessService.disable(guestAccess);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
	}
	
	@RequiresPermissions("sys:guestAccess:edit")
	@RequestMapping(value = "delete")
	public String delete(GuestAccess guestAccess, RedirectAttributes redirectAttributes) {
		guestAccessService.delete(guestAccess);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
	}
	
	@RequiresPermissions("sys:guestAccess:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(GuestAccess guestAccess, RedirectAttributes redirectAttributes) {
		guestAccessService.deleteByIds(guestAccess);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("sys:guestAccess:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(GuestAccess guestAccess, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			guestAccess.setPage(new Page<GuestAccess>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, GuestAccess.class).setDataList(guestAccessService.findList(guestAccess)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("sys:guestAccess:edit")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(GuestAccess guestAccess, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<GuestAccess> list = guestAccessService.findPage(new Page<GuestAccess>(1, 5), new GuestAccess()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new GuestAccess());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, GuestAccess.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("sys:guestAccess:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(GuestAccess guestAccess, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GuestAccess> list = ei.getDataList(GuestAccess.class);
			List<GuestAccess> insertList=new ArrayList<GuestAccess>();
			List<GuestAccess> subList=new ArrayList<GuestAccess>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<GuestAccess>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(GuestAccess zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
//					System.out.println("insertList size is :"+insertList.size());
					guestAccessService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
//			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/sys/guestAccess/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(guestAccess));
		}
	
	
	
	
}