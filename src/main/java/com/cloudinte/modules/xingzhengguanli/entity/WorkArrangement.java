package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 工作安排Entity
 * @author dcl
 * @version 2019-12-06
 */
public class WorkArrangement extends DataEntity<WorkArrangement> {
	
	private static final long serialVersionUID = 1L;
	private String keshi;		// 科室
	private Date startDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String title;		// 标题
	private String importantJob;		// 重点工作
	private String otherJob;		// 常规工作
	
	private String contentTitle;//content_title  内容标题
	private String otherTitle;//other_title  其他内容标题
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
	 *  工作安排
	 */
	public WorkArrangement() {
		super();
	}

	/**
	 *  工作安排
	 * @param id
	 */
	public WorkArrangement(String id){
		super(id);
	}

	@Length(min=0, max=255, message="科室长度必须介于 0 和 255 之间")
	@ExcelField(title="科室", align=2, sort=1 )
	public String getKeshi() {
		return keshi;
	}

	public void setKeshi(String keshi) {
		this.keshi = keshi;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=2 )
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=3 )
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Length(min=0, max=255, message="标题长度必须介于 0 和 255 之间")
	@ExcelField(title="标题", align=2, sort=4 )
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="重点工作", align=2, sort=5 )
	public String getImportantJob() {
		return importantJob;
	}

	public void setImportantJob(String importantJob) {
		this.importantJob = importantJob;
	}
	
	@ExcelField(title="常规工作", align=2, sort=6 )
	public String getOtherJob() {
		return otherJob;
	}

	public void setOtherJob(String otherJob) {
		this.otherJob = otherJob;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getOtherTitle() {
		return otherTitle;
	}

	public void setOtherTitle(String otherTitle) {
		this.otherTitle = otherTitle;
	}
	
}