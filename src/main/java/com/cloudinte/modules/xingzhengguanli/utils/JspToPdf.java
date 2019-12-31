package com.cloudinte.modules.xingzhengguanli.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.common.collect.Maps;
import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class JspToPdf {

	public static String[] fontPath = { "C:/WINDOWS/Fonts/simsun.ttc" };

	/**
	 * 输出到流，可以直接下载，可以生成文件。
	 *
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void exportToStream(OutputStream outputStream, String html) throws DocumentException, IOException {

		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();

		//添加中文识别，这里是设置的宋体，Linux下要换成对应的字体
		//fontResolver.addFont("E:/Workspaces/MyEclipse 2017 CI/teshuzhaosheng/src/main/resources/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		for (String path : fontPath) {
			fontResolver.addFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		}

		// 指定html文档
		renderer.setDocumentFromString(html);

		// 解决图片的相对路径问题
		//renderer.getSharedContext().setBaseURL("http://localhost:8080");//导出到服务器

		renderer.layout();
		renderer.createPDF(outputStream, true);
	}

	public static void exportToFile(String filename, String html) throws IOException, DocumentException {
		File file = new File(filename);
		if (file != null) {
			File parentFile = file.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				parentFile.mkdirs();
			}
		}
		OutputStream outputStream = new FileOutputStream(filename);
		exportToStream(outputStream, html);
	}

	/**
	 * freemarker 工具， 获取渲染后的内容
	 *
	 * @param templateFileFullPath
	 *            模板文件全路径
	 * @param dataModel
	 *            渲染的数据
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String getContent(String templateFileFullPath, Object dataModel) throws IOException, TemplateException {
		if (StringUtils.isEmpty(templateFileFullPath)) {
			return "";
		}

		// E:/Workspaces/MyEclipse 2017 CI/teshuzhaosheng/src/main/webapp/WEB-INF/classes/hello.html
		templateFileFullPath = templateFileFullPath.replace("\\\\", "/");
		templateFileFullPath = templateFileFullPath.replace("\\", "/");

		// 模板文件目录 E:/Workspaces/MyEclipse 2017 CI/teshuzhaosheng/src/main/webapp/WEB-INF/classes
		String templateFileDir = templateFileFullPath.substring(0, templateFileFullPath.lastIndexOf("/"));
		// 模板文件名称 hello.html
		String templateFileName = templateFileFullPath.substring(templateFileFullPath.lastIndexOf("/") + 1);

		Configuration config = new Configuration(Configuration.VERSION_2_3_26);
		config.setDefaultEncoding("UTF-8");
		config.setDirectoryForTemplateLoading(new File(templateFileDir));
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		config.setLogTemplateExceptions(false);
		Template template = config.getTemplate(templateFileName);
		StringWriter writer = new StringWriter();
		template.process(dataModel, writer);
		writer.flush();
		return writer.toString();
	}

	public static void exportToStream(ServletOutputStream outputStream, String templateFile, Map<String, Object> data)
			throws IOException, TemplateException, DocumentException {
		exportToStream(outputStream, getContent(templateFile, data));
	}

	public static void exportToFile(String filename, String templateFile, Map<String, Object> data) throws IOException, TemplateException, DocumentException {
		//exportToStream(new FileOutputStream(filename), getContent(templateFile, data));
		exportToFile(filename, getContent(templateFile, data));
	}

	public static void main(String[] args) throws Exception {
		String filename = "D:\\" + (new Random().nextInt(1000000) + 1) + ".pdf";//设置导出路径
		//String templateFile = "C:/Workspaces/MyEclipse2017/teshuzhaosheng/src/main/resources/pdfTemplates/zkzPdf-test.html";
		String templateFile = "D:\\workspace\\ceaie\\src\\main\\resources\\templates\\modules\\pdf\\applyPdf.html";
		//exportToFile(filename, templateFile, null);
		Map<String, Object> map = Maps.newConcurrentMap();
		map.put("dw", "云智小橙");
		map.put("ss", "北京市");
		map.put("lx", "IT");
		map.put("gm", "10-50人");
		map.put("frxm", "yxq");
		map.put("frzw", "财务");
		map.put("lxr1", "ys");
		map.put("lxr1zw", "产品");
		map.put("lxr1s", "12345678");
		map.put("lxr1bgdh", "010-12345678");
		map.put("lxr1yx", "12345678@123.com");
		map.put("lxr2", "hhw");
		map.put("lxr2zw", "zjl");
		map.put("lxr2sj", "12345678901");
		map.put("lxr2bgdh", "010-12345678");
		map.put("lxr2yx", "12345678@123.com");
		map.put("lxr2cz", "lxr2cz");
		map.put("txdz", "北京市海淀区");
		map.put("yzbm", "100000");
		map.put("rhly", "希望加入");
		map.put("sfjr", "☑是  □否");
		
		String fzjg = "<div>□中外合作办学专业委员会</div>"
				+ "<div>□自费出国留学中介服务分会①</div>"
				+ "<div>□职业技术教育国际交流分会</div>"
				+ "<div>□国际文化教育交流志愿者工作委员会</div>"
				+ "<div>□教师进修分会</div>"
				+ "<div>☑教育装备国际交流分会</div>"
				+ "<div>☑中学教育国际交流分会</div>"
				+ "<div>☑教育与文化创意产业分会</div>"
				+ "<div>□民办教育国际交流分会</div>"
				+ "<div>□国际医学教育分会</div>"
				+ "<div>□应用型高校国际交流分会</div>"
				+ "<div>□教师教育国际交流分会</div>"
				+ "<div>□未来教育研究专业委员会</div>"
				+ "<div>□双一流建设高校国际交流分会</div>";
		map.put("fzjg", fzjg);
		
		System.out.println(getContent(templateFile, map));
	}

}
