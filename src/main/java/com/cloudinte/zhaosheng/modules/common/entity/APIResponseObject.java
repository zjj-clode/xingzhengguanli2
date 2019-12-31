package com.cloudinte.zhaosheng.modules.common.entity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author 廖水平
 */
public class APIResponseObject {

	/**
	 * 出现错误
	 */
	public static final int STATE_ERR = -1;
	/**
	 * 操作失败
	 */
	public static final int STATE_FAILURE = 0;
	/**
	 * 操作成功
	 */
	public static final int STATE_SUCCESS = 1;
	/**
	 * 请登录
	 */
	public static final int STATE_REQUIRES_USER = 2;
	/**
	 * 无权限
	 */
	public static final int STATE_REQUIRES_PERMISSION = 3;

	/**
	 * 出现错误
	 */
	public static final String MSG_ERR = "出现错误";
	/**
	 * 操作成功
	 */
	public static final String MSG_SUCCESS = "操作成功";
	/**
	 * 操作失败
	 */
	public static final String MSG_FAILURE = "操作失败";
	/**
	 * 请登录
	 */
	public static final String MSG_REQUIRES_USER = "请重新登录";
	/**
	 * 无权限
	 */
	public static final String MSG_REQUIRES_PERMISSION = "您无权操作";

	/** 状态 */
	private int state;

	/** 消息 */
	private String msg;

	/** 单条数据 */
	private Object objectarr;

	/** 数据条数 */
	private Long total;

	@SuppressWarnings("rawtypes")
	/** 数据集合 */
	private List list;

	public APIResponseObject() {}

	public APIResponseObject(int state, String msg) {
		super();
		this.state = state;
		this.msg = msg;
	}

	public APIResponseObject(int state, String msg, Long total, List<?> list) {
		super();
		this.state = state;
		this.msg = msg;
		this.total = total;
		this.list = list;
	}

	public APIResponseObject(int state, String msg, Object objectarr) {
		super();
		this.state = state;
		this.msg = msg;
		this.setObjectarr(objectarr);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}


	public String getJessionid() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (null != request) {
			return request.getSession().getId();
		}
		return "";
	}

	public Object getObjectarr() {
		return objectarr;
	}

	public void setObjectarr(Object objectarr) {
		this.objectarr = objectarr;
	}

}
