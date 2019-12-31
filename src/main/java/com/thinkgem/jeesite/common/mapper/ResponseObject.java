package com.thinkgem.jeesite.common.mapper;

import java.util.List;

import com.cloudinte.zhaosheng.modules.common.entity.APIResponseObject;

/**
 * @author 廖水平
 */
public class ResponseObject extends APIResponseObject {

	/** 单条数据 */
	private Object data;

	public Object getData() {
		return data;
	}

	public ResponseObject data(Object data) {
		this.data = data;
		return this;
	}

	public ResponseObject msg(String msg) {
		setMsg(msg);
		return this;
	}

	public ResponseObject list(List<?> list) {
		setList(list);
		return this;
	}

	public ResponseObject state(int state) {
		setState(state);
		return this;
	}

	public ResponseObject total(Number total) {
		setTotal(total.longValue());
		return this;
	}

	public ResponseObject() {
	}

	public ResponseObject(int state, String msg) {
		super(state, msg);
	}

	public ResponseObject(int state, String msg, Object data) {
		this(state, msg);
		this.data = data;
	}

	public ResponseObject(int state, String msg, Long total, List<?> list) {
		super(state, msg, total, list);
	}

	/**
	 * 
	 * @return new ResponseObject(STATE_ERR, MSG_ERR);
	 */
	public static ResponseObject err() {
		return new ResponseObject(STATE_ERR, MSG_ERR);
	}

	/**
	 * 
	 * @return new ResponseObject(STATE_FAILURE, MSG_FAILURE);
	 */
	public static ResponseObject failure() {
		return new ResponseObject(STATE_FAILURE, MSG_FAILURE);
	}

	/**
	 * 
	 * @return new ResponseObject(STATE_SUCCESS, MSG_SUCCESS);
	 */
	public static ResponseObject success() {
		return new ResponseObject(STATE_SUCCESS, MSG_SUCCESS);
	}

	private boolean withJsessionId = false;

	@Override
	public String getJessionid() {
		return withJsessionId ? super.getJessionid() : null;
	}

	/**
	 * 是否输出jessionid。默认为false，不输出
	 * 
	 * @param withJsessionId
	 * @return
	 */
	public ResponseObject withJsessionId(boolean withJsessionId) {
		this.withJsessionId = withJsessionId;
		return this;
	}
}
