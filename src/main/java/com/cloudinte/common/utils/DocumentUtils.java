package com.cloudinte.common.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.thinkgem.jeesite.common.utils.StringUtils;

public class DocumentUtils {
	  public static Document article2Document(Articletemp article) {  
	        Document doc = new Document();  
	        //id,不分词，进行索引和存储
	        doc.add(new StringField("id", article.getId(), Store.YES));
	        //title ,分词，索引，存储
	        doc.add(new TextField("title",StringUtils.trimToEmpty(article.getTitle()) , Store.YES));
	        
	        //type ,分词，索引，存储
	        doc.add(new TextField("type",StringUtils.trimToEmpty(article.getType()) , Store.YES));
	        //link,不分词，不索引，存储
	        doc.add(new StoredField("link", article.getLink()));
	        //描述 分词，索引，存储
	        doc.add(new TextField("content",StringUtils.trimToEmpty(article.getContent()),Store.YES));
	        //提问人 分词，索引，存储
	        doc.add(new TextField("askpname", StringUtils.trimToEmpty(article.getAskpname()), Store.YES));
	        doc.add(new TextField("searchFrom",StringUtils.trimToEmpty(article.getSearchFrom()),Store.YES));
	        //创建日期分词，索引，存储
	        doc.add(new TextField("createDate", StringUtils.trimToEmpty(article.getCreateDate()), Store.YES));
	        return doc;  
	    }  
	  
	    public static Articletemp document2Ariticle(Document doc) {  
	        Articletemp article = new Articletemp();  
	        article.setId(doc.get("id"));
	        article.setTitle(doc.get("title"));  
	        article.setLink(doc.get("link"));
	        article.setType(doc.get("type"));
	        article.setContent(doc.get("content"));
	        article.setAskpname(doc.get("askpname"));
	        article.setSearchFrom(doc.get("searchFrom"));
	        article.setCreateDate(doc.get("createDate"));
	        return article;  
	    }  
	    
}
