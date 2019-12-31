package com.cloudinte.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CreateHtml {
	private static Logger logger = LoggerFactory.getLogger(CreateHtml.class);

	// templatePath模板文件存放路径
	// templateName 模板文件名称
	// filename 生成的文件名称
	public static void analysisTemplate(String templatePath, String templateName, String fileName, Map<?, ?> root) throws Exception {
		try {
			Configuration config = new Configuration();
			// 设置要解析的模板所在的目录，并加载模板文件
			config.setDirectoryForTemplateLoading(new File(templatePath));
			// 设置包装器，并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper());

			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			// 否则会出现乱码
			Template template = config.getTemplate(templateName, "UTF-8");
			// 合并数据模型与模板
			FileOutputStream fos = new FileOutputStream(fileName);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (TemplateException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 静态化页面。<br/>
	 * 实现方法: HttpClient请求链接，获取到HTML文档流，保存到本地xxx.html文件。
	 *
	 * @param url
	 *            需要转换的链接
	 * @param path
	 *            文件要保存的真实路径
	 * @param name
	 *            文件要保存的名字
	 * @param extension
	 *            文件要保存的扩展名.html/.htm
	 * @return
	 */
	public static boolean staticHtml(String url, String path, String name, String extension) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		logger.debug("url--->{}", url);
		HttpUriRequest request = new HttpGet(url);
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			if (response != null && response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String content = EntityUtils.toString(entity, "UTF-8");
					//logger.debug("content--->{}", content);
					//
					File filePath = new File(path);
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					//logger.debug("path--->{}", path);
					path = StringUtils.appendIfMissing(path, File.separator);
					String pathname = path + name + extension;
					logger.debug("pathname--->{}", pathname);
					File file = new File(pathname);
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
					out.write(content);
					out.flush();
					out.close();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 文章静态化
	 *
	 * <pre>
	 * html/栏目id/发布年份/view-文章id.html
	 * html/9f8552614ac94310b32e18218e32b25f/2018/view-075229574ceb44f191c9252ee641bd1d.html
	 * </pre>
	 */
	public static boolean staticArticle(Article article, HttpServletRequest request) {
		return staticArticle(article, Servlets.getBasePathWithoutContextPath(request), Servlets.getRealPath(SettingsUtils.getSysConfig("cms.static.baseDir", "/s")));
	}

	/**
	 * 文章静态化
	 *
	 * @param article
	 * @param basePathWithoutContextPath
	 *            Servlets.getBasePathWithoutContextPath()，如：http://localhost
	 * @param baseDir
	 *            Servlets.getRealPath("/html")，如：/opt/zhaosheng/html
	 * @return
	 */
	public static boolean staticArticle(Article article, String basePathWithoutContextPath, String baseDir) {
		// http://localhost/zhaosheng/f/view-9f8552614ac94310b32e18218e32b25f-1c310be5afdd480ba3da6fc745b9c8f3.htm
		String url = basePathWithoutContextPath + CmsUtils.getUrl(article);
		String path = baseDir + File.separator + article.getCategory().getId() + File.separator;
		String name = "view-" + article.getId();
		String extension = ".html";
		return CreateHtml.staticHtml(url, path, name, extension);
	}

	/**
	 * 栏目静态化
	 *
	 * <pre>
	 * html/栏目id/list-页码.html
	 * html/9f8552614ac94310b32e18218e32b25f/list-1.html
	 * html/9f8552614ac94310b32e18218e32b25f/list-2.html
	 * </pre>
	 */
	public static boolean staticCategory(Category category, HttpServletRequest request) {
		return staticCategory(category, Servlets.getBasePathWithoutContextPath(request), Servlets.getRealPath(SettingsUtils.getSysConfig("cms.static.baseDir", "/s")));
	}

	/**
	 * 栏目静态化
	 *
	 * @param category
	 * @param basePathWithoutContextPath
	 *            Servlets.getBasePathWithoutContextPath()，如：http://localhost
	 * @param baseDir
	 *            Servlets.getRealPath("/html")，如：/opt/zhaosheng/html
	 * @return
	 */
	public static boolean staticCategory(Category category, String basePathWithoutContextPath, String baseDir) {
		if ("".equals(category.getModule()) || "link".equals(category.getModule()) || !StringUtils.isBlank(category.getHref())) { // 外部链接
			return true;
		}
		long count = CmsUtils.geArticleCountByCategoryId(category.getId());
		int pageSize = Integer.valueOf(Global.getConfig("page.pageSize"));
		long maxPageNo = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
		String path = baseDir + File.separator + category.getId() + File.separator;
		String extension = ".html";
		// http://localhost/zhaosheng/f/list-9f8552614ac94310b32e18218e32b25f.htm
		int pageNo = 1;
		String url_ = basePathWithoutContextPath + CmsUtils.getUrl(category) + "?pageSize=" + pageSize;
		boolean flag = true;
		while (flag && pageNo <= maxPageNo) {
			String url = url_ + "&pageNo=" + (pageNo < 1 ? 1 : pageNo);
			String name = "list-" + pageNo;
			flag = CreateHtml.staticHtml(url, path, name, extension);
			pageNo++;
		}
		return true;
	}

	/**
	 * 首页静态化
	 *
	 * <pre>
	 * html / index.html
	 * </pre>
	 */
	public static boolean staticIndex(HttpServletRequest request) {
		return staticIndex(Servlets.getBasePath(), Servlets.getRealPath(SettingsUtils.getSysConfig("cms.static.baseDir", "/s")));
	}

	/**
	 * 首页静态化
	 *
	 * @param basePath
	 *            Servlets.getBasePath()，如：http://localhost/zhaosheng
	 * @param baseDir
	 *            Servlets.getRealPath("/html")，如：/opt/zhaosheng/html
	 * @return
	 */
	public static boolean staticIndex(String basePath, String baseDir) {
		// 请求动态地址： http://localhost/zhaosheng/f/index
		String url = basePath + Global.getFrontPath() + "/index";
		String path = baseDir + File.separator;
		String name = "index";
		String extension = ".html";
		return CreateHtml.staticHtml(url, path, name, extension);
	}

}
