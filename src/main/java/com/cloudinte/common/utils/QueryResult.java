package com.cloudinte.common.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;

public class QueryResult {

	private int count;

	private List<Articletemp> list = Lists.newArrayList();

	public QueryResult() {
		super();
	}

	public QueryResult(int count, List<Articletemp> list) {
		super();
		this.setCount(count);
		this.setList(list);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Articletemp> getList() {
		return list;
	}

	public void setList(List<Articletemp> list) {
		this.list = list;
	}

	public void setList(Page<Articletemp> articletempPage) {
		// TODO Auto-generated method stub
		
	}
}
