package com.cloudinte.modules.xingzhengguanli.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 三重一大事项Entity
 * @author dcl
 * @version 2019-12-04
 */
public class ImportantBigEvent extends DataEntity<ImportantBigEvent> {
	
	private static final long serialVersionUID = 1L;
	private String keshi;		// 科室
	private Date startDate;		// 开始时间
	private String fundNumber;		// 经费号
	private String itemName;		// 事项名称
	private Double money;		// 金额（元）
	private Date meetingDate;		// 会议时间
	private String meeingPlace;		// 会议地点
	private String meeingHost;		// 主持人
	private String meeingMember;		// 出席人员
	private String meeingNoteTaker;		// 记录人
	private String meeingTheme;		// 会议主题
	private String meeingContent;		// 主要内容
	
	private String eventdate;
	
	private Date meetingStartDate;		// 会议时间
	
	private Date meetingEndDate;		// 会议时间
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
	 *  三重一大事项
	 */
	public ImportantBigEvent() {
		super();
	}

	/**
	 *  三重一大事项
	 * @param id
	 */
	public ImportantBigEvent(String id){
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@ExcelField(title="日期", align=2, sort=2 )
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Length(min=0, max=255, message="经费号长度必须介于 0 和 255 之间")
	@ExcelField(title="经费号", align=2, sort=3 )
	public String getFundNumber() {
		return fundNumber;
	}

	public void setFundNumber(String fundNumber) {
		this.fundNumber = fundNumber;
	}
	
	@Length(min=0, max=255, message="事项名称长度必须介于 0 和 255 之间")
	@ExcelField(title="事项名称", align=2, sort=4 )
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="金额（元）", align=2, sort=5 )
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@ExcelField(title="会议时间", align=2, sort=6 )
	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}
	
	@Length(min=0, max=255, message="会议地点长度必须介于 0 和 255 之间")
	public String getMeeingPlace() {
		return meeingPlace;
	}

	public void setMeeingPlace(String meeingPlace) {
		this.meeingPlace = meeingPlace;
	}
	
	@Length(min=0, max=255, message="主持人长度必须介于 0 和 255 之间")
	//@ExcelField(title="主持人", align=2, sort=8 )
	public String getMeeingHost() {
		return meeingHost;
	}

	public void setMeeingHost(String meeingHost) {
		this.meeingHost = meeingHost;
	}
	
	@Length(min=0, max=255, message="出席人员长度必须介于 0 和 255 之间")
	//@ExcelField(title="出席人员", align=2, sort=9 )
	public String getMeeingMember() {
		return meeingMember;
	}

	public void setMeeingMember(String meeingMember) {
		this.meeingMember = meeingMember;
	}
	
	@Length(min=0, max=255, message="记录人长度必须介于 0 和 255 之间")
	//@ExcelField(title="记录人", align=2, sort=10 )
	public String getMeeingNoteTaker() {
		return meeingNoteTaker;
	}

	public void setMeeingNoteTaker(String meeingNoteTaker) {
		this.meeingNoteTaker = meeingNoteTaker;
	}
	
	@Length(min=0, max=255, message="会议主题长度必须介于 0 和 255 之间")
	//@ExcelField(title="会议主题", align=2, sort=11 )
	public String getMeeingTheme() {
		return meeingTheme;
	}

	public void setMeeingTheme(String meeingTheme) {
		this.meeingTheme = meeingTheme;
	}
	
	@Length(min=0, max=500, message="主要内容长度必须介于 0 和 500 之间")
	//@ExcelField(title="主要内容", align=2, sort=12 )
	public String getMeeingContent() {
		return meeingContent;
	}

	public void setMeeingContent(String meeingContent) {
		this.meeingContent = meeingContent;
	}

	public String getEventdate() {
		return eventdate;
	}

	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}

	public Date getMeetingStartDate() {
		return meetingStartDate;
	}

	public void setMeetingStartDate(Date meetingStartDate) {
		this.meetingStartDate = meetingStartDate;
	}

	public Date getMeetingEndDate() {
		return meetingEndDate;
	}

	public void setMeetingEndDate(Date meetingEndDate) {
		this.meetingEndDate = meetingEndDate;
	}
	
}