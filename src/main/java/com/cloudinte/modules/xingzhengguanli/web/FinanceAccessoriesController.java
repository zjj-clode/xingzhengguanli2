package com.cloudinte.modules.xingzhengguanli.web;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import com.cloudinte.modules.xingzhengguanli.entity.FinanceAccessories;
import com.cloudinte.modules.xingzhengguanli.service.FinanceAccessoriesService;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 附件管理Controller
 * @author dcl
 * @version 2019-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/financeAccessories")
public class FinanceAccessoriesController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "附件管理";

	@Autowired
	private FinanceAccessoriesService financeAccessoriesService;
	
	@ModelAttribute
	public FinanceAccessories get(@RequestParam(required=false) String id) {
		FinanceAccessories entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = financeAccessoriesService.get(id);
		}
		if (entity == null){
			entity = new FinanceAccessories();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:financeAccessories:view")
	@RequestMapping(value = {"list", ""})
	public String list(FinanceAccessories financeAccessories, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FinanceAccessories> page = financeAccessoriesService.findPage(new Page<FinanceAccessories>(request, response), financeAccessories); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "financeAccessories");
		setBase64EncodedQueryStringToEntity(request, financeAccessories);
		return "modules/xingzhengguanli/financeAccessories/financeAccessoriesList";
	}

	@RequiresPermissions("xingzhengguanli:financeAccessories:view")
	@RequestMapping(value = "form")
	public String form(FinanceAccessories financeAccessories, Model model) {
		if(StringUtils.isBlank(financeAccessories.getId())){
			financeAccessories.setDownloadTimes(0);
		}
		model.addAttribute("financeAccessories", financeAccessories);
		model.addAttribute("ename", "financeAccessories");
		return "modules/xingzhengguanli/financeAccessories/financeAccessoriesForm";
	}

	@RequiresPermissions("xingzhengguanli:financeAccessories:edit")
	@RequestMapping(value = "save")
	public String save(FinanceAccessories financeAccessories, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, financeAccessories)){
			return form(financeAccessories, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(financeAccessories.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		financeAccessoriesService.save(financeAccessories);
		return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
	}
	
	@RequiresPermissions("xingzhengguanli:financeAccessories:edit")
	@RequestMapping(value = "disable")
	public String disable(FinanceAccessories financeAccessories, RedirectAttributes redirectAttributes) {
		financeAccessoriesService.disable(financeAccessories);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
	}
	
	@RequiresPermissions("xingzhengguanli:financeAccessories:edit")
	@RequestMapping(value = "delete")
	public String delete(FinanceAccessories financeAccessories, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		financeAccessoriesService.delete(financeAccessories);
		
		String path = request.getSession().getServletContext().getRealPath("/");
		String contextPathString = request.getContextPath();
		String files = financeAccessories.getFiles();
		
		if(StringUtils.isNotBlank(files)){
			StringBuffer buffer = new StringBuffer();
			buffer.append(path);
			if (files.startsWith(contextPathString)) {
				files = files.substring(contextPathString.length()+1);
			}else {
				files = files.substring(1);
				
			}
			buffer.append(files);
			
			try {
				String filepath =URLDecoder.decode(buffer.toString(),"utf-8");
				FileUtils.deleteFile(filepath);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
	}
	
	@RequiresPermissions("xingzhengguanli:financeAccessories:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(FinanceAccessories financeAccessories,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		financeAccessoriesService.deleteByIds(financeAccessories);
		
		if(financeAccessories.getIds() != null && financeAccessories.getIds().length != 0){
			String[] ids = financeAccessories.getIds();
			String path = request.getSession().getServletContext().getRealPath("/");
			String contextPathString = request.getContextPath();
			for (String string : ids) {
				FinanceAccessories  accessories = financeAccessoriesService.get(string);
				if(accessories != null){
					String files = accessories.getFiles();
					if(StringUtils.isNotBlank(files)){
						StringBuffer buffer = new StringBuffer();
						buffer.append(path);
						if (files.startsWith(contextPathString)) {
							files = files.substring(contextPathString.length()+1);
						}else {
							files = files.substring(1);
							
						}
						buffer.append(files);
						
						try {
							String filepath =URLDecoder.decode(buffer.toString(),"utf-8");
							FileUtils.deleteFile(filepath);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:financeAccessories:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(FinanceAccessories financeAccessories, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			financeAccessories.setPage(new Page<FinanceAccessories>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, FinanceAccessories.class).setDataList(financeAccessoriesService.findList(financeAccessories)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:financeAccessories:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(FinanceAccessories financeAccessories, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<FinanceAccessories> list = financeAccessoriesService.findPage(new Page<FinanceAccessories>(1, 5), new FinanceAccessories()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new FinanceAccessories());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, FinanceAccessories.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:financeAccessories:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(FinanceAccessories financeAccessories, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FinanceAccessories> list = ei.getDataList(FinanceAccessories.class);
			List<FinanceAccessories> insertList=new ArrayList<FinanceAccessories>();
			List<FinanceAccessories> subList=new ArrayList<FinanceAccessories>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<FinanceAccessories>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(FinanceAccessories zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					financeAccessoriesService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(financeAccessories));
		}
	
	
	/**
	 * 文件下载
	 * @param response
	 * @param str
	 * @throws UnsupportedEncodingException 
	 */
	private void downFile(HttpServletResponse response, String pathZip,String filename) throws UnsupportedEncodingException {
		File file = new File(pathZip);
		if (file.exists()) {
			 
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
				//response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
			response.setHeader("Content-Disposition", "attachment; filename="
						+ new String(filename.getBytes("gbk"), "ISO-8859-1"));
			
			 
			 byte[] b = new byte[2014];
			 FileInputStream fis = null;
			 BufferedInputStream bis = null;
			 try {
				fis = new  FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(b);
				while (i != -1) {
					os.write(b,0,i);
					i = bis.read(b);
				}
				os.flush();
				os.close();  
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (bis != null) {
					try {
						bis.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}
	
	
	@RequestMapping(value = "downFiles")
	public String downFiles(FinanceAccessories financeAccessories, Model model,
			 HttpServletRequest request,HttpServletResponse response) {
		
		Integer downloadTimes = financeAccessories.getDownloadTimes();
		if(downloadTimes != null){
			financeAccessories.setDownloadTimes(downloadTimes + 1);
		}else{
			financeAccessories.setDownloadTimes(1);
		}
		financeAccessoriesService.updateDownloadTimes(financeAccessories);
		
		String path = request.getSession().getServletContext().getRealPath("/");
		String contextPathString = request.getContextPath();
		
		String filePath = financeAccessories.getFiles();
		String[] names = filePath.split("\\.");
		String filename = financeAccessories.getTitle()+"."+names[names.length -1];
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(path);
		if (filePath.startsWith(contextPathString)) {
			filePath = filePath.substring(contextPathString.length()+1);
		}else {
			filePath = filePath.substring(1);
			
		}
		buffer.append(filePath);
		
		try {
			String filepath =URLDecoder.decode(buffer.toString(),"utf-8");// buffer.toString(); //URLDecoder.decode(buffer.toString(),"utf-8") ;
			
			this.downFile(response, filepath,filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:"+adminPath+"/xingzhengguanli/financeAccessories/?repage&" ;
	}
	
}