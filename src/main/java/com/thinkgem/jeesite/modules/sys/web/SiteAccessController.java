package com.thinkgem.jeesite.modules.sys.web;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateTool;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.SiteAccess;
import com.thinkgem.jeesite.modules.sys.service.SiteAccessService;

/**
 * 访问统计相关
 * 
 * @author 廖水平
 */
@Controller
@RequestMapping(value = { "${adminPath}/sys/siteAccess" })
public class SiteAccessController extends BaseController {

	@Autowired
	private SiteAccessService siteAccessService;

	/**
	 * 在线用户列表
	 */
	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping("/onlineUser")
	public String loginUsers(SiteAccess siteAccess, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		siteAccess.setOnline("1");
		Page<SiteAccess> page = siteAccessService.findPage(new Page<SiteAccess>(request, response), siteAccess);
		model.addAttribute("page", page);
		model.addAttribute("onlineUserCount", siteAccessService.findOnlineUserCount());//在线用户数
		model.addAttribute("loginUserCount", siteAccessService.findLoginUserCount());//登录用户数
		setBase64EncodedQueryStringToEntity(request, siteAccess);
		return "modules/sys/onlineUser";
	}

	/**
	 * 将登录用户（按session id）踢下线
	 */
	@RequiresPermissions("sys:siteAccess:kick")
	@RequestMapping("/kickSession")
	public String kickSession(SiteAccess siteAccess, RedirectAttributes redirectAttributes) {
		boolean success = siteAccessService.kickSession(siteAccess);
		addMessage(redirectAttributes, success ? "操作成功" : "操作失败");
		return "redirect:" + adminPath + "/sys/siteAccess/onlineUser?"
				+ getBase64DecodedQueryStringFromEntity(siteAccess);
	}

	/**
	 * 通过账号或姓名、登录时间段，查询某个用户的登录记录
	 */
	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping("/userLoginQuery")
	public String userLoginQuery(SiteAccess siteAccess, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SiteAccess> page = siteAccessService.findPage(new Page<SiteAccess>(request, response), siteAccess);
		model.addAttribute("page", page);
		setBase64EncodedQueryStringToEntity(request, siteAccess);
		return "modules/sys/userLoginQuery";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping("/countMonthByDay")
	public String countMonthByDay(SiteAccess siteAccess, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (StringUtils.isBlank(siteAccess.getLoginYearMonth())) {
			siteAccess.setLoginYearMonth(DateUtils.formatDate(new Date(), "yyyy-MM"));
		}
		setBase64EncodedQueryStringToEntity(request, siteAccess);
		return "modules/sys/countMonthByDay";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping(value = { "/line.option.json" }, headers = { "X-Requested-With=XMLHttpRequest" })
	public String lineOptionJson(SiteAccess siteAccess, ModelMap model) {
		String text = "";
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		if (!StringUtils.isBlank(siteAccess.getLoginYearMonth())) {
			text = siteAccess.getLoginYearMonth() + "月登录人次走势图";
			map = siteAccessService.countMonthByDay(siteAccess);
		} else if (!StringUtils.isBlank(siteAccess.getLoginYear())) {
			text = siteAccess.getLoginYear() + "年登录人次走势图";
			map = siteAccessService.countYearByMonth(siteAccess);
		}
		String subtext = "数据来源：本系统";
		Map<String, LinkedHashMap<String, Long>> dataMap = new HashMap<>();
		dataMap.put("登录人次", map);
		model.addAttribute("text", text);
		model.addAttribute("subtext", subtext);
		model.addAttribute("dataMap", dataMap);
		return "modules/sys/line.option.json";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping(value = "countMonthByDayExport", method = RequestMethod.POST)
	public String countMonthByDayExport(SiteAccess siteAccess, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {

		LinkedHashMap<String, Long> map = siteAccessService.countMonthByDay(siteAccess);
		String title = siteAccess.getLoginYearMonth() + "月份登录人次统计";
		String fileName = title + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";

		try {
			SXSSFWorkbook wb = new SXSSFWorkbook(500);
			Sheet sheet = wb.createSheet(title);

			CellStyle style_center = wb.createCellStyle();
			style_center.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
			style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中 

			CellStyle style_right = wb.createCellStyle();
			style_right.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
			style_right.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 居右

			int rownum = 0;

			Row titleRow = sheet.createRow(rownum++);
			titleRow.createCell(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(style_center);
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, 1));

			Row headerRow = sheet.createRow(rownum++);
			headerRow.setHeightInPoints(16);
			Cell dateCell = headerRow.createCell(0);
			dateCell.setCellValue("日期");
			dateCell.setCellStyle(style_center);
			Cell countCell = headerRow.createCell(1);
			countCell.setCellValue("登录人次");
			countCell.setCellStyle(style_center);

			for (Entry<String, Long> entry : map.entrySet()) {
				String dateStr = entry.getKey();
				Long count = entry.getValue();
				Row row = sheet.createRow(rownum++);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(dateStr);
				cell0.setCellStyle(style_center);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(count);
				cell1.setCellStyle(style_right);
			}

			int dateWidth = "yyyy-MM-dd".getBytes().length * 256;
			sheet.setColumnWidth(0, dateWidth);
			int countWidth = "登录人次".getBytes().length * 256;
			sheet.setColumnWidth(1, countWidth);

			//写出
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(fileName.getBytes("gbk"), "ISO-8859-1"));
			wb.write(response.getOutputStream());
			wb.dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出" + title + "失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/siteAccess/countMonthByDay";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping("/countYearByMonth")
	public String countYearByMonth(SiteAccess siteAccess, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (StringUtils.isBlank(siteAccess.getLoginYear())) {
			siteAccess.setLoginYear(DateUtils.formatDate(new Date(), "yyyy"));
		}
		setBase64EncodedQueryStringToEntity(request, siteAccess);
		return "modules/sys/countYearByMonth";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping(value = "countYearByMonthExport", method = RequestMethod.POST)
	public String countYearByMonthExport(SiteAccess siteAccess, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {

		LinkedHashMap<String, Long> map = siteAccessService.countYearByMonth(siteAccess);
		String title = siteAccess.getLoginYear() + "年登录人次统计";
		String fileName = title + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";

		try {
			SXSSFWorkbook wb = new SXSSFWorkbook(500);
			Sheet sheet = wb.createSheet(title);

			CellStyle style_center = wb.createCellStyle();
			style_center.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
			style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中 

			CellStyle style_right = wb.createCellStyle();
			style_right.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
			style_right.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 居右

			int rownum = 0;

			Row titleRow = sheet.createRow(rownum++);
			titleRow.createCell(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(style_center);
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, 1));

			Row headerRow = sheet.createRow(rownum++);
			headerRow.setHeightInPoints(16);
			Cell dateCell = headerRow.createCell(0);
			dateCell.setCellValue("月份");
			dateCell.setCellStyle(style_center);
			Cell countCell = headerRow.createCell(1);
			countCell.setCellValue("登录人次");
			countCell.setCellStyle(style_center);

			for (Entry<String, Long> entry : map.entrySet()) {
				String dateStr = entry.getKey();
				Long count = entry.getValue();
				Row row = sheet.createRow(rownum++);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(dateStr);
				cell0.setCellStyle(style_center);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(count);
				cell1.setCellStyle(style_right);
			}

			int dateWidth = "yyyy-MM".getBytes().length * 256;
			sheet.setColumnWidth(0, dateWidth);
			int countWidth = "登录人次".getBytes().length * 256;
			sheet.setColumnWidth(1, countWidth);

			//写出
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(fileName.getBytes("gbk"), "ISO-8859-1"));
			wb.write(response.getOutputStream());
			wb.dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出" + title + "失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/siteAccess/countYearByMonth";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping("/countByLoginname")
	public String countByLoginname(SiteAccess siteAccess, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (siteAccess.getPage().getPageSize() < 1) {
			siteAccess.getPage().setPageSize(10);
		}
		if (siteAccess.getLoginDateTimeStart() == null && siteAccess.getLoginDateTimeEnd() == null) {
			siteAccess.setLoginDateTimeStart(DateTool.getThisMonthFirstSecond());
			siteAccess.setLoginDateTimeEnd(DateTool.getThisMonthLastSecond());
		}
		return "modules/sys/countByLoginname";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping(value = { "/pie.option.json" }, headers = { "X-Requested-With=XMLHttpRequest" })
	public String pieOptionJson(SiteAccess siteAccess, ModelMap model) {
		if (siteAccess.getPage().getPageSize() < 1) {
			siteAccess.getPage().setPageSize(10);
		}
		if (siteAccess.getLoginDateTimeStart() == null && siteAccess.getLoginDateTimeEnd() == null) {
			siteAccess.setLoginDateTimeStart(DateTool.getThisMonthFirstSecond());
			siteAccess.setLoginDateTimeEnd(DateTool.getThisMonthLastSecond());
		}
		HashMap<String, Long> dataMap = siteAccessService.countByLoginname(siteAccess);

		String text = "登录次数前" + siteAccess.getPage().getPageSize() + "的用户";
		String subtext = "数据来源：本系统";
		model.put("text", text);
		model.put("subtext", subtext);
		model.put("dataMap", dataMap);
		return "modules/sys/pie.option.json";
	}

	@RequiresPermissions("sys:siteAccess:stat")
	@RequestMapping(value = "countByLoginnameExport", method = RequestMethod.POST)
	public String countByLoginnameExport(SiteAccess siteAccess, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {

		if (siteAccess.getPage().getPageSize() < 1) {
			siteAccess.getPage().setPageSize(10);
		}
		if (siteAccess.getLoginDateTimeStart() == null && siteAccess.getLoginDateTimeEnd() == null) {
			siteAccess.setLoginDateTimeStart(DateTool.getThisMonthFirstSecond());
			siteAccess.setLoginDateTimeEnd(DateTool.getThisMonthLastSecond());
		}

		LinkedHashMap<String, Long> map = siteAccessService.countByLoginname(siteAccess);
		String title = "登录次数前" + siteAccess.getPage().getPageSize() + "的用户统计";
		String fileName = title + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";

		try {
			SXSSFWorkbook wb = new SXSSFWorkbook(500);
			Sheet sheet = wb.createSheet(title);

			CellStyle style_center = wb.createCellStyle();
			style_center.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
			style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中 

			CellStyle style_right = wb.createCellStyle();
			style_right.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
			style_right.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 居右

			int rownum = 0;

			Row titleRow = sheet.createRow(rownum++);
			titleRow.createCell(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(style_center);
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, 1));

			Row headerRow = sheet.createRow(rownum++);
			headerRow.setHeightInPoints(16);
			Cell dateCell = headerRow.createCell(0);
			dateCell.setCellValue("用户");
			dateCell.setCellStyle(style_center);
			Cell countCell = headerRow.createCell(1);
			countCell.setCellValue("登录人次");
			countCell.setCellStyle(style_center);

			for (Entry<String, Long> entry : map.entrySet()) {
				String dateStr = entry.getKey();
				Long count = entry.getValue();
				Row row = sheet.createRow(rownum++);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(dateStr);
				cell0.setCellStyle(style_center);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(count);
				cell1.setCellStyle(style_right);
			}

			int dateWidth = "yyyy-MM".getBytes().length * 256;
			sheet.setColumnWidth(0, dateWidth);
			int countWidth = "登录人次".getBytes().length * 256;
			sheet.setColumnWidth(1, countWidth);

			//写出
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(fileName.getBytes("gbk"), "ISO-8859-1"));
			wb.write(response.getOutputStream());
			wb.dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出" + title + "失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/siteAccess/countByLoginname";
	}
}
