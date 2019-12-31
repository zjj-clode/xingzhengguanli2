package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 附件管理Entity
 * @author dcl
 * @version 2019-12-12
 */
public class FinanceAccessories extends DataEntity<FinanceAccessories> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String files;		// 附件
	private String isShow;		// 是否显示
	private Integer downloadTimes;		// 下载次数
	
	
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
	 *  附件管理
	 */
	public FinanceAccessories() {
		super();
	}

	/**
	 *  附件管理
	 * @param id
	 */
	public FinanceAccessories(String id){
		super(id);
	}

	@Length(min=1, max=255, message="标题长度必须介于 1 和 255 之间")
	@ExcelField(title="标题", align=2, sort=1 )
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=500, message="附件长度必须介于 1 和 500 之间")
	@ExcelField(title="附件", align=2, sort=2 )
	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}
	
	@Length(min=0, max=1, message="是否显示长度必须介于 0 和 1 之间")
	@ExcelField(title="是否显示", align=2, sort=3 ,dictType="yes_no")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	@ExcelField(title="下载次数", align=2, sort=4 )
	public Integer getDownloadTimes() {
		return downloadTimes;
	}

	public void setDownloadTimes(Integer downloadTimes) {
		this.downloadTimes = downloadTimes;
	}
	
}