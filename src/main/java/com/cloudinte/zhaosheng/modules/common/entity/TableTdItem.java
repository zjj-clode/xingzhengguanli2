package com.cloudinte.zhaosheng.modules.common.entity;

public class TableTdItem {

	public TableTdItem(String tdvalue,int rowspan,int colspan)
	{
		this.tdvalue=tdvalue;
		this.rowspan=rowspan;
		this.colspan=colspan;
	}
	private String tdvalue;
	private int rowspan;
	private int colspan;
	public String getTdvalue() {
		return tdvalue;
	}
	public void setTdvalue(String tdvalue) {
		this.tdvalue = tdvalue;
	}
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
}
