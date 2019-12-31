package com.cloudinte.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.QaAndArticleIndexDao;
import com.thinkgem.jeesite.modules.cms.entity.QaAndArticleIndex;
import com.thinkgem.jeesite.modules.cms.service.QaAndArticleIndexService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 基类
 *
 * @author lsp
 *
 */
public abstract class AbsIndexDao<T> {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private QaAndArticleIndexService qaAndArticleIndexService;
	@Autowired
	private QaAndArticleIndexDao qaAndArticleIndexDao;

	/**
	 * 指定分词器
	 */
	protected abstract Analyzer getAnalyzer();

	/**
	 * 指定索引目录
	 */
	protected abstract Directory getDirectory();

	/**
	 * 转换为统一的索引对象
	 */
	protected abstract Articletemp toArticletemp(T t);

	/**
	 * 创建索引
	 */
	@Transactional(readOnly = false)
	public void save(T t) {
		save(toArticletemp(t));
	}

	private void save(Articletemp articletemp) {
		Document document = DocumentUtils.article2Document(articletemp);
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getAnalyzer());
			indexWriter = new IndexWriter(getDirectory(), indexWriterConfig);
			indexWriter.addDocument(document);
			qaAndArticleIndexService.save(articletemp);
		} catch (Exception e) {
			logger.error("IndexDao.save error", e);
		} finally {
			LuceneUtils.closeIndexWriter(indexWriter);
		}
	}

	/**
	 * 刪除索引
	 */
	public void delete(String id) {
		IndexWriter indexWriter = null;
		try {
			Term term = new Term("id", id);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getAnalyzer());
			indexWriter = new IndexWriter(getDirectory(), indexWriterConfig);
			indexWriter.deleteDocuments(term);// 删除含有指定term的所有文档
		} catch (Exception e) {
			logger.error("IndexDao.delete error", e);
		} finally {
			LuceneUtils.closeIndexWriter(indexWriter);
		}
	}

	/**
	 * 刪除索引
	 */
	public void delete(String id, String type) {
		IndexWriter indexWriter = null;
		try {
			Term term = new Term("id", id);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getAnalyzer());
			indexWriter = new IndexWriter(getDirectory(), indexWriterConfig);
			indexWriter.deleteDocuments(term);// 删除含有指定term的所有文档
			Articletemp articletemp = new Articletemp();
			articletemp.setId(id);
			articletemp.setType(type);
			QaAndArticleIndex qaAndArticleIndex = qaAndArticleIndexService.toQaAndArticleIndex(articletemp);
			qaAndArticleIndexService.delete(qaAndArticleIndex);
		} catch (Exception e) {
			logger.error("IndexDao.delete error", e);
		} finally {
			LuceneUtils.closeIndexWriter(indexWriter);
		}
	}

	/**
	 * 更新索引
	 */
	public void update(T t) {
		update(toArticletemp(t));
	}

	private void update(Articletemp articletemp) {
		Document doc = DocumentUtils.article2Document(articletemp);
		IndexWriter indexWriter = null;
		try {
			Term term = new Term("id", articletemp.getId());
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getAnalyzer());
			indexWriter = new IndexWriter(getDirectory(), indexWriterConfig);
			indexWriter.updateDocument(term, doc);// 先删除，后创建。
			QaAndArticleIndex qaAndArticleIndex = qaAndArticleIndexService.toQaAndArticleIndex(articletemp);
			// 自动建索引的时候，UserUtils.getUser()不存在的
			if (StringUtils.isBlank(UserUtils.getUser().getId())) {
				if (StringUtils.isBlank(qaAndArticleIndex.getId())) {
					qaAndArticleIndex.setId(IdGen.uuid());
					qaAndArticleIndex.setCreateBy(new User("1"));
					qaAndArticleIndex.setUpdateBy(qaAndArticleIndex.getCreateBy());
					qaAndArticleIndex.setCreateDate(new Date());
					qaAndArticleIndex.setUpdateDate(qaAndArticleIndex.getCreateDate());
					qaAndArticleIndexDao.insert(qaAndArticleIndex);
				} else {
					qaAndArticleIndex.setUpdateBy(new User("1"));
					qaAndArticleIndex.setUpdateDate(new Date());
					qaAndArticleIndexDao.update(qaAndArticleIndex);
				}
			} else {
				qaAndArticleIndexService.save(qaAndArticleIndex);
			}
		} catch (Exception e) {
			logger.error("IndexDao.update error", e);
		} finally {
			LuceneUtils.closeIndexWriter(indexWriter);
		}
	}

	public Page<Articletemp> searchPageByAfter(HttpServletRequest request, HttpServletResponse response, String keyword, String[] fields, int pageIndex, int pageSize, Directory... directories) {
		MultiReader multiReader = null;
		List<Articletemp> list = new ArrayList<>();
		Page<Articletemp> page = new Page<>(request, response);
		try {
			multiReader = createMultiReader(directories);
			IndexSearcher indexSearcher = new IndexSearcher(multiReader);
			QueryParser queryParser = new MultiFieldQueryParser(fields, getAnalyzer());
			Query query = queryParser.parse(QueryParser.escape(keyword));
			int count = indexSearcher.count(query);
			page.setCount(count);
			//获取上一页的最后一个元素
			ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, query, indexSearcher);
			//通过最后一个元素去搜索下一页的元素
			TopDocs tds = indexSearcher.searchAfter(lastSd, query, pageSize);

			//
			Highlighter highlighter = prepareHighlighter(100, query);

			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = indexSearcher.doc(sd.doc);
				Articletemp articletemp = DocumentUtils.document2Ariticle(doc);
				//
				String text = highlighter.getBestFragment(getAnalyzer(), "title", doc.get("title"));
				if (text != null) {
					articletemp.setTitle(text);
				}
				//
				list.add(articletemp);
			}

		} catch (IOException | ParseException | InvalidTokenOffsetsException e) {
			e.printStackTrace();
		} finally {
			try {
				multiReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		page.setPageNo(pageIndex);
		page.setPageSize(pageSize);
		page.setList(list);
		return page;
	}

	private MultiReader createMultiReader(Directory[] directories) throws IOException {
		Set<DirectoryReader> directoryReaderSet = Sets.newHashSet();
		for (int i = 0; i < directories.length; i++) {
			Directory directory = directories[i];
			if (directory != null) {
				try {
					DirectoryReader directoryReader = DirectoryReader.open(directory);
					directoryReaderSet.add(directoryReader);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		MultiReader multiReader = new MultiReader(directoryReaderSet.toArray(new DirectoryReader[] {}));
		return multiReader;
	}

	public QueryResult searchPageByAfter(String keyword, String[] fields, int pageIndex, int pageSize) {
		QueryResult queryResult = new QueryResult();
		List<Articletemp> list = new ArrayList<>();
		try {
			DirectoryReader ireader = DirectoryReader.open(getDirectory());

			IndexSearcher searcher = new IndexSearcher(ireader);

			QueryParser parser = new MultiFieldQueryParser(fields, getAnalyzer());
			Query query = parser.parse(QueryParser.escape(keyword));
			int countAll = searcher.count(query);
			queryResult.setCount(countAll);
			//获取上一页的最后一个元素
			ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, query, searcher);
			//通过最后一个元素去搜索下一页的元素
			TopDocs tds = searcher.searchAfter(lastSd, query, pageSize);

			//
			Highlighter highlighter = prepareHighlighter(100, query);

			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				Articletemp articletemp = DocumentUtils.document2Ariticle(doc);

				//
				String text = highlighter.getBestFragment(getAnalyzer(), "title", doc.get("title"));
				if (text != null) {
					articletemp.setTitle(text);
				}
				//

				list.add(articletemp);
			}
			ireader.close();
		} catch (IOException | ParseException | InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		queryResult.setList(list);
		queryResult.setCount(10);
		return queryResult;
	}

	public QueryResult searchPageByAfter(String keyword, int pageIndex, int pageSize) {
		String[] fields = { "title", "content" };
		return searchPageByAfter(keyword, fields, pageIndex, pageSize);
	}

	private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher) throws IOException {
		if (pageIndex == 1) {
			return null;//如果是第一页就返回空
		}
		int num = pageSize * (pageIndex - 1);//获取上一页的最后数量
		TopDocs tds = searcher.search(query, num);
		if (num > tds.scoreDocs.length) {
			num = tds.scoreDocs.length;
		}
		return tds.scoreDocs[num - 1];
	}

	public Document query(String tableName, String filedName, String value) throws IOException, ParseException {
		DirectoryReader ireader = DirectoryReader.open(getDirectory());
		IndexSearcher isearcher = new IndexSearcher(ireader);
		Analyzer analyzer = null;
		QueryParser parser = new QueryParser(tableName + filedName, analyzer);
		Query query = parser.parse(QueryParser.escape(tableName));//单条件
		//查询前多少行数据
		TopScoreDocCollector topCollector = TopScoreDocCollector.create(100);
		isearcher.search(query, topCollector);
		// 取数范围
		ScoreDoc[] hits = topCollector.topDocs(1, 20).scoreDocs;
		Document hitDoc = null;
		for (int i = 0; i < hits.length; i++) {
			hitDoc = isearcher.doc(hits[i].doc);
			logger.debug("hitDoc ---> {}", hitDoc);
		}
		ireader.close();
		return hitDoc;
	}

	public QueryResult getById(String id) {
		QueryResult queryResult = new QueryResult();
		List<Articletemp> list = new ArrayList<>();
		Term term = new Term("id", id);
		Query query = new TermQuery(term);
		DirectoryReader directoryReader;
		try {
			directoryReader = DirectoryReader.open(getDirectory());
			IndexSearcher searcher = new IndexSearcher(directoryReader);
			TopDocs topDocs = searcher.search(query, 1);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document hitDoc = searcher.doc(scoreDoc.doc);
				Articletemp articletemp = DocumentUtils.document2Ariticle(hitDoc);
				list.add(articletemp);
			}
			return new QueryResult(1, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queryResult;
	}

	/**
	 * 查询
	 *
	 * @param queryString  查询关键字
	 * @param fields       查询哪些属性
	 * @param fragmentSize 摘要字数
	 * @param firstResult  开始索引
	 * @param maxResult    最多查询几条
	 * @return
	 */
	public QueryResult search(String queryString, String[] fields, int fragmentSize, int firstResult, int maxResult) {
		QueryResult queryResult = new QueryResult();
		List<Articletemp> list = new ArrayList<>();
		try {
			DirectoryReader directoryReader = DirectoryReader.open(getDirectory());
			IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
			QueryParser queryParser = new MultiFieldQueryParser(fields, getAnalyzer());
			Query query = queryParser.parse(QueryParser.escape(queryString));
			int topN = firstResult + maxResult;
			TopDocs topDocs = indexSearcher.search(query, topN);
			int count = topDocs.totalHits;// 总记录数
			logger.debug("总记录数为：{}", topDocs.totalHits);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;// 本页返回的记录
			Highlighter highlighter = prepareHighlighter(fragmentSize, query); // 高亮处理

			// 处理结果
			logger.debug("firstResult + maxResult = {}", firstResult + maxResult);
			logger.debug("scoreDocs.length = {}", scoreDocs.length);
			int endIndex = Math.min(firstResult + maxResult, scoreDocs.length);
			for (int i = firstResult; i < endIndex; i++) {
				Document hitDoc = indexSearcher.doc(scoreDocs[i].doc);
				Articletemp articletemp = DocumentUtils.document2Ariticle(hitDoc);
				// 高亮后的标题
				String highlightedTextFragment = highlighter.getBestFragment(getAnalyzer(), "title", hitDoc.get("title"));
				logger.debug("highlightedTextFragment = {}", highlightedTextFragment);
				if (highlightedTextFragment != null) {
					articletemp.setTitle(highlightedTextFragment);
				}
				list.add(articletemp);
			}
			directoryReader.close();
			return new QueryResult(count, list);
		} catch (Exception e) {
			logger.error("IndexDao.search error", e);
		}
		return queryResult;
	}

	/**
	 * 搜索关键字高亮显示
	 *
	 * @param fragmentSize 摘要字数
	 */
	private Highlighter prepareHighlighter(int fragmentSize, Query query) {
		// 高亮格式
		Formatter formatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
		Scorer source = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, source);
		// 摘要
		Fragmenter fragmenter = new SimpleFragmenter(fragmentSize);
		highlighter.setTextFragmenter(fragmenter);
		return highlighter;
	}

}
