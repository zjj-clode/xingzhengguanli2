package com.cloudinte.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.persistence.Page;

/**
 * cms
 *
 * @author lsp
 *
 */
@Component
public class AllIndexDao extends AbsIndexDao<Articletemp> {

	@Override
	protected Analyzer getAnalyzer() {
		return LuceneUtils.getAnalyzer();
	}

	@Override
	protected Directory getDirectory() {
		return LuceneUtils.getDirectory();
	}

	@Override
	public void save(Articletemp t) {
		throw new RuntimeException("不支持");
	}

	@Override
	public void update(Articletemp t) {
		throw new RuntimeException("不支持");
	}

	public QueryResult searchArticle(String queryString, int firstResult, int maxResult) {
		return super.search(queryString, new String[] { "title" }, 10, firstResult, maxResult);
	}

	@Override
	protected Articletemp toArticletemp(Articletemp t) {
		return t;
	}

	public QueryResult searchAll(String queryString, int firstResult, int maxResult) {
		String[] fields = new String[] { "title" };
		return super.search(queryString, fields, 50, firstResult, maxResult);
	}

	public Page<Articletemp> searchAllPageByAfter(HttpServletRequest request, HttpServletResponse response, String queryString, int pageIndex, int pageSize) {
		String[] fields = new String[] { "title" };

		//return super.searchPageByAfter(queryString, fields, pageIndex, pageSize);

		return super.searchPageByAfter(request, response, queryString, fields, pageIndex, pageSize, LuceneUtils.getCmsDirectory(), LuceneUtils.getQaDirectory());

	}
}
