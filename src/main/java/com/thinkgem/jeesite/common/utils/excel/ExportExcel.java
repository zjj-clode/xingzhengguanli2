/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出 @see org.apache.poi.ss.SpreadsheetVersion）
 * 
 * @author ThinkGem
 * @version 2013-04-21
 */
public class ExportExcel {

	private static Logger log = LoggerFactory.getLogger(ExportExcel.class);

	/**
	 * 工作薄对象
	 */
	private SXSSFWorkbook wb;

	/**
	 * 工作表对象
	 */
	private Sheet sheet;

	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;

	/**
	 * 当前行号
	 */
	private int rownum;

	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	List<Object[]> annotationList = Lists.newArrayList();

	public List<Object[]> getAnnotationList() {
		return annotationList;
	}

	public void setAnnotationList(List<Object[]> annotationList) {
		this.annotationList = annotationList;
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param cls
	 *            实体对象，通过annotation.ExportField获取标题
	 */
	public ExportExcel(String title, Class<?> cls) {
		this(title, cls, 1);
	}
	

	/**
	 * 构造函数
	 *
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param cls
	 *            实体对象，通过annotation.ExportField获取标题
	 * @Param properties
	 * 				用户所选择的需要导出的列
	 */
	public ExportExcel(String title, Class<?> cls,String[] properties) {
		this(title, cls, 1,properties);
	}
	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param cls
	 *            实体对象，通过annotation.ExportField获取标题
	 * @param type
	 *            导出类型（1:导出数据；2：导出模板）
	 */
	public ExportExcel(String title, Class<?> cls, int type, String[] properties) {
		// Get annotation field 
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs) {
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type() == 0 || ef.type() == type)) {
				//如果f包含在properties中，则添加到annotationList中
				for (String property :properties) {
					if (f.getName().equalsIgnoreCase(property)){
						annotationList.add(new Object[] { ef, f });
					}
				}
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms) {
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type() == 0 || ef.type() == type)) {
				for (String property :properties) {
					if (m.getName().equalsIgnoreCase(property)){
						annotationList.add(new Object[] { ef, m });
					}
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(((ExcelField) o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		for (Object[] os : annotationList) {
			String t = ((ExcelField) os[0]).title();
			// 如果是导出，则去掉注释
			if (type == 1) {
				String[] ss = StringUtils.split(t, "**", 2);
				if (ss.length == 2) {
					t = ss[0];
				}
			}
			headerList.add(t);
		}
		initialize(title, headerList);
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param cls
	 *            实体对象，通过annotation.ExportField获取标题
	 * @param type
	 *            导出类型（1:导出数据；2：导出模板）
	 * @param groups
	 *            导入分组
	 */
	public ExportExcel(String title, Class<?> cls, int type, int... groups) {
		// Get annotation field 
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs) {
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type() == 0 || ef.type() == type)) {
				if (groups != null && groups.length > 0) {
					boolean inGroup = false;
					for (int g : groups) {
						if (inGroup) {
							break;
						}
						for (int efg : ef.groups()) {
							if (g == efg) {
								inGroup = true;
								annotationList.add(new Object[] { ef, f });
								break;
							}
						}
					}
				} else {
					annotationList.add(new Object[] { ef, f });
				}
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms) {
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type() == 0 || ef.type() == type)) {
				if (groups != null && groups.length > 0) {
					boolean inGroup = false;
					for (int g : groups) {
						if (inGroup) {
							break;
						}
						for (int efg : ef.groups()) {
							if (g == efg) {
								inGroup = true;
								annotationList.add(new Object[] { ef, m });
								break;
							}
						}
					}
				} else {
					annotationList.add(new Object[] { ef, m });
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(((ExcelField) o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		for (Object[] os : annotationList) {
			String t = ((ExcelField) os[0]).title();
			// 如果是导出，则去掉注释
			if (type == 1) {
				String[] ss = StringUtils.split(t, "**", 2);
				if (ss.length == 2) {
					t = ss[0];
				}
			}
			headerList.add(t);
		}
		initialize(title, headerList);
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param headers
	 *            表头数组
	 */
	public ExportExcel(String title, String[] headers) {
		initialize(title, Lists.newArrayList(headers));
	}
	
	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param jsonheaders
	 *            json表头
	 */
	public ExportExcel(String title, String jsonheaders,int headlevel,int colsize) {
		initialize(title, jsonheaders,headlevel,colsize);
	}
	
	


	int  createMulHeader(String jsontext,int excelrow,int excelcolumn,int headlevel)
	{
		  int cellCountSum = 0;
		JSONArray jarr=JSONArray.fromObject(jsontext);
		JSONObject jobj=null;
	    int rowcount = 0;
	    int colnum=0;
	    int cellCount=0;
	    Row headerRow=sheet.getRow(excelrow-1);
		for(int i=0;i<jarr.size();i++)//遍历子集
		{
			jobj=JSONObject.fromObject(jarr.get(i));
			if(jobj.containsKey("subnode"))
			{
				rowcount=0;
				cellCount=createMulHeader(jobj.get("subnode").toString(),excelrow+1,excelcolumn,headlevel);
			}
			else
			{
				rowcount = headlevel-excelrow+1;
				cellCount=1;
			}
			System.out.println(excelcolumn+":"+headerRow+":"+excelrow+":"+rownum);
			Cell cell =headerRow.createCell(excelcolumn-1);
			
			cell.setCellValue(jobj.get("nodename").toString());
			if((excelrow+rowcount-1-(excelrow-1))>0||(excelcolumn+cellCount-2-(excelcolumn-1))>0)
			{
			sheet.addMergedRegion(new CellRangeAddress(excelrow-1, excelrow+rowcount-1,
					excelcolumn-1,excelcolumn+cellCount-2));
			}
			System.out.println(jobj.get("nodename")+":"+(excelrow-1)+":"+(excelrow+rowcount-1)+":"+(excelcolumn-1)+":"+(excelcolumn+cellCount-2));
			sheet.autoSizeColumn(colnum);
			cellCountSum+=cellCount;
			excelcolumn+=cellCount;
			}
        return cellCountSum;
	}
	
	
	
	/**
	 * 初始化函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param headerList
	 *            表头列表
	 */
	private void initialize(String title, String jsontext,int headlevel,int colsize) {
		wb = new SXSSFWorkbook(500);
		sheet = wb.createSheet("Export");
		styles = createStyles(wb);
		if (StringUtils.isNotBlank(title)) {
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(),
					titleRow.getRowNum(), colsize-1));
		}
		int nowrowunm=rownum;
		for(int i=0;i<headlevel;i++)
		{
		   System.out.println("create row "+i);
			sheet.createRow(rownum++);
		}
		
		createMulHeader(jsontext,nowrowunm+1,1,headlevel);
		
		for (int i = 0; i < colsize; i++) {
			int colWidth = sheet.getColumnWidth(i) * 2;
			sheet.setColumnWidth(i, colWidth > 4000 ? 4000 : colWidth);
			
		}
		log.debug("Initialize success11.");
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param headerList
	 *            表头列表
	 */
	public ExportExcel(String title, List<String> headerList) {
		initialize(title, headerList);
	}

	/**
	 * 初始化函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param headerList
	 *            表头列表
	 */
	private void initialize(String title, List<String> headerList) {
		wb = new SXSSFWorkbook(500);
		sheet = wb.createSheet("Export");
		styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)) {
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(),
					titleRow.getRowNum(), headerList.size() - 1));
		}
		if (headerList == null) {
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length == 2) {
				cell.setCellValue(ss[0]);
				Comment comment = sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			} else {
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
		}

		for (int i = 0; i < headerList.size(); i++) {
			int colWidth = sheet.getColumnWidth(i) * 2;
			sheet.setColumnWidth(i, colWidth > 4000 ? 4000 : colWidth);
			
		}
		log.debug("Initialize success11.");
	}

	/**
	 * 创建表格样式
	 * 
	 * @param wb
	 *            工作薄对象
	 * @return 样式列表
	 */
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
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
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
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);

		return styles;
	}

	/**
	 * 添加一行
	 * 
	 * @return 行对象
	 */
	public Row addRow() {
		return sheet.createRow(rownum++);
	}

	/**
	 * 添加一个单元格
	 * 
	 * @param row
	 *            添加的行
	 * @param column
	 *            添加列号
	 * @param val
	 *            添加值
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val) {
		return this.addCell(row, column, val, 0, Class.class);
	}

	/**
	 * 添加一个单元格
	 * 
	 * @param row
	 *            添加的行
	 * @param column
	 *            添加列号
	 * @param val
	 *            添加值
	 * @param align
	 *            对齐方式（1：靠左；2：居中；3：靠右）
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType) {
		
		
		Cell cell = row.createCell(column);
		String cellvalue = "";
		CellStyle style = styles.get("data" + (align >= 1 && align <= 3 ? align : ""));
		DataFormat format = wb.createDataFormat();
		CellStyle style1 = styles.get("data" + (align >= 1 && align <= 3 ? align : ""));
		style1.setDataFormat(format.getFormat("yyyy-MM-dd"));
		try {

			if (val == null) {
				cell.setCellValue("");
			} else if (val instanceof String) {
				cellvalue = val.toString();
				/*if(StringUtils.isNoneBlank(cellvalue))
				{
					cellvalue=cellvalue.trim();
					if(cellvalue.length()>50)
						cellvalue=cellvalue.substring(0, 50);
				}*/
				cell.setCellValue(cellvalue);
			} else if (val instanceof Integer) {
				cell.setCellValue((Integer) val);
			} else if (val instanceof Long) {
				cell.setCellValue((Long) val);
			} else if (val instanceof Double) {
				cell.setCellValue((Double) val);
			} else if (val instanceof Float) {
				cell.setCellValue(val.toString());
			} else if (val instanceof Number) {
				cell.setCellValue(((Number) val).doubleValue());
			} else if (val instanceof Date) {

				cell.setCellValue((Date) val);
				cell.setCellStyle(style1);
			} else {
				if (fieldType != Class.class) {
					cellvalue = (String) fieldType.getMethod("setValue", Object.class).invoke(null, val);
					/*
						if(StringUtils.isNoneBlank(cellvalue))
						{	
							cellvalue=cellvalue.trim();
							if(cellvalue.length()>50)
								cellvalue=cellvalue.substring(0, 50);
						}*/
					cell.setCellValue(cellvalue);
				} else {
					cellvalue = (String) Class
							.forName(
									this.getClass()
											.getName()
											.replaceAll(this.getClass().getSimpleName(),
													"fieldtype." + val.getClass().getSimpleName() + "Type"))
							.getMethod("setValue", Object.class).invoke(null, val);
					/*	if(StringUtils.isNoneBlank(cellvalue))
						{
							cellvalue=cellvalue.trim();
							if(cellvalue.length()>50)
								cellvalue=cellvalue.substring(0, 50);
						}*/
					cell.setCellValue(cellvalue);

				}

			}
		} catch (Exception ex) {
			log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());

			cell.setCellValue(val.toString());
			if (val instanceof Date) {
				cell.setCellStyle(style1);
			} else {
				cell.setCellStyle(style);
			}
		}

		return cell;
	}

	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * 
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataList(List<E> list) {
		for (E e : list) {
			int colunm = 0;
			Row row = addRow();
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList) {
				ExcelField ef = (ExcelField) os[0];
				Object val = null;
				// Get entity value
				try {
					if (StringUtils.isNotBlank(ef.value())) {
						val = Reflections.invokeGetter(e, ef.value());
					} else {
						if (os[1] instanceof Field) {
							val = Reflections.invokeGetter(e, ((Field) os[1]).getName());
						} else if (os[1] instanceof Method) {
							val = Reflections.invokeMethod(e, ((Method) os[1]).getName(), new Class[] {},
									new Object[] {});
						}
					}
					// If is dict, get dict label
					if (StringUtils.isNotBlank(ef.dictType())) {
						val = DictUtils.getDictLabel(val == null ? "" : val.toString(), ef.dictType(), "");
					}
				} catch (Exception ex) {
					// Failure to ignore
					log.info(ex.toString());
					val = "";
				}
				//	log.debug(val.toString()+":error:");
				this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
				sb.append(val + ", ");
			}
			//	log.debug("Write success: [" + row.getRowNum() + "] " + sb.toString());
		}
		return this;
	}
	
	/**
	 * 添加数据
	 * dataList=[{column_5=测试, column_6=测试1, column_1=S20153131236, column_2=2, column_3=测试, column_4=测试, column_0=于梦佳}, {column_5=测试2, column_6=测试2, column_1=S20153131240, column_2=2, column_3=测试2, column_4=测试2, column_0=周田田}]
	 * 该方法只针对于自动生成的表有用，数据格式如上
	 * @return ExportExcel 数据列表
	 */
	public ExportExcel setDataList(List<HashMap<String,String>> list,int columnSize) {
		for (HashMap<String,String> map : list) {
			int colunm = 0;
			Row row = addRow();
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i<columnSize;i++){
				this.addCell(row, colunm++, map.get("column_"+i), 2, null);
				sb.append(map.get("column_"+i)+", ");
			}
		}
		return this;
	}

	

	
	/**
	 * 添加数据
	 * dataList=[{column_5=测试, column_6=测试1, column_1=S20153131236, column_2=2, column_3=测试, column_4=测试, column_0=于梦佳}, {column_5=测试2, column_6=测试2, column_1=S20153131240, column_2=2, column_3=测试2, column_4=测试2, column_0=周田田}]
	 * 该方法只针对于自动生成的表有用，数据格式如上
	 * @return ExportExcel 数据列表
	 */

	public ExportExcel setDataList(List<HashMap<String,Object>> list,String[] colheader,Boolean ismer,Boolean[] colmer,int[] rowspan) {
		
		if(!ismer)//不需要合并单元格
		{
			for (HashMap<String, Object> map : list) {
			int colunm = 0;
			Row row = addRow();
			for(int i = 0;i<colheader.length;i++)
			  {
				this.addCell(row, colunm++, map.get(colheader[i]), 2, null);	
			   }
			}
		}
		if(ismer)//需要合并单元格
		{
			int frow=rownum,lrow=0;
			int rowindex=rownum;
			int rownumt=0;
			for(int i=0;i<list.size();i++)
				addRow();
			for (HashMap<String, Object> map : list)
			{
			int colunm = 0;
		   
			Row row=this.getSheet().getRow(rowindex++);//获得当前列
			for(int i = 0;i<colheader.length;i++)//列
			  {
				if(colmer[i])//该列合并
				{
					if(rowspan[rownumt]>0)
					{
						lrow=frow+rowspan[rownumt]-1;
						this.sheet.addMergedRegion(new CellRangeAddress(frow,lrow,colunm,colunm));
						System.out.println(rownumt+":"+frow+":"+lrow+":"+colunm+":"+map.get(colheader[i]));
						try
						{
						  this.addCell(row, colunm, map.get(colheader[i]), 2, null);
						}catch(Exception ex){}
					}
					colunm++;
				}
				else//非合并列
				  this.addCell(row, colunm++, map.get(colheader[i]), 2, null);	 
			   }
			frow++;
			rownumt++;
			}
		}
		return this;
	}
	
	
	/**
	 * 添加数据
	 * dataList=[{column_5=测试, column_6=测试1, column_1=S20153131236, column_2=2, column_3=测试, column_4=测试, column_0=于梦佳}, {column_5=测试2, column_6=测试2, column_1=S20153131240, column_2=2, column_3=测试2, column_4=测试2, column_0=周田田}]
	 * 该方法只针对于自动生成的表有用，数据格式如上
	 * @return ExportExcel 数据列表
	 */

	/**
	 * 输出数据流
	 * 
	 * @param os
	 *            输出数据流
	 */
	public ExportExcel write(OutputStream os) throws IOException {
		wb.write(os);
		return this;
	}

	/**
	 * 输出到客户端
	 * 
	 * @param fileName
	 *            输出文件名
	 */
	public ExportExcel write(HttpServletResponse response, String fileName) throws IOException {
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		//response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String(fileName.getBytes("gbk"), "ISO-8859-1"));
		write(response.getOutputStream());
		return this;
	}

	/**
	 * 输出到文件
	 * 
	 * @param fileName
	 *            输出文件名
	 */
	public ExportExcel writeFile(String name) throws FileNotFoundException, IOException {
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}

	/**
	 * 清理临时文件
	 */
	public ExportExcel dispose() {
		wb.dispose();
		return this;
	}

	public Sheet getSheet() {
		return sheet;
	}




	//	/**
	//	 * 导出测试
	//	 */
	//	public static void main(String[] args) throws Throwable {
	//		
	//		List<String> headerList = Lists.newArrayList();
	//		for (int i = 1; i <= 10; i++) {
	//			headerList.add("表头"+i);
	//		}
	//		
	//		List<String> dataRowList = Lists.newArrayList();
	//		for (int i = 1; i <= headerList.size(); i++) {
	//			dataRowList.add("数据"+i);
	//		}
	//		
	//		List<List<String>> dataList = Lists.newArrayList();
	//		for (int i = 1; i <=1000000; i++) {
	//			dataList.add(dataRowList);
	//		}
	//
	//		ExportExcel ee = new ExportExcel("表格标题", headerList);
	//		
	//		for (int i = 0; i < dataList.size(); i++) {
	//			Row row = ee.addRow();
	//			for (int j = 0; j < dataList.get(i).size(); j++) {
	//				ee.addCell(row, j, dataList.get(i).get(j));
	//			}
	//		}
	//		
	//		ee.writeFile("target/export.xlsx");
	//
	//		ee.dispose();
	//		
	//		log.debug("Export success.");
	//		
	//	}
	
	/**
	 * 添加数据
	 * dataList=[{column_5=测试, column_6=测试1, column_1=S20153131236, column_2=2, column_3=测试, column_4=测试, column_0=于梦佳}, {column_5=测试2, column_6=测试2, column_1=S20153131240, column_2=2, column_3=测试2, column_4=测试2, column_0=周田田}]
	 * 该方法只针对于自动生成的表有用，数据格式如上
	 * @return ExportExcel 数据列表
	 */

	public ExportExcel setDataList(List<HashMap<String,Object>> list,String[] colheader,Boolean ismer,Boolean[] colmer,int[][] rowspan) {
		
		if(!ismer)//不需要合并单元格
		{
			for (HashMap<String, Object> map : list) {
			int colunm = 0;
			Row row = addRow();
			for(int i = 0;i<colheader.length;i++)
			  {
				this.addCell(row, colunm++, map.get(colheader[i]), 2, null);	
			   }
			}
		}
		if(ismer)//需要合并单元格
		{
			int frow=rownum,lrow=0;
			int rowindex=rownum;
			int rownumt=0;
			for(int i=0;i<list.size();i++){
				addRow();
			}
			for (HashMap<String, Object> map : list){
				int colunm = 0;
	   
				Row row=this.getSheet().getRow(rowindex++);//获得当前列
				for(int i = 0;i<colheader.length;i++){//列 
					if(colmer[i]){//该列合并
						if(rowspan[i][rownumt]>0){
							lrow=frow+rowspan[i][rownumt]-1;
							this.sheet.addMergedRegion(new CellRangeAddress(frow,lrow,colunm,colunm));
							System.out.println(rownumt+":"+frow+":"+lrow+":"+colunm+":"+map.get(colheader[i]));
							try
							{
								this.addCell(row, colunm, map.get(colheader[i]), 2, null);
							}catch(Exception ex){}
						}
						colunm++;
					}else//非合并列
						this.addCell(row, colunm++, map.get(colheader[i]), 2, null);	 
				}
				frow++;
				rownumt++;
			}
		}
		return this;
	}

}
