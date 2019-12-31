package com.cloudinte.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 
 * <pre>
 * Modify Information:
 * Author       Date        Description
 * ============ =========== ============================
 * wtman        2015-05-13  Create this file
 * </pre>
 * 
 * <pre>
 * 报表格式：
 *                              Title
 * Subhead
 * +----------------+--------------+------------+----------+--------+
 * |     Header     |    Header    |   Header   |  Header  | Header |
 * +----------------+--------------+------------+----------+--------+
 * |String          |String        |      Number|   Date   |String  |
 * +----------------+--------------+------------+----------+--------+
 * |String          |String        |      Number|   Date   |String  |
 * +----------------+--------------+------------+----------+--------+
 * |String          |String        |      Number|   Date   |String  |
 * +----------------+--------------+------------+----------+--------+
 * 说明：不支持分页
 * </pre>
 * 
 */
public class Report4 {
	private String title; // 标题
	private String subhead; // 副标题
	private Column[] columns; // 列数组
	private ArrayList<String[]> list; // 报表数据
	private HSSFCellStyle style_title; // 标题风格
	private HSSFCellStyle style_subhead; // 副标题风格
	private HSSFCellStyle[] style_headers; // 第一行风格
	private HSSFCellStyle style_foot; // 最后一行风格
	private HSSFCellStyle[] styles; // 其他列的风格
    private HSSFCellStyle style_special; // 特殊列
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private int rowNum;
	private int colNum;
    private List<Integer> sNo;

	/**
	 * @param title
	 *            String 标题
	 * @param subhead
	 *            String 子标题
	 * @param columns
	 *            Column[] 列对象数组
	 * @param list
	 *            ArrayList 报表数据
	 */
	public Report4(String title, String subhead, Column[] columns, ArrayList<String[]> list) {
		this.title = title;
		this.subhead = subhead;
		this.columns = columns;
		this.list = list;
        this.sNo = new ArrayList<Integer>();
	}
    public Report4(String title, String subhead, Column[] columns, ArrayList<String[]> list, List<Integer> sNo) {
        this.title = title;
        this.subhead = subhead;
        this.columns = columns;
        this.list = list;
        this.sNo = sNo;
    }

	/**
	 * 把报表导出到OutputStream
	 * 
	 * @param stream
	 *            OutputStream
	 * @throws Exception
	 * @since 1.2
	 */
	public void export(java.io.OutputStream stream) throws Exception {
		try {
			generate();
			doExport(stream);
		} catch (Exception e) {
			throw e;
		} finally {
            if (stream != null) {
                stream.close();
            }
        }
	}

	public void doExport(java.io.OutputStream stream) throws Exception {
		try {
			wb.write(stream);
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	public HSSFWorkbook generate() throws Exception {
		HSSFCellStyle style;
		HSSFRow row;
		HSSFCell cell;
		int rowNo = 0;
		try {
			init();

			// 合并一行，主标题
			sheet.addMergedRegion(new CellRangeAddress(rowNo, (short) 0, rowNo,
					(short) (colNum - 1)));
			row = sheet.createRow((short) rowNo);
			row.setHeight((short) 500);
			cell = row.createCell(0);
			cell.setCellStyle(style_title);
			cell.setCellValue(title);

			if (subhead != null && subhead.trim().length() > 0) {
				// 合并一行，副标题
				rowNo++;
				sheet.addMergedRegion(new CellRangeAddress(rowNo, (short) 0, rowNo,
						(short) (colNum - 1)));
				row = sheet.createRow((short) rowNo);
				row.setHeight((short) 400);
				cell = row.createCell(0);
				cell.setCellStyle(style_subhead);
				cell.setCellValue(subhead);
			}

			for (int i = 0; i < rowNum; i++) {
				rowNo++;
				row = sheet.createRow((short) (rowNo));
				row.setHeight((short) 400);
				String[] data = (String[]) list.get(i);

				for (int j = 0; j < colNum; j++) {
					if (i == 0) {
						style = style_headers[j];
					} else if (i == (rowNum - 1)) {
						style = style_foot;
					} else {
                        if(sNo.contains(i)){
                            style = style_special;
                        }else{
                            style = styles[j];
                        }
					}
					cell = row.createCell( j);
					cell.setCellStyle(style);
					cell.setCellValue(data[j]);
				}
			}
			return wb;
		} catch (Exception e) {
			throw e;
		}
	}

	private void init() {
		HSSFFont font;

		wb = new HSSFWorkbook();
		sheet = wb.createSheet("Powered by POI");

		sheet.setMargin(HSSFSheet.TopMargin, 0.4);
		sheet.setMargin(HSSFSheet.BottomMargin, 0.2);
		sheet.setMargin(HSSFSheet.LeftMargin, 0.8);
		sheet.setMargin(HSSFSheet.RightMargin, 0.2);

		// 页面设置
		HSSFPrintSetup printSetup = sheet.getPrintSetup();

		// 纸张大小
		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

		// 打印方向(横向=true, 纵向=false)
		printSetup.setLandscape(false);

		rowNum = list.size();
		colNum = columns.length;

		// 设置列宽
		for (int i = 0; i < colNum; i++) {
            sheet.setColumnWidth(i, columns[i].getWidth());
            
		}

		// 标题风格
		font = wb.createFont(); // font
		font.setFontHeightInPoints((short) 16);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

		style_title = wb.createCellStyle();
		style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style_title.setWrapText(false);
		style_title.setFont(font);

		// 副标题风格
		font = wb.createFont(); // font
		font.setFontHeightInPoints((short) 11);
		font.setFontName("宋体");

		style_subhead = wb.createCellStyle();
		style_subhead.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style_subhead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style_subhead.setWrapText(false);
		style_subhead.setFont(font);

		// 第一行风格
		font = wb.createFont(); // font
		font.setFontHeightInPoints((short) 11);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        
        style_headers = new HSSFCellStyle[colNum];
        for (int i = 0; i < colNum; i++) {
            style_headers[i] = wb.createCellStyle();
            int align = columns[i].getAlign();

            if (align == Column.ALIGN_CENTER) {
                style_headers[i].setAlignment(HSSFCellStyle.ALIGN_CENTER);
            }
            if (align == Column.ALIGN_LEFT) {
                style_headers[i].setAlignment(HSSFCellStyle.ALIGN_LEFT);
            }
            if (align == Column.ALIGN_RIGHT) {
                style_headers[i].setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            }

            style_headers[i].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style_headers[i].setWrapText(false);
            style_headers[i].setFont(font);

            style_headers[i].setBorderBottom((short) 1);
            style_headers[i].setBorderTop((short) 1);
            style_headers[i].setBorderLeft((short) 1);
            style_headers[i].setBorderRight((short) 1);
        }

		// 最后一行风格
		font = wb.createFont(); // font
		font.setFontHeightInPoints((short) 11);
		font.setFontName("宋体");
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

		style_foot = wb.createCellStyle();
		style_foot.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_foot.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style_foot.setWrapText(false);
		style_foot.setFont(font);

		style_foot.setBorderBottom((short) 1);
		style_foot.setBorderTop((short) 1);
		style_foot.setBorderLeft((short) 1);
		style_foot.setBorderRight((short) 1);
        
		// 特殊标明的列
        font = wb.createFont(); // font
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

        style_special = wb.createCellStyle();
        style_special.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style_special.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style_special.setWrapText(false);
        style_special.setFont(font);

        style_special.setBorderBottom((short) 1);
        style_special.setBorderTop((short) 1);
        style_special.setBorderLeft((short) 1);
        style_special.setBorderRight((short) 1);

		// 其他列风格
		font = wb.createFont(); // font
		font.setFontHeightInPoints((short) 11);
		font.setFontName("宋体");

		styles = new HSSFCellStyle[colNum];
		for (int i = 0; i < colNum; i++) {
			styles[i] = wb.createCellStyle();
			int align = columns[i].getAlign();

			if (align == Column.ALIGN_CENTER) {
				styles[i].setAlignment(HSSFCellStyle.ALIGN_CENTER);
			}
			if (align == Column.ALIGN_LEFT) {
				styles[i].setAlignment(HSSFCellStyle.ALIGN_LEFT);
			}
			if (align == Column.ALIGN_RIGHT) {
				styles[i].setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			}

			styles[i].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styles[i].setWrapText(false);
			styles[i].setFont(font);

			styles[i].setBorderBottom((short) 1);
			styles[i].setBorderTop((short) 1);
			styles[i].setBorderLeft((short) 1);
			styles[i].setBorderRight((short) 1);
		}
	}

}
