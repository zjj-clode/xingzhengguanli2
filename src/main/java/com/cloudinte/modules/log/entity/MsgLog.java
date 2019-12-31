/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cloudinte.modules.log.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 消息日志
 * 
 * @author 廖水平
 * @version 1.0 2016年6月12日 上午9:56:20
 */
public class MsgLog extends DataEntity<MsgLog> {
	private static final long serialVersionUID = 8675850463501514983L;

	/** 标题 */
	private String title;
	/** 内容 */
	private String content;
	/** 发送方式 app/weixin/email/sms */
	private String pushType;
	/** 接收者 */
	private User toUser;
	/** 接受账号，微信方式的openid、APP方式的clientid、电子邮件方式的email地址、短信方式的手机号码 */
	private String toNum;
	/** 发送时间 */
	protected Date createDate;
	/** 是否成功 */
	private String success;
	/** 失败原因 */
	protected String remarks;

	//消息推送方式 
	public static final String PUSH_TYPE_APP = "app";
	public static final String PUSH_TYPE_WEIXIN = "weixin";
	public static final String PUSH_TYPE_EMAIL = "email";
	public static final String PUSH_TYPE_SMS = "sms";
	//
	public static final String MSG_TITLE_BAOXIUCHENGGONG = "报修成功消息";
	public static final String MSG_TITLE_SHOULIBAOXIU = "受理报修消息";
	public static final String MSG_TITLE_SHOULIJIEGUO = "受理结果消息";
	public static final String MSG_TITLE_CHULIWEIXIU = "处理维修消息";
	public static final String MSG_TITLE_WANGONGJIEGUO = "完工结果消息";
	public static final String MSG_TITLE_PINGJIAJIEGUO = "评价结果消息";
	public static final String MSG_TITLE_HUIFANGJIEGUO = "回访结果消息";

	private String[] ids;

	//查询参数
	private Date beginDate; // 开始日期
	private Date endDate; // 结束日期

	public MsgLog() {
		super();
	}

	public MsgLog(String id) {
		super(id);
	}

	public MsgLog(String title, String content, String pushType, User toUser, String toNum, Date createDate,
			String success, String remarks) {
		super();
		this.title = title;
		this.content = content;
		this.pushType = pushType;
		this.toUser = toUser;
		this.toNum = toNum;
		this.createDate = createDate;
		this.success = success;
		this.remarks = remarks;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public String getToNum() {
		return toNum;
	}

	public void setToNum(String toNum) {
		this.toNum = toNum;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
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

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}