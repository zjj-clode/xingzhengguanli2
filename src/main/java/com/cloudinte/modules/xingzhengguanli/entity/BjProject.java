package com.cloudinte.modules.xingzhengguanli.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 北京市共建项目Entity
 * @author dcl
 * @version 2019-12-12
 */
public class BjProject extends DataEntity<BjProject> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 申报人
	private String firstFiles;		// 最初申报版
	private String lastFiles;		// 修改后最终版
	private Date uploadDate;		// 上传时间
	private Date beginUploadDate;		// 开始 上传时间
	private Date endUploadDate;		// 结束 上传时间
	
	
	//
	private String[] ids;

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
	
	
	/**
	 *  北京市共建项目
	 */
	public BjProject() {
		super();
	}

	/**
	 *  北京市共建项目
	 * @param id
	 */
	public BjProject(String id){
		super(id);
	}

	@NotNull(message="申报人不能为空")
	@ExcelField(title="申报人", align=2, sort=1 )
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=500, message="最初申报版长度必须介于 0 和 500 之间")
	@ExcelField(title="最初申报版", align=2, sort=2 )
	public String getFirstFiles() {
		return firstFiles;
	}

	public void setFirstFiles(String firstFiles) {
		this.firstFiles = firstFiles;
	}
	
	@Length(min=0, max=500, message="修改后最终版长度必须介于 0 和 500 之间")
	@ExcelField(title="修改后最终版", align=2, sort=3 )
	public String getLastFiles() {
		return lastFiles;
	}

	public void setLastFiles(String lastFiles) {
		this.lastFiles = lastFiles;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="上传时间", align=2, sort=4 )
	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	public Date getBeginUploadDate() {
		return beginUploadDate;
	}

	public void setBeginUploadDate(Date beginUploadDate) {
		this.beginUploadDate = beginUploadDate;
	}
	
	public Date getEndUploadDate() {
		return endUploadDate;
	}

	public void setEndUploadDate(Date endUploadDate) {
		this.endUploadDate = endUploadDate;
	}
		
}