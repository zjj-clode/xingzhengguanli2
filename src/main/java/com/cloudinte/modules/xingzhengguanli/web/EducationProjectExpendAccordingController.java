package com.cloudinte.modules.xingzhengguanli.web;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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

import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectExpendAccording;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectExpendAccordingService;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 项目支出立项依据Controller
 * @author dcl
 * @version 2019-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectExpendAccording")
public class EducationProjectExpendAccordingController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "项目支出立项依据";

	@Autowired
	private EducationProjectExpendAccordingService educationProjectExpendAccordingService;

	@ModelAttribute
	public EducationProjectExpendAccording get(@RequestParam(required=false) String id) {
		EducationProjectExpendAccording entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectExpendAccordingService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectExpendAccording();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectExpendAccording educationProjectExpendAccording, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			educationProjectExpendAccording.setUser(opUser);
		}
		
		Page<EducationProjectExpendAccording> page = educationProjectExpendAccordingService.findPage(new Page<EducationProjectExpendAccording>(request, response), educationProjectExpendAccording); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectExpendAccording");
		setBase64EncodedQueryStringToEntity(request, educationProjectExpendAccording);
		return "modules/xingzhengguanli/educationProjectExpendAccording/educationProjectExpendAccordingList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectExpendAccording educationProjectExpendAccording, Model model) {
		
		if(StringUtils.isBlank(educationProjectExpendAccording.getId())){
			User opUser = UserUtils.getUser();
			educationProjectExpendAccording.setUser(opUser);
			
			String currentYear = DateUtils.formatDate(new Date(), "yyyy");
			
			String projectStartYear = String.valueOf(Integer.valueOf(currentYear) + 1) ;
			educationProjectExpendAccording.setProjectName(projectStartYear + SettingsUtils.getSysConfig("projectName", "年教育教学改革专项"));
			educationProjectExpendAccording.setProjectCategory(SettingsUtils.getSysConfig("projectCategory", "其他项目"));
			educationProjectExpendAccording.setProjectCode(SettingsUtils.getSysConfig("projectCode", "教育部"));
			educationProjectExpendAccording.setProjectCycle(SettingsUtils.getSysConfig("projectCycle", "1年"));
			educationProjectExpendAccording.setProjectStartYear(projectStartYear);
			educationProjectExpendAccording.setProjectUnit(SettingsUtils.getSysConfig("projectUnit", "中国政法大学"));
			
		}
		
		model.addAttribute("educationProjectExpendAccording", educationProjectExpendAccording);
		model.addAttribute("ename", "educationProjectExpendAccording");
		return "modules/xingzhengguanli/educationProjectExpendAccording/educationProjectExpendAccordingForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectExpendAccording educationProjectExpendAccording, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectExpendAccording)){
			return form(educationProjectExpendAccording, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectExpendAccording.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectExpendAccordingService.save(educationProjectExpendAccording);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectExpendAccording educationProjectExpendAccording, RedirectAttributes redirectAttributes) {
		educationProjectExpendAccordingService.disable(educationProjectExpendAccording);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectExpendAccording educationProjectExpendAccording, RedirectAttributes redirectAttributes) {
		educationProjectExpendAccordingService.delete(educationProjectExpendAccording);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectExpendAccording educationProjectExpendAccording, RedirectAttributes redirectAttributes) {
		educationProjectExpendAccordingService.deleteByIds(educationProjectExpendAccording);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectExpendAccording educationProjectExpendAccording, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			
			User opUser = UserUtils.getUser();
			if("6".equals(opUser.getUserType())){//科室
				educationProjectExpendAccording.setUser(opUser);
			}
			List<EducationProjectExpendAccording> educationProjectExpendAccordingList = educationProjectExpendAccordingService.findList(educationProjectExpendAccording);
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			
			//根据年份分类，2020  list
			Map<String, List<EducationProjectExpendAccording>> yearEductionProjectMap = new HashMap<String, List<EducationProjectExpendAccording>>();
			if(educationProjectExpendAccordingList != null && educationProjectExpendAccordingList.size() != 0){
				for (EducationProjectExpendAccording educationProjectExpendAccording2 : educationProjectExpendAccordingList) {
					
					List<EducationProjectExpendAccording> list = yearEductionProjectMap.get(educationProjectExpendAccording2.getProjectStartYear());
					if(list == null){
						list = new ArrayList<EducationProjectExpendAccording>();
					}
					list.add(educationProjectExpendAccording2);
					yearEductionProjectMap.put(educationProjectExpendAccording2.getProjectStartYear(), list);
				}
				
				SXSSFWorkbook wb = new SXSSFWorkbook(500); 
				Map<String, CellStyle> styles  = createStyles(wb);
				
				for (String year : yearEductionProjectMap.keySet()) {
					
					
					List<EducationProjectExpendAccording> list = yearEductionProjectMap.get(year);
					
					Sheet sheet = wb.createSheet(year+"项目支出立项依据");
					int  rownum = 0;
					
					Row rownull = sheet.createRow(rownum++);
					Cell cellNull = rownull.createCell(0);
					cellNull.setCellValue("");
					sheet.addMergedRegion(new CellRangeAddress(0, 0,0, 4));
					
					Row titleRow = sheet.createRow(rownum++);
					titleRow.setHeightInPoints(50);
					Cell titleCell = titleRow.createCell(0);
					titleCell.setCellStyle(styles.get("title"));
					titleCell.setCellValue("项目支出立项依据");
					sheet.addMergedRegion(new CellRangeAddress(1, 1,0, 4));
					
					Row titleRow2 = sheet.createRow(rownum++);
					titleRow2.setHeightInPoints(30);
					Cell titleCell2 = titleRow2.createCell(0);
					titleCell2.setCellStyle(styles.get("title"));
					titleCell2.setCellValue("（"+year+"年度）");
					sheet.addMergedRegion(new CellRangeAddress(2, 2,0, 4));
					
					Row rownull1 = sheet.createRow(rownum++);
					Cell cellNull1 = rownull1.createCell(0);
					cellNull1.setCellValue("");
					sheet.addMergedRegion(new CellRangeAddress(3, 3,0, 4));
					
					
					Row dataRow1 = sheet.createRow(rownum++);
					dataRow1.setHeightInPoints(30);
					
					int cellNum = 0;
					Cell cell = dataRow1.createCell(cellNum);
					cell.setCellStyle(styles.get("data"));
					cell.setCellValue("项目名称");
					
					cellNum++;
					Cell cell1 = dataRow1.createCell(cellNum);
					cell1.setCellStyle(styles.get("data"));
					cell1.setCellValue("");
					
					cellNum++;
					Cell cell2 = dataRow1.createCell(cellNum);
					cell2.setCellStyle(styles.get("data"));
					cell2.setCellValue(year + SettingsUtils.getSysConfig("projectName", "年教育教学改革专项"));
					
					cellNum++;
					Cell cell3 = dataRow1.createCell(cellNum);
					cell3.setCellStyle(styles.get("data"));
					cell3.setCellValue("");
					
					cellNum++;
					Cell cell4 = dataRow1.createCell(cellNum);
					cell4.setCellStyle(styles.get("data"));
					cell4.setCellValue("");
					
					sheet.addMergedRegion(new CellRangeAddress(4, 4,0, 1));
					sheet.addMergedRegion(new CellRangeAddress(4, 4,2, 4));
					
					
					Row dataRow2 = sheet.createRow(rownum++);
					dataRow2.setHeightInPoints(30);
					
					int cellNum1 = 0;
					Cell cell21 = dataRow2.createCell(cellNum1);
					cell21.setCellStyle(styles.get("data"));
					cell21.setCellValue("项目单位");
					
					cellNum1++;
					Cell cell22 = dataRow2.createCell(cellNum1);
					cell22.setCellStyle(styles.get("data"));
					cell22.setCellValue("");
					
					cellNum1++;
					Cell cell23 = dataRow2.createCell(cellNum1);
					cell23.setCellStyle(styles.get("data"));
					cell23.setCellValue(SettingsUtils.getSysConfig("projectUnit", "中国政法大学"));
					
					cellNum1++;
					Cell cell24 = dataRow2.createCell(cellNum1);
					cell24.setCellStyle(styles.get("data"));
					cell24.setCellValue("");
					
					cellNum1++;
					Cell cell25 = dataRow2.createCell(cellNum1);
					cell25.setCellStyle(styles.get("data"));
					cell25.setCellValue("");
					
					sheet.addMergedRegion(new CellRangeAddress(5, 5,0, 1));
					sheet.addMergedRegion(new CellRangeAddress(5, 5,2, 4));
					
					
					Row dataRow3 = sheet.createRow(rownum++);
					dataRow3.setHeightInPoints(30);
					
					int cellNum2 = 0;
					Cell cell31 = dataRow3.createCell(cellNum2);
					cell31.setCellStyle(styles.get("data"));
					cell31.setCellValue("主管单位及代码");
					
					cellNum2++;
					Cell cell32 = dataRow3.createCell(cellNum2);
					cell32.setCellStyle(styles.get("data"));
					cell32.setCellValue("");
					
					cellNum2++;
					Cell cell33 = dataRow3.createCell(cellNum2);
					cell33.setCellStyle(styles.get("data"));
					cell33.setCellValue(SettingsUtils.getSysConfig("projectCode", "教育部"));
					
					cellNum2++;
					Cell cell34 = dataRow3.createCell(cellNum2);
					cell34.setCellStyle(styles.get("data"));
					cell34.setCellValue("项目类别");
					
					cellNum2++;
					Cell cell35 = dataRow3.createCell(cellNum2);
					cell35.setCellStyle(styles.get("data"));
					cell35.setCellValue(SettingsUtils.getSysConfig("projectCategory", "其他项目"));
					
					sheet.addMergedRegion(new CellRangeAddress(6, 6,0, 1));
					
					Row dataRow4 = sheet.createRow(rownum++);
					dataRow4.setHeightInPoints(30);
					
					int cellNum3 = 0;
					Cell cell41 = dataRow4.createCell(cellNum3);
					cell41.setCellStyle(styles.get("data"));
					cell41.setCellValue("项目开始年份");
					
					cellNum3++;
					Cell cell42 = dataRow4.createCell(cellNum3);
					cell42.setCellStyle(styles.get("data"));
					cell42.setCellValue("");
					
					cellNum3++;
					Cell cell43 = dataRow4.createCell(cellNum3);
					cell43.setCellStyle(styles.get("data"));
					cell43.setCellValue(year);
					
					cellNum3++;
					Cell cell44 = dataRow4.createCell(cellNum3);
					cell44.setCellStyle(styles.get("data"));
					cell44.setCellValue("项目周期");
					
					cellNum3++;
					Cell cell45 = dataRow4.createCell(cellNum3);
					cell45.setCellStyle(styles.get("data"));
					cell45.setCellValue(SettingsUtils.getSysConfig("projectCycle", "1年"));
					
					sheet.addMergedRegion(new CellRangeAddress(7, 7,0, 1));
					
					sheet.setColumnWidth(1, 6000);
					sheet.setColumnWidth(2, 6000);
					sheet.setColumnWidth(3, 6000);
					sheet.setColumnWidth(4, 6000);
					
					String reformInEducation = "一、深入推进高校创新创业教育改革\r\n";
					String basicPosition = "二、巩固本科教学基础地位\r\n";
					String professionalStructure = "三、调整优化学科专业结构\r\n";
					String mechanism = "四、完善协同育人机制\r\n";
					String fuse = "五、着力推进信息技术与教育教学深度融合\r\n";
					String personnelTraining = "六、建立完善拔尖人才培养体制机制\r\n";
					
					if(list != null && list.size() != 0){
						for (EducationProjectExpendAccording educationProject : list) {
							
							String str1 =  educationProject.getReformInEducation();
							if(StringUtils.isNotBlank(str1)){
								str1 = str1.replaceAll("&mdash;", "-");
								str1 = str1.replaceAll("&ldquo;", "“");
								str1 = str1.replaceAll("&rdquo;", "”");
								reformInEducation += str1;
							}
							
							String str2  = educationProject.getBasicPosition();
							if(StringUtils.isNotBlank(str2)){
								str2 = str2.replaceAll("&mdash;", "-");
								str2 = str2.replaceAll("&ldquo;", "“");
								str2 = str2.replaceAll("&rdquo;", "”");
								basicPosition += str2;
							}
							
							String str3  = educationProject.getProfessionalStructure();
							if(StringUtils.isNotBlank(str3)){
								str3 = str3.replaceAll("&mdash;", "-");
								str3 = str3.replaceAll("&ldquo;", "“");
								str3 = str3.replaceAll("&rdquo;", "”");
								professionalStructure += str3;
							}
							
							String str4  = educationProject.getMechanism();
							if(StringUtils.isNotBlank(str4)){
								str4 = str4.replaceAll("&mdash;", "-");
								str4 = str4.replaceAll("&ldquo;", "“");
								str4 = str4.replaceAll("&rdquo;", "”");
								mechanism += str4;
							}
							
							String str5  = educationProject.getFuse();
							if(StringUtils.isNotBlank(str5)){
								str5 = str5.replaceAll("&mdash;", "-");
								str5 = str5.replaceAll("&ldquo;", "“");
								str5 = str5.replaceAll("&rdquo;", "”");
								fuse += str5;
							}
							
							String str6  = educationProject.getPersonnelTraining();
							if(StringUtils.isNotBlank(str6)){
								str6 = str6.replaceAll("&mdash;", "-");
								str6 = str6.replaceAll("&ldquo;", "“");
								str6 = str6.replaceAll("&rdquo;", "”");
								personnelTraining += str6;
							}
						
						}
					}
					
					String content = reformInEducation+basicPosition+professionalStructure+mechanism+fuse+personnelTraining;
					
					Row dataRow5 = sheet.createRow(rownum++);
					dataRow5.setHeightInPoints(500);
					
					int cellNum5 = 0;
					Cell cell51 = dataRow5.createCell(cellNum5);
					cell51.setCellStyle(styles.get("data4"));
					cell51.setCellValue("项目支出立项依据");
					
					
					cellNum5++;
					Cell cell52 = dataRow5.createCell(cellNum5);
					cell52.setCellStyle(styles.get("data5"));
					cell52.setCellValue(content);
					
					cellNum5++;
					Cell cell53 = dataRow5.createCell(cellNum5);
					cell53.setCellStyle(styles.get("data5"));
					cell53.setCellValue("");
					
					cellNum5++;
					Cell cell54 = dataRow5.createCell(cellNum5);
					cell54.setCellStyle(styles.get("data5"));
					cell54.setCellValue("");
					
					cellNum5++;
					Cell cell55 = dataRow5.createCell(cellNum5);
					cell55.setCellStyle(styles.get("data5"));
					cell55.setCellValue("");
					
					sheet.addMergedRegion(new CellRangeAddress(8, 8,1, 4));
				}
				response.reset();
				response.setContentType("application/octet-stream; charset=utf-8");
				//response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
				response.setHeader("Content-Disposition", "attachment; filename="
						+ new String(fileName.getBytes("gbk"), "ISO-8859-1"));
				OutputStream out = response.getOutputStream();  
				wb.write(out);
				wb.dispose();
				out.close();
				
			}else{
				addMessage(redirectAttributes, "没有数据，无法导出");
				return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));

			}
			
			/*educationProjectExpendAccording.setPage(new Page<EducationProjectExpendAccording>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectExpendAccording.class).setDataList(educationProjectExpendAccordingService.findList(educationProjectExpendAccording)).write(response, fileName).dispose();*/
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectExpendAccording educationProjectExpendAccording, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectExpendAccording> list = educationProjectExpendAccordingService.findPage(new Page<EducationProjectExpendAccording>(1, 5), new EducationProjectExpendAccording()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectExpendAccording());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectExpendAccording.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectExpendAccording:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectExpendAccording educationProjectExpendAccording, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectExpendAccording> list = ei.getDataList(EducationProjectExpendAccording.class);
			List<EducationProjectExpendAccording> insertList=new ArrayList<EducationProjectExpendAccording>();
			List<EducationProjectExpendAccording> subList=new ArrayList<EducationProjectExpendAccording>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectExpendAccording>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectExpendAccording zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectExpendAccordingService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectExpendAccording/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectExpendAccording));
		}
	
	
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setAlignment(CellStyle.ALIGN_CENTER);
		// 自动换行  
		style.setWrapText(true);  
		//文字竖向
		//style.setRotation((short)255);
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 14);
		style.setFont(dataFont);
		styles.put("data", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		//		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setBorderTop(CellStyle.BORDER_THIN);//上边框  
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		//headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);
		
		
		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		//文字竖向
		style.setRotation((short)255);
		Font dataFont2 = wb.createFont();
		dataFont2.setFontName("Arial");
		dataFont2.setFontHeightInPoints((short) 14);
		style.setFont(dataFont2);
		styles.put("data4", style);
		
		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		// 自动换行  
		style.setWrapText(true);  
		//文字竖向
		//style.setRotation((short)255);
		Font dataFont3 = wb.createFont();
		dataFont3.setFontName("Arial");
		dataFont3.setFontHeightInPoints((short) 14);
		style.setFont(dataFont3);
		styles.put("data5", style);

		return styles;
	}
	
}