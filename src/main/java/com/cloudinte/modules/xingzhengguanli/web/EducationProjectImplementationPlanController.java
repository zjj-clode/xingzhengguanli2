package com.cloudinte.modules.xingzhengguanli.web;
import com.thinkgem.jeesite.common.config.Global;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
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

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectImplementationPlan;
import com.cloudinte.modules.xingzhengguanli.service.EducationProjectImplementationPlanService;

/**
 * 项目支出实施方案Controller
 * @author dcl
 * @version 2019-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/educationProjectImplementationPlan")
public class EducationProjectImplementationPlanController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "项目支出实施方案";

	@Autowired
	private EducationProjectImplementationPlanService educationProjectImplementationPlanService;
	
	@ModelAttribute
	public EducationProjectImplementationPlan get(@RequestParam(required=false) String id) {
		EducationProjectImplementationPlan entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = educationProjectImplementationPlanService.get(id);
		}
		if (entity == null){
			entity = new EducationProjectImplementationPlan();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:view")
	@RequestMapping(value = {"list", ""})
	public String list(EducationProjectImplementationPlan educationProjectImplementationPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){//科室
			educationProjectImplementationPlan.setUser(opUser);
		}
		
		Page<EducationProjectImplementationPlan> page = educationProjectImplementationPlanService.findPage(new Page<EducationProjectImplementationPlan>(request, response), educationProjectImplementationPlan); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "educationProjectImplementationPlan");
		setBase64EncodedQueryStringToEntity(request, educationProjectImplementationPlan);
		return "modules/xingzhengguanli/educationProjectImplementationPlan/educationProjectImplementationPlanList";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:view")
	@RequestMapping(value = "form")
	public String form(EducationProjectImplementationPlan educationProjectImplementationPlan, Model model) {
		
		if(StringUtils.isBlank(educationProjectImplementationPlan.getId())){
			User opUser = UserUtils.getUser();
			educationProjectImplementationPlan.setUser(opUser);
			
			String currentYear = DateUtils.formatDate(new Date(), "yyyy");
			
			String projectStartYear = String.valueOf(Integer.valueOf(currentYear) + 1) ;
			educationProjectImplementationPlan.setProjectName(projectStartYear + SettingsUtils.getSysConfig("projectImplementationName", "年253教育教学改革专项"));
			educationProjectImplementationPlan.setProjectCategory(SettingsUtils.getSysConfig("projectCategory", "其他项目"));
			educationProjectImplementationPlan.setProjectCode(SettingsUtils.getSysConfig("projectImplementationCode", "[105]教育部"));
			educationProjectImplementationPlan.setProjectCycle(SettingsUtils.getSysConfig("projectCycle", "1年"));
			educationProjectImplementationPlan.setProjectStartYear(projectStartYear);
			educationProjectImplementationPlan.setProjectUnit(SettingsUtils.getSysConfig("projectUnit", "中国政法大学"));
			
		}
		
		model.addAttribute("educationProjectImplementationPlan", educationProjectImplementationPlan);
		model.addAttribute("ename", "educationProjectImplementationPlan");
		return "modules/xingzhengguanli/educationProjectImplementationPlan/educationProjectImplementationPlanForm";
	}

	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:edit")
	@RequestMapping(value = "save")
	public String save(EducationProjectImplementationPlan educationProjectImplementationPlan, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, educationProjectImplementationPlan)){
			return form(educationProjectImplementationPlan, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(educationProjectImplementationPlan.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		educationProjectImplementationPlanService.save(educationProjectImplementationPlan);
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:edit")
	@RequestMapping(value = "disable")
	public String disable(EducationProjectImplementationPlan educationProjectImplementationPlan, RedirectAttributes redirectAttributes) {
		educationProjectImplementationPlanService.disable(educationProjectImplementationPlan);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:edit")
	@RequestMapping(value = "delete")
	public String delete(EducationProjectImplementationPlan educationProjectImplementationPlan, RedirectAttributes redirectAttributes) {
		educationProjectImplementationPlanService.delete(educationProjectImplementationPlan);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
	}
	
	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(EducationProjectImplementationPlan educationProjectImplementationPlan, RedirectAttributes redirectAttributes) {
		educationProjectImplementationPlanService.deleteByIds(educationProjectImplementationPlan);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EducationProjectImplementationPlan educationProjectImplementationPlan, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User opUser = UserUtils.getUser();
			if("6".equals(opUser.getUserType())){//科室
				educationProjectImplementationPlan.setUser(opUser);
			}
			
			List<EducationProjectImplementationPlan> planList = educationProjectImplementationPlanService.findList(educationProjectImplementationPlan);
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			
			if(planList != null && planList.size() != 0){
				Map<String, List<EducationProjectImplementationPlan>> planListMap = new HashMap<String, List<EducationProjectImplementationPlan>>();
				
				for (EducationProjectImplementationPlan educationProjectImplementationPlan2 : planList) {
					
					List<EducationProjectImplementationPlan> list = planListMap.get(educationProjectImplementationPlan2.getProjectStartYear());
					if(list == null){
						list = new ArrayList<EducationProjectImplementationPlan>();
					}
					list.add(educationProjectImplementationPlan2);
					
					planListMap.put(educationProjectImplementationPlan2.getProjectStartYear(), list);
				}
				
				SXSSFWorkbook wb = new SXSSFWorkbook(500); 
				Map<String, CellStyle> styles  = createStyles(wb);
				
				for (String year : planListMap.keySet()) {
					
					List<EducationProjectImplementationPlan> list = planListMap.get(year);
					
					Sheet sheet = wb.createSheet(year+"项目支出实施方案");
					int  rownum = 0;
					
					Row rownull = sheet.createRow(rownum++);
					Cell cellNull = rownull.createCell(0);
					cellNull.setCellValue("");
					sheet.addMergedRegion(new CellRangeAddress(0, 0,0, 4));
					
					Row titleRow = sheet.createRow(rownum++);
					titleRow.setHeightInPoints(50);
					Cell titleCell = titleRow.createCell(0);
					titleCell.setCellStyle(styles.get("title"));
					titleCell.setCellValue("项目支出实施方案");
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
					cell2.setCellValue(year + SettingsUtils.getSysConfig("projectImplementationName", "年253教育教学改革专项"));
					
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
					cell33.setCellValue(SettingsUtils.getSysConfig("projectImplementationCode", "[105]教育部"));
					
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
						for (EducationProjectImplementationPlan educationProject : list) {
							
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
					cell51.setCellValue("项目支出实施方案");
					
					
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
				addMessage(redirectAttributes, "无数据");
				return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));

			}
			
			/*String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			educationProjectImplementationPlan.setPage(new Page<EducationProjectImplementationPlan>(request, response, -1));
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectImplementationPlan.class).setDataList(educationProjectImplementationPlanService.findList(educationProjectImplementationPlan)).write(response, fileName).dispose();*/
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(EducationProjectImplementationPlan educationProjectImplementationPlan, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<EducationProjectImplementationPlan> list = educationProjectImplementationPlanService.findPage(new Page<EducationProjectImplementationPlan>(1, 5), new EducationProjectImplementationPlan()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new EducationProjectImplementationPlan());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, EducationProjectImplementationPlan.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:educationProjectImplementationPlan:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(EducationProjectImplementationPlan educationProjectImplementationPlan, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EducationProjectImplementationPlan> list = ei.getDataList(EducationProjectImplementationPlan.class);
			List<EducationProjectImplementationPlan> insertList=new ArrayList<EducationProjectImplementationPlan>();
			List<EducationProjectImplementationPlan> subList=new ArrayList<EducationProjectImplementationPlan>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<EducationProjectImplementationPlan>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(EducationProjectImplementationPlan zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					educationProjectImplementationPlanService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/educationProjectImplementationPlan/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(educationProjectImplementationPlan));
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