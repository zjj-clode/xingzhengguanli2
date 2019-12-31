package com.thinkgem.jeesite.modules.sys.security;

/**
 * 自定义CasToken，继承自{@link org.apache.shiro.cas.CasToken}
 * ，增加p属性，用于接收.p参数（可以是整个原始请求url的base编码）
 * 
 * @author 廖水平
 */
public class CasToken extends org.apache.shiro.cas.CasToken {

	private static final long serialVersionUID = 2870947720525596795L;

	/**
	 * 请求参数。<br/>
	 * cas服务器认证后的回调url可以携带除ticket参数外的另一个参数。<br/>
	 */
	private String p;

	public CasToken(String ticket) {
		super(ticket);
	}

	public CasToken(String ticket, String p) {
		super(ticket);
		this.p = p;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

}
