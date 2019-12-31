package com.cloudinte.zhaosheng.modules.common.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 统计模板Entity
 * @author hhw
 * @version 2018-09-06
 */
public class StaSql extends DataEntity<StaSql> {
	
	private static final long serialVersionUID = 1L;
	private String  staname;		// 统计名称
	private String  stasql;		// 统计SQL
	private String  headinf;		// 表头信息
	private String  statype;		// 统计类型
	private Integer headlevel;//表头维度
	private Integer sort;//排序
	private String colinf;//表头信息
	private String mercol;//需要合并行的列
	//
	String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public void processEmptyArrayParam() {
		if (ids != null && ids.length < 1) {
			ids = null;
		}
	}
	
	
	public StaSql() {
		super();
	}

	public StaSql(String id){
		super(id);
	}

	@Length(min=1, max=64, message="统计名称长度必须介于 1 和 64 之间")
	@ExcelField(title="统计名称", align=2, sort=1 ,dictType="")
	public String getStaname() {
		return staname;
	}

	public void setStaname(String staname) {
		this.staname = staname;
	}
	
	@ExcelField(title="统计SQL", align=2, sort=2 ,dictType="")
	public String getStasql() {
		return stasql;
	}

	public void setStasql(String stasql) {
		this.stasql = stasql;
	}
	
	@ExcelField(title="表头信息", align=2, sort=3 ,dictType="")
	public String getHeadinf() {
		return headinf;
	}

	public void setHeadinf(String headinf) {
		this.headinf = headinf;
	}
	
	@Length(min=0, max=64, message="统计类型长度必须介于 0 和 64 之间")
	@ExcelField(title="统计类型", align=2, sort=4 ,dictType="statype")
	public String getStatype() {
		return statype;
	}

	public void setStatype(String statype) {
		this.statype = statype;
	}

	public Integer getHeadlevel() {
		return headlevel;
	}

	public void setHeadlevel(Integer headlevel) {
		this.headlevel = headlevel;
	}

	public String getColinf() {
		return colinf;
	}

	public void setColinf(String colinf) {
		this.colinf = colinf;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getMercol() {
		return mercol;
	}

	public void setMercol(String mercol) {
		this.mercol = mercol;
	}

	
}