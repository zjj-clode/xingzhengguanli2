package com.cloudinte.common.utils;

import java.text.SimpleDateFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * cms
 *
 * @author lsp
 *
 */
@Component
public class CmsIndexDao extends AbsIndexDao<Article> {
	
	@Override
	protected Analyzer getAnalyzer() {
		return LuceneUtils.getAnalyzer();
	}
	
	@Override
	protected Directory getDirectory() {
		return LuceneUtils.getCmsDirectory();
	}

	@Override
	protected Articletemp toArticletemp(Article t) {
		Articletemp articletemp = new Articletemp();
		articletemp.setId(t.getId());
		articletemp.setTitle(t.getTitle());
		articletemp.setType("article");
		if (StringUtils.trim(t.getLink()) == null || StringUtils.trim(t.getLink()) == "" || StringUtils.trim(t.getLink()).length() == 0) {
			articletemp.setLink("view-" + t.getCategory().getId() + "-" + t.getId() + SettingsUtils.getSysConfig("urlSuffix", ".html"));
		} else if (StringUtils.endsWithIgnoreCase(t.getLink(), ".pdf")) {
			StringBuffer sb = new StringBuffer();
			sb.append("/viewPDF?addr=");
			sb.append(t.getLink());
			articletemp.setLink(sb.toString());

		} else {
			articletemp.setLink(t.getLink());
		}

		articletemp.setContent(t.getDescription() + t.getKeywords());
		articletemp.setAskpname("系统管理员");
		articletemp.setSearchFrom(t.getCategory().getName());
		if (t.getReleaseDate() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(t.getReleaseDate());
			articletemp.setCreateDate(dateString);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(t.getCreateDate());
			articletemp.setCreateDate(dateString);
		}
		return articletemp;
	}
	
	public QueryResult searchArticle(String queryString, int firstResult, int maxResult) {
		return super.search(queryString, new String[] { "title", "content" }, 10, firstResult, maxResult);
	}
}
