package com.cloudinte.common.utils;

import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

public class LuceneUtils {
	private static Logger logger = LoggerFactory.getLogger(LuceneUtils.class);

	public static final String lucene_index_dir_cms = "/lucene/index/cms";
	public static final String lucene_index_dir_qa = "/lucene/index/qa";
	public static final String lucene_index_dir = "/lucene/index";

	private static Directory directory;
	private static Directory cmsDirectory;
	private static Directory qaDirectory;
	
	private static Analyzer analyzer;
	
	static {
		try {
			//
			String luceneDirectory = SettingsUtils.getSysConfig("lucene_index_dir", lucene_index_dir);
			luceneDirectory = getRealPath(luceneDirectory);
			logger.debug("luceneDirectory----->{}", luceneDirectory);
			directory = FSDirectory.open(Paths.get(luceneDirectory));
			
			// CMS
			String cmsLuceneDirectory = SettingsUtils.getSysConfig("lucene_index_dir_cms", lucene_index_dir_cms);
			cmsLuceneDirectory = getRealPath(cmsLuceneDirectory);
			logger.debug("cmsLuceneDirectory----->{}", cmsLuceneDirectory);
			cmsDirectory = FSDirectory.open(Paths.get(cmsLuceneDirectory));

			// QA
			String qaLuceneDirectory = SettingsUtils.getSysConfig("lucene_index_dir_qa", lucene_index_dir_qa);
			qaLuceneDirectory = getRealPath(qaLuceneDirectory);
			logger.debug("qaLuceneDirectory----->{}", qaLuceneDirectory);
			qaDirectory = FSDirectory.open(Paths.get(qaLuceneDirectory));

			//analyzer = new StandardAnalyzer();
			//analyzer = new PaodingAnalyzer();
			//analyzer = new IKAnalyzer();
			analyzer = new IKAnalyzer5x();
			
		} catch (Exception e) {
			logger.error("LuceneUtils error!", e);
		}
	}

	public static String getRealPath(String path) {
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			return request.getSession().getServletContext().getRealPath(path);
		} else {
			// /D:/apache-tomcat-8.0.48/webapps/zhaosheng/WEB-INF/classes/
			// /usr/local/zhaosheng/WEB-INF/classes/
			String os = System.getProperty("os.name");
			if (!StringUtils.isBlank(os) && os.toLowerCase().contains("windows")) {
				return LuceneUtils.class.getResource("/").getPath().substring(1).replace("/WEB-INF/classes/", path);
			} else {
				return LuceneUtils.class.getResource("/").getPath().replace("/WEB-INF/classes/", path);
			}
		}
	}

	/**
	 * 默认为： /lucene/index
	 */
	public static Directory getDirectory() {
		return directory;
	}
	
	/**
	 * cms的lucene索引目录，由配置文件中的lucene_index_dir_cms指定，默认为： /lucene/index/cms
	 */
	public static Directory getCmsDirectory() {
		return cmsDirectory;
	}

	/**
	 * qa的lucene索引目录，由配置文件中的lucene_index_dir_qa指定，默认为： /lucene/index/qa
	 */
	public static Directory getQaDirectory() {
		return qaDirectory;
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static void closeIndexWriter(IndexWriter indexWriter) {
		if (indexWriter != null) {
			try {
				indexWriter.close();
			} catch (Exception e2) {
				logger.error("indexWriter.close error", e2);
			}
		}
	}
}
