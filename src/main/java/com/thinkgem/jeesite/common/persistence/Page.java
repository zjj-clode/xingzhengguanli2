/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.config.Global;

/**
 * 分页类
 *
 * @author ThinkGem
 * @version 2013-7-2
 * @param <T>
 */
public class Page<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）

	private long count;// 总记录数，设置为“-1”表示不查询总数

	private int first;// 首页索引
	private int last;// 尾页索引
	private int prev;// 上一页索引
	private int next;// 下一页索引

	private boolean firstPage;//是否是第一页
	private boolean lastPage;//是否是最后一页

	private int length = 8;// 显示页面长度
	private final int slider = 1;// 前后显示页面长度

	private List<T> list = new ArrayList<>();

	private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc

	private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。

	private String funcParam = ""; // 函数的附加参数，第三个参数值。

	private String message = ""; // 设置提示消息，显示在“共n条”之后

	public Page() {
		this.pageSize = -1;
	}

	/**
	 * 构造方法
	 *
	 * @param request  传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public Page(HttpServletRequest request, HttpServletResponse response) {
		this(request, response, -2);
	}

	/**
	 * 构造方法
	 *
	 * @param request         传递 repage 参数，来记住页码
	 * @param response        用于设置 Cookie，记住页码
	 * @param defaultPageSize 默认分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public Page(HttpServletRequest request, HttpServletResponse response, int defaultPageSize) {
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)) {
			//	CookieUtils.setCookie(response, "pageNo", no);
			try {
				this.setPageNo(Integer.parseInt(no));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} /*else if (request.getParameter("repage") != null) {
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)) {
				this.setPageNo(Integer.parseInt(no));
			}
			}*/
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");

		if (StringUtils.isNumeric(size)) {
			//CookieUtils.setCookie(response, "pageSize", size);
			try {
				this.setPageSize(Integer.parseInt(size));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} /*else if (request.getParameter("repage") != null) {
			no = CookieUtils.getCookie(request, "pageSize");
			if (StringUtils.isNumeric(size)) {
				this.setPageSize(Integer.parseInt(size));
			}
			} */else if (defaultPageSize != -2) {
			this.pageSize = defaultPageSize;

		}
		// 设置排序参数
		String orderBy = request.getParameter("orderBy");

		if (StringUtils.isNotBlank(orderBy)) {
			this.setOrderBy(orderBy);
		}

		////System.out.println(this);
	}

	/**
	 * 构造方法
	 *
	 * @param pageNo   当前页码
	 * @param pageSize 分页大小
	 */
	public Page(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}

	/**
	 * 构造方法
	 *
	 * @param pageNo   当前页码
	 * @param pageSize 分页大小
	 * @param count    数据条数
	 */
	public Page(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}

	/**
	 * 构造方法
	 *
	 * @param pageNo   当前页码
	 * @param pageSize 分页大小
	 * @param count    数据条数
	 * @param list     本页数据对象列表
	 */
	public Page(int pageNo, int pageSize, long count, List<T> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.list = list;
	}

	/**
	 * 初始化参数
	 */
	public void initialize() {

		//1
		this.first = 1;

		this.last = (int) (count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);

		if (this.count % this.pageSize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}

		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage = true;
		}

		if (this.pageNo >= this.last) {
			this.pageNo = this.last;
			this.lastPage = true;
		}

		if (this.pageNo < this.last - 1) {
			this.next = this.pageNo + 1;
		} else {
			this.next = this.last;
		}

		if (this.pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}

		//2
		if (this.pageNo < this.first) {// 如果当前页小于首页
			this.pageNo = this.first;
		}

		if (this.pageNo > this.last) {// 如果当前页大于尾页
			this.pageNo = this.last;
		}

	}

	/**
	 * 默认输出当前分页标签 <div class="page">${page}</div>
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("<div class=\"col-sm-5\">");
		sb.append("<div class=\"dataTables_info\" id=\"example1_info\" role=\"status\" aria-live=\"polite\">共 " + count + "  条，当前显示 " + ((pageNo - 1) * pageSize + 1) + " 到 " + (pageNo * pageSize > count ? count : pageNo * pageSize) + " 条</div>");
		sb.append("</div>");

		sb.append("<div class=\"col-sm-7\">");
		sb.append("<div class=\"dataTables_paginate paging_simple_numbers\" id=\"example1_paginate\">");
		sb.append("<ul class=\"pagination\">");
		if (pageNo == first) {// 如果是首页
			sb.append("<li class=\"paginate_button previous disabled\" id=\"example1_previous\"><a href=\"#\" aria-controls=\"example1\" data-dt-idx=\"0\" tabindex=\"0\">上一页</a></li>");
		} else {
			sb.append("<li class=\"paginate_button previous\" id=\"example1_previous\" ><a href=\"javascript:\"onclick=\"" + funcName + "(" + prev + "," + pageSize + ",'" + funcParam
					+ "');\" aria-controls=\"example1\" data-dt-idx=\"0\" tabindex=\"0\">上一页</a></li>");
		}

		int begin = pageNo - length / 2;

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first) + "</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
		}

		if (pageNo == last) {
			sb.append("<li class=\"paginate_button next disabled\" id=\"example1_next\"><a href=\"#\" aria-controls=\"example1\" data-dt-idx=\"2\" tabindex=\"0\">下一页</a></li>");
		} else {
			sb.append("<li class=\"paginate_button next\" id=\"example1_next\"><a href=\"javascript:\" onclick=\"" + funcName + "(" + next + "," + pageSize + ",'" + funcParam
					+ "');\" aria-controls=\"example1\" data-dt-idx=\"2\" tabindex=\"0\">下一页</a></li>");
		}

		sb.append("</ul>");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * 获取分页HTML代码
	 *
	 * @return
	 */
	public String getHtml() {
		return toString();
	}

	//	public static void main(String[] args) {
	//		Page<String> p = new Page<String>(3, 3);
	//		//System.out.println(p);
	//		//System.out.println("首页："+p.getFirst());
	//		//System.out.println("尾页："+p.getLast());
	//		//System.out.println("上页："+p.getPrev());
	//		//System.out.println("下页："+p.getNext());
	//	}

	/**
	 * 获取设置总数
	 *
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置数据总数
	 *
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count) {
			pageNo = 1;
		}
	}

	/**
	 * 获取当前页码
	 *
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页码
	 *
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 获取页面大小
	 *
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置页面大小（最大500）
	 *
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;// > 500 ? 500 : pageSize;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 首页索引
	 *
	 * @return
	 */
	@JsonIgnore
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 *
	 * @return
	 */
	//@JsonIgnore
	public int getLast() {
		return last;
	}

	/**
	 * 获取页面总数
	 *
	 * @return getLast();
	 */
	@JsonIgnore
	public int getTotalPage() {
		return count == 0 ? 0 : getLast() == 0 ? 1 : getLast();
	}

	/**
	 * 是否为第一页
	 *
	 * @return
	 */
	@JsonIgnore
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 *
	 * @return
	 */
	@JsonIgnore
	public boolean isLastPage() {
		return lastPage;
	}

	/**
	 * 上一页索引值
	 *
	 * @return
	 */
	@JsonIgnore
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 下一页索引值
	 *
	 * @return
	 */
	@JsonIgnore
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	/**
	 * 获取本页数据对象列表
	 *
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 *
	 * @param list
	 */
	public Page<T> setList(List<T> list) {
		this.list = list;
		initialize();
		return this;
	}

	/**
	 * 获取查询排序字符串
	 *
	 * @return
	 */
	@JsonIgnore
	public String getOrderBy() {
		// SQL过滤，防止注入
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if (sqlPattern.matcher(orderBy).find()) {
			return "";
		}
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获取点击页码调用的js函数名称 function ${page.funcName}(pageNo){location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 *
	 * @return
	 */
	@JsonIgnore
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 *
	 * @param funcName 默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * 获取分页函数的附加参数
	 *
	 * @return
	 */
	@JsonIgnore
	public String getFuncParam() {
		return funcParam;
	}

	/**
	 * 设置分页函数的附加参数
	 *
	 * @return
	 */
	public void setFuncParam(String funcParam) {
		this.funcParam = funcParam;
	}

	/**
	 * 设置提示消息，显示在“共n条”之后
	 *
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 分页是否有效
	 *
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize == -1;
	}

	/**
	 * 是否进行总数统计
	 *
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.count == -1;
	}

	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult() {
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}

	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults() {
		return getPageSize();
	}

	//	/**
	//	 * 获取 Spring data JPA 分页对象
	//	 */
	//	public Pageable getSpringPage(){
	//		List<Order> orders = new ArrayList<Order>();
	//		if (orderBy!=null){
	//			for (String order : StringUtils.split(orderBy, ",")){
	//				String[] o = StringUtils.split(order, " ");
	//				if (o.length==1){
	//					orders.add(new Order(Direction.ASC, o[0]));
	//				}else if (o.length==2){
	//					if ("DESC".equals(o[1].toUpperCase())){
	//						orders.add(new Order(Direction.DESC, o[0]));
	//					}else{
	//						orders.add(new Order(Direction.ASC, o[0]));
	//					}
	//				}
	//			}
	//		}
	//		return new PageRequest(this.pageNo - 1, this.pageSize, new Sort(orders));
	//	}
	//
	//	/**
	//	 * 设置 Spring data JPA 分页对象，转换为本系统分页对象
	//	 */
	//	public void setSpringPage(org.springframework.data.domain.Page<T> page){
	//		this.pageNo = page.getNumber();
	//		this.pageSize = page.getSize();
	//		this.count = page.getTotalElements();
	//		this.list = page.getContent();
	//	}

	public String getPageStr(String basestr) {
		StringBuilder sb = new StringBuilder();

		//System.out.println(pageNo);
		if (pageNo != first) {// 如果是首页
			sb.append("<a class=\"back\" href=\"javascript:page(1," + pageSize + ")\"><img src=\"" + basestr + "/images/ic_back.png\"  /></a>");
		}

		int begin = pageNo - length / 2;
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}
		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<span class=\"current\">" + i + "</span>");
			} else {
				sb.append("<a href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a>");
			}
		}
		if (pageNo != last) {
			sb.append("<a class=\"next\" href=\"javascript:page(" + last + "," + pageSize + ")\"><img src=\"" + basestr + "/images/ic_next.png\" /></a>");
		}
		return sb.toString();
	}

	public String getFrontPageStr() {

		// 不超过2页，没有必要显示分页按钮
		if (getTotalPage() < 2) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		length = 6;//新页面8个页码放不下

		if (pageNo != first) {// 如果不是首页
			sb.append("<li class=\"last\"><a rel=\"pre\" href=\"javascript:page(" + 1 + "," + pageSize + ")\"><em>«首页</em></a></li>");
			sb.append("<li class=\"last\"><a rel=\"next\" href=\"javascript:page(" + (pageNo - 1) + "," + pageSize + ")\"><em> ‹上一页</em></a></li>");
		}

		sb.append("<li class=\"pagination\"><ul>");
		int begin = pageNo - length / 2;
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a></li>");
			} else {
				sb.append("<li><a href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a></li>");
			}
		}
		sb.append("</ul></li>");
		if (pageNo != last) {
			sb.append("<li class=\"last\"><a rel=\"next\" href=\"javascript:page(" + (pageNo + 1) + "," + pageSize + ")\"><em>下一页›</em></a></li>");
			sb.append("<li class=\"last\"><a rel=\"next\" href=\"javascript:page(" + this.last + "," + pageSize + ")\"><em>尾页»</em></a></li>");
		}

		return sb.toString();
	}

	public String getPageStr() {
		StringBuilder sb = new StringBuilder();

		if (pageNo != first) {// 如果不是首页
			sb.append(
					"<span class='fl'><a  href='javascript:void(0)' onclick='pagejump(1)' class='green1 br'>第一页</a></span>" + "<span class='fl'><a  href='javascript:void(0)' onclick='pagejump(" + (pageNo - 1) + ")' class='green1 br'>前一页</a></span>");
		}
		if (pageNo != last) {

			sb.append("<span class='fl'><a href='javascript:void(0)' onclick='pagejump(" + (pageNo + 1) + ")'  class='green1 br'>后一页</a></span>" + "<span class='fl'><a href='javascript:void(0)' onclick='pagejump(" + last
					+ ")' class='green1'>最后页</a></span>");
		}
		return sb.toString();
	}

	public String getMFrontPageStr() {
		StringBuilder sb = new StringBuilder();
		int length = 3;
		if (pageNo != first) {// 如果不是首页
			sb.append("<button class=\"last\"><a rel=\"pre\" href=\"javascript:page(" + 1 + "," + pageSize + ")\"><em>«首页</em></a></button>");
			sb.append("<button class=\"last\"><a rel=\"next\" href=\"javascript:page(" + (pageNo - 1) + "," + pageSize + ")\"><em> ‹上一页</em></a></button>");
		}

		//sb.append("<button class=\"pagination\"><ul>");
		int begin = pageNo - length / 2;
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<button class=\"active\"><a href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a></button>");
			} else {
				sb.append("<button><a href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a></button>");
			}
		}
		//sb.append("</ul></button>");
		if (pageNo != last) {
			sb.append("<button class=\"last\"><a rel=\"next\" href=\"javascript:page(" + (pageNo + 1) + "," + pageSize + ")\"><em>下一页›</em></a></button>");
			sb.append("<button class=\"last\"><a rel=\"next\" href=\"javascript:page(" + this.last + "," + pageSize + ")\"><em>尾页»</em></a></button>");
		}

		return sb.toString();
	}

	//云智分页
	public String getYZFrontPageStr() {
		StringBuilder sb = new StringBuilder();
		//int length = 3;
		if (pageNo != first) {// 如果不是首页
			sb.append("<a rel=\"pre\" href=\"javascript:page(" + 1 + "," + pageSize + ")\"><em>首页</em></a>");
			sb.append("<a rel=\"next\" href=\"javascript:page(" + (pageNo - 1) + "," + pageSize + ")\"><em> < </em></a>");
		}

		// sb.append("<li class=\"pagination\"><ul>");
		int begin = pageNo - length / 2;
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<a class=\"pageon\" href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a>");
			} else {
				sb.append("<a href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a>");
			}
		}
		//sb.append("</ul></li>");
		if (pageNo != last) {
			sb.append("<a rel=\"next\" href=\"javascript:page(" + (pageNo + 1) + "," + pageSize + ")\"><em> > </em></a>");
			sb.append("<a rel=\"next\" href=\"javascript:page(" + this.last + "," + pageSize + ")\"><em>尾页</em></a>");
		}

		return sb.toString();
	}

	//云智手机分页
	public String getMYZFrontPageStr() {
		StringBuilder sb = new StringBuilder();
		int length = 3;
		if (pageNo != first) {// 如果不是首页
			sb.append("<a rel=\"pre\" href=\"javascript:page(" + 1 + "," + pageSize + ")\"><em>首页</em></a>");
			sb.append("<a rel=\"next\" href=\"javascript:page(" + (pageNo - 1) + "," + pageSize + ")\"><em> < </em></a>");
		}

		// sb.append("<li class=\"pagination\"><ul>");
		int begin = pageNo - length / 2;
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<a class=\"pageon\" href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a>");
			} else {
				sb.append("<a href=\"javascript:page(" + i + "," + pageSize + ")\">" + i + "</a>");
			}
		}
		//sb.append("</ul></li>");
		if (pageNo != last) {
			sb.append("<a rel=\"next\" href=\"javascript:page(" + (pageNo + 1) + "," + pageSize + ")\"><em> > </em></a>");
			sb.append("<a rel=\"next\" href=\"javascript:page(" + this.last + "," + pageSize + ")\"><em>尾页</em></a>");
		}

		return sb.toString();
	}

	//只显示上一页下一页

	public String getPNPageStr() {
		StringBuilder sb = new StringBuilder();

		if (pageNo != first && pageNo != last) {// 如果不是首页
			sb.append("<a  href='javascript:void(0)' onclick='pagejump(" + (pageNo - 1) + ")' class='btn_prev'><button><i class='icon_arrow_left'></i>Previous</button></a>" + "<a href='javascript:void(0)' onclick='pagejump(" + (pageNo + 1)
					+ ")'  class='btn_next'><button>Next<i class='icon_arrow_right'></i></button></a>");
		}
		if (pageNo == first && pageNo != last) {// 如果是首页
			sb.append("<a  href='javascript:void(0)' onclick='pagejump(" + 1 + ")' class='btn_prev'><button><i class='icon_arrow_left'></i>Previous</button></a>" + "<a href='javascript:void(0)' onclick='pagejump(" + (pageNo + 1)
					+ ")'  class='btn_next'><button>Next<i class='icon_arrow_right'></i></button></a>");
		}
		if (pageNo == last && pageNo != first) {// 如果是尾页
			sb.append("<a  href='javascript:void(0)' onclick='pagejump(" + (pageNo - 1) + ")' class='btn_prev'><button><i class='icon_arrow_left'></i>Previous</button></a>" + "<a href='javascript:void(0)' onclick='pagejump(" + last
					+ ")'  class='btn_next'><button>Next<i class='icon_arrow_right'></i></button></a>");
		}
		if (pageNo == last && pageNo == first) {// 如果是尾页
			sb.append("<a  href='javascript:void(0)' onclick='pagejump(" + 1 + ")' class='btn_prev'><button><i class='icon_arrow_left'></i>Previous</button></a>" + "<a href='javascript:void(0)' onclick='pagejump(" + last
					+ ")'  class='btn_next'><button>Next<i class='icon_arrow_right'></i></button></a>");
		}
		/*if(pageNo!=last)
		{	
			sb.append("<a  href='javascript:void(0)' onclick='pagejump("+(pageNo-1)+")' class='icon_arrow_left'><button><i class='icon_arrow_left'></i>Previous</button></a>" +
					"<a href='javascript:void(0)' onclick='pagejump("+(pageNo+1)+")'  class='icon_arrow_right'><button>Next<i class='icon_arrow_right'></i></button></a>");            
		}*/
		return sb.toString();
	}

	public String getCauFrontPageStr() {
        int length =3;
		StringBuilder sb = new StringBuilder();

		sb.append("<ul class=\"paging mt_10\">");

		if (pageNo == first) {// 如果是首页
			sb.append("<li><a class=\"next\"  href=\"#\"><em>上一页</em></a></li>");
		} else {
			sb.append("<li><a class=\"next\" href=\"javascript:\"onclick=\"" + funcName + "(" + prev + "," + pageSize + ",'" + funcParam + "');\" ><em>上一页</em></a></li>");

		}

		int begin = pageNo - length / 2;

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}
		sb.append("<li>");
		sb.append("<ul>");

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first) + "</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"sep\"><span>...</span></li>");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
		}
		sb.append("</li>");
		sb.append("</ul>");
		if (pageNo == last) {
			sb.append("<li><a class=\"next\"  href=\"#\"><em>下一页</em></a></li>");
		} else {
			sb.append("<li><a  class=\"next\" href=\"javascript:\" onclick=\"" + funcName + "(" + next + "," + pageSize + ",'" + funcParam+ "');\"><em>下一页</em></a></li>");
		}

		sb.append("</ul>");
		return sb.toString();
	}
	
}
