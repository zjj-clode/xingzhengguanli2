package com.cloudinte.common.utils;

import java.io.Serializable;


public class Articletemp  implements Serializable{

     private static final long serialVersionUID = 1L;
	 private String id;  
	 private String title;  
	 private String content;
	 private String link;
	 private String askpname;
	 private String type;
	 private String createDate;
	 private String searchFrom;
	public String getAskpname() {
		return askpname;
	}
	public void setAskpname(String askpname) {
		this.askpname = askpname;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setId(String parseInt) {
		// TODO Auto-generated method stub
		this.id=parseInt;
	}
	public void setTitle(String string) {
		// TODO Auto-generated method stub
		this.title=string;
		
	}
	public void setContent(String string) {
		// TODO Auto-generated method stub
		this.content=string;
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public String getTitle() {
		// TODO Auto-generated method stub
		return this.title;
	}
	public String getContent() {
		// TODO Auto-generated method stub
		return this.content;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String string) {
		this.createDate = string;
	}
	public String getSearchFrom() {
		return searchFrom;
	}
	public void setSearchFrom(String searchFrom) {
		this.searchFrom = searchFrom;
	}
	@Override
	public String toString() {
		return "Articletemp [id=" + id + ", title=" + title + ", content=" + content + ", link=" + link + ", askpname="
				+ askpname + ", type=" + type + ",  createDate=" + createDate + ",  searchFrom=" + searchFrom + "]";
	}
	
}
