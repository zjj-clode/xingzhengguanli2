/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.modules.log.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 业务日志
 * 
 * @author 廖水平
 * @version 1.0 2016年6月12日 上午9:56:20
 */
public class BusinessLog extends DataEntity<BusinessLog> {

	private static final long serialVersionUID = 8675850463501514983L;

	/**
	 * 业务日志类型-登录系统
	 */
	public static final String BUSINESS_LOG_TYPE_GY_LOGIN = "1";//登录
	/**
	 * 业务日志类型-修改密码
	 */
	public static final String BUSINESS_LOG_TYPE_UPDATEPWD = "5";//修改密码
	/**
	 * 业务日志类型-修改
	 */
	public static final String BUSINESS_LOG_TYPE_UPDATE = "6";//修改
	/**
	 * 业务日志类型-新增
	 */
	public static final String BUSINESS_LOG_TYPE_ADD = "2";
	/**
	 * 业务日志类型-刪除
	 */
	public static final String BUSINESS_LOG_TYPE_DEL = "3";
	/**
	 * 业务日志类型-审核
	 */
	public static final String BUSINESS_LOG_TYPE_CHECK = "4";
	
	/**
	 * 业务日志类型-其他
	 */
	public static final String BUSINESS_LOG_TYPE_OTHER = "9";//其他业务类型

	private String title; // 日志标题
	private String type; // 业务类型  ，字典： business_log_type
	private String preChangeData;//变更前数据
	private String postChangeData;//变更后数据

	private String ip;//操作者IP

	//查询参数
	private String[] ids; //批量操作id
	private Date beginDate; // 开始日期
	private Date endDate; // 结束日期

	/**
	 * 操作者
	 */
	@Override
	public User getCreateBy() {
		return super.getCreateBy();
	}

	/**
	 * 操作时间
	 */
	@Override
	public Date getCreateDate() {
		return super.getCreateDate();
	}

	public String getIp() {
		return ip;
	}

	/**
	 * 操作者IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	public BusinessLog() {
		super();
	}

	public BusinessLog(String id) {
		super(id);
	}

	/**
	 * 业务操作日志
	 * 
	 * @param title
	 *            日志标题
	 * @param type
	 *            业务类型 ，如：{@link #BUSINESS_LOG_TYPE_GY_CHANGEBED}
	 * @param preChangeData
	 *            变更前数据
	 * @param postChangeData
	 *            变更后数据
	 * @param remarks
	 *            备注信息
	 */
	public BusinessLog(String title, String type, String preChangeData, String postChangeData, String remarks) {
		super();
		this.title = title;
		this.type = type;
		this.preChangeData = preChangeData;
		this.postChangeData = postChangeData;
		this.remarks = remarks;
		ip = Servlets.getRequest() != null ? Servlets.getRequest().getLocalAddr() : null;
	}

	/**
	 * 使用 {@link #BusinessLog(String, String, String, String, String)}
	 * 
	 * @param title
	 *            日志标题
	 * @param type
	 *            业务类型 ，如：{@link #BUSINESS_LOG_TYPE_GY_CHANGEBED}
	 * @param preChangeData
	 *            变更前数据
	 * @param postChangeData
	 *            变更后数据
	 */
	@Deprecated
	public BusinessLog(String title, String type, String preChangeData, String postChangeData) {
		this(title, type, preChangeData, postChangeData, null);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPreChangeData() {
		return preChangeData;
	}

	public void setPreChangeData(String preChangeData) {
		this.preChangeData = preChangeData;
	}

	public String getPostChangeData() {
		return postChangeData;
	}

	public void setPostChangeData(String postChangeData) {
		this.postChangeData = postChangeData;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}