package com.cloudinte.modules.xingzhengguanli.web;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.modules.xingzhengguanli.entity.BjProject;
import com.cloudinte.modules.xingzhengguanli.service.BjProjectService;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 北京市共建项目Controller
 * @author dcl
 * @version 2019-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/bjProject")
public class BjProjectController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "北京市共建项目";

	@Autowired
	private BjProjectService bjProjectService;
	
	@ModelAttribute
	public BjProject get(@RequestParam(required=false) String id) {
		BjProject entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bjProjectService.get(id);
		}
		if (entity == null){
			entity = new BjProject();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:bjProject:view")
	@RequestMapping(value = {"list", ""})
	public String list(BjProject bjProject, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			bjProject.setUser(opUser);
		}
		
		Page<BjProject> page = bjProjectService.findPage(new Page<BjProject>(request, response), bjProject); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "bjProject");
		setBase64EncodedQueryStringToEntity(request, bjProject);
		return "modules/xingzhengguanli/bjProject/bjProjectList";
	}

	@RequiresPermissions("xingzhengguanli:bjProject:view")
	@RequestMapping(value = "form")
	public String form(BjProject bjProject, Model model) {
		
		if(StringUtils.isBlank(bjProject.getId())){
			User opUser = UserUtils.getUser();
			bjProject.setUser(opUser);
		}
		
		model.addAttribute("bjProject", bjProject);
		model.addAttribute("ename", "bjProject");
		return "modules/xingzhengguanli/bjProject/bjProjectForm";
	}

	@RequiresPermissions("xingzhengguanli:bjProject:edit")
	@RequestMapping(value = "save")
	public String save(BjProject bjProject, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, bjProject)){
			return form(bjProject, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(bjProject.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		if(StringUtils.isBlank(bjProject.getId())){
			bjProject.setUploadDate(new Date());
		}
		bjProjectService.save(bjProject);
		return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
	}
	
	@RequiresPermissions("xingzhengguanli:bjProject:edit")
	@RequestMapping(value = "disable")
	public String disable(BjProject bjProject, RedirectAttributes redirectAttributes) {
		bjProjectService.disable(bjProject);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
	}
	
	@RequiresPermissions("xingzhengguanli:bjProject:edit")
	@RequestMapping(value = "delete")
	public String delete(BjProject bjProject,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		bjProjectService.delete(bjProject);
		String path = request.getSession().getServletContext().getRealPath("/");
		String contextPathString = request.getContextPath();
		String firstFilesPath = bjProject.getFirstFiles();
		String lastFilesPath = bjProject.getLastFiles();
		
		if(StringUtils.isNotBlank(firstFilesPath)){
			StringBuffer buffer = new StringBuffer();
			buffer.append(path);
			if (firstFilesPath.startsWith(contextPathString)) {
				firstFilesPath = firstFilesPath.substring(contextPathString.length()+1);
			}else {
				firstFilesPath = firstFilesPath.substring(1);
				
			}
			buffer.append(firstFilesPath);
			
			try {
				String filepath =URLDecoder.decode(buffer.toString(),"utf-8");
				FileUtils.deleteFile(filepath);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		if(StringUtils.isNotBlank(lastFilesPath)){
			StringBuffer buffer2 = new StringBuffer();
			buffer2.append(path);
			if (lastFilesPath.startsWith(contextPathString)) {
				lastFilesPath = lastFilesPath.substring(contextPathString.length()+1);
			}else {
				lastFilesPath = lastFilesPath.substring(1);
			}
			buffer2.append(lastFilesPath);
			try {
				
				String filepath2 =URLDecoder.decode(buffer2.toString(),"utf-8");
				FileUtils.deleteFile(filepath2);
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
		}
		
		
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
	}
	
	@RequiresPermissions("xingzhengguanli:bjProject:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(BjProject bjProject,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		bjProjectService.deleteByIds(bjProject);
		if(bjProject.getIds() != null && bjProject.getIds().length != 0){
			String[] ids = bjProject.getIds();
			String path = request.getSession().getServletContext().getRealPath("/");
			String contextPathString = request.getContextPath();
			for (String string : ids) {
				BjProject project = bjProjectService.get(string);
				if(project != null){
					
					String firstFilesPath = project.getFirstFiles();
					String lastFilesPath = project.getLastFiles();
					
					if(StringUtils.isNotBlank(firstFilesPath)){
						StringBuffer buffer = new StringBuffer();
						buffer.append(path);
						if (firstFilesPath.startsWith(contextPathString)) {
							firstFilesPath = firstFilesPath.substring(contextPathString.length()+1);
						}else {
							firstFilesPath = firstFilesPath.substring(1);
							
						}
						buffer.append(firstFilesPath);
						
						try {
							String filepath =URLDecoder.decode(buffer.toString(),"utf-8");
							FileUtils.deleteFile(filepath);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					
					if(StringUtils.isNotBlank(lastFilesPath)){
						StringBuffer buffer2 = new StringBuffer();
						buffer2.append(path);
						if (lastFilesPath.startsWith(contextPathString)) {
							lastFilesPath = lastFilesPath.substring(contextPathString.length()+1);
						}else {
							lastFilesPath = lastFilesPath.substring(1);
						}
						buffer2.append(lastFilesPath);
						try {
							
							String filepath2 =URLDecoder.decode(buffer2.toString(),"utf-8");
							FileUtils.deleteFile(filepath2);
						} catch (UnsupportedEncodingException e) {
							
							e.printStackTrace();
						}
					}
				}
			}
		}
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:bjProject:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(BjProject bjProject, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			bjProject.setPage(new Page<BjProject>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, BjProject.class).setDataList(bjProjectService.findList(bjProject)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:bjProject:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(BjProject bjProject, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<BjProject> list = bjProjectService.findPage(new Page<BjProject>(1, 5), new BjProject()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new BjProject());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, BjProject.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:bjProject:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(BjProject bjProject, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BjProject> list = ei.getDataList(BjProject.class);
			List<BjProject> insertList=new ArrayList<BjProject>();
			List<BjProject> subList=new ArrayList<BjProject>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<BjProject>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(BjProject zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					bjProjectService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(bjProject));
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
	
	
	@RequestMapping(value = "downloadProjectFile")
	public String downloadProjectFile(BjProject bjProject, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes){
		
		String path = request.getSession().getServletContext().getRealPath("/");
		String filename = "申报材料.zip";
		List<File> pathList = new ArrayList<File>();
		byte[] b = new byte[1024];
		
		try {
			if(StringUtils.isNotBlank(bjProject.getFirstFiles())){
				StringBuffer buffer = new StringBuffer();
				buffer.append(path);
				String filePath = bjProject.getFirstFiles();
				buffer.append(filePath);
				String filepathOut = URLDecoder.decode(buffer.toString(),"utf-8");
				
				File file = new File(filepathOut);
				pathList.add(file);
			}
			if(StringUtils.isNotBlank(bjProject.getLastFiles())){
				StringBuffer buffer = new StringBuffer();
				buffer.append(path);
				String filePath = bjProject.getLastFiles();
				buffer.append(filePath);
				String filepathOut = URLDecoder.decode(buffer.toString(),"utf-8");
				
				File file = new File(filepathOut);
				pathList.add(file);
			}
			
			if(pathList.size() != 0){
				
				String pathZip = path + "/"+"tmpzip"+"/" + filename;
				
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(pathZip));
				out.setEncoding("GBK");
				for (File file : pathList) {
					FileInputStream fis = new FileInputStream(file);
					out.putNextEntry(new ZipEntry(file.getName()));
					int len;
					while ((len = fis.read(b)) > 0) {
						out.write(b,0,len);
					}
					out.closeEntry();
					fis.close();
				}
				out.close();
				this.downFile(response, pathZip,filename);
				
				FileUtils.deleteFile(pathZip);
				
			}else{
				addMessage(redirectAttributes, "没有上传申报表！");
				return "redirect:"+adminPath+"/xingzhengguanli/bjProject/?repage&";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}