package com.thinkgem.jeesite.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 问题咨询和文章索引管理Entity
 * @author gff
 * @version 2018-03-19
 */
public class QaAndArticleIndex extends DataEntity<QaAndArticleIndex> {
	
	private static final long serialVersionUID = 1L;
	private String indexId;       //索引Id
	private String link;		// 链接
	private String title;		// 标题
	private String content;		// 内容
	private String askpname;		// askpname
	private String luCreateDate;		// 时间（发布时间）
	private String type;		// 索引类型
	private String searchFrom;		// 索引来源
	private String beginLuCreateDate;		// 开始 时间（发布时间）
	private String endLuCreateDate;		// 结束 时间（发布时间）
	
	
	//
	String[] ids;

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
	
	
	public QaAndArticleIndex() {
		super();
	}

	public QaAndArticleIndex(String id){
		super(id);
	}

	@Length(min=0, max=64, message="链接长度必须介于 0 和 64 之间")
	@ExcelField(title="链接", align=2, sort=1 ,dictType="")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Length(min=0, max=64, message="标题长度必须介于 0 和 64 之间")
	@ExcelField(title="标题", align=2, sort=2 ,dictType="")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="内容长度必须介于 0 和 255 之间")
	@ExcelField(title="内容", align=2, sort=3 ,dictType="")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="askpname长度必须介于 0 和 255 之间")
	@ExcelField(title="askpname", align=2, sort=4 ,dictType="")
	public String getAskpname() {
		return askpname;
	}

	public void setAskpname(String askpname) {
		this.askpname = askpname;
	}
	
	@Length(min=0, max=32, message="时间（发布时间）长度必须介于 0 和 32 之间")
	@ExcelField(title="时间（发布时间）", align=2, sort=5 ,dictType="")
	public String getLuCreateDate() {
		return luCreateDate;
	}

	public void setLuCreateDate(String luCreateDate) {
		this.luCreateDate = luCreateDate;
	}
	
	@Length(min=0, max=32, message="索引类型长度必须介于 0 和 32 之间")
	@ExcelField(title="索引类型", align=2, sort=6 ,dictType="")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=255, message="索引来源长度必须介于 0 和 255 之间")
	@ExcelField(title="索引来源", align=2, sort=7 ,dictType="")
	public String getSearchFrom() {
		return searchFrom;
	}

	public void setSearchFrom(String searchFrom) {
		this.searchFrom = searchFrom;
	}
	
	public String getBeginLuCreateDate() {
		return beginLuCreateDate;
	}

	public void setBeginLuCreateDate(String beginLuCreateDate) {
		this.beginLuCreateDate = beginLuCreateDate;
	}
	
	public String getEndLuCreateDate() {
		return endLuCreateDate;
	}

	public void setEndLuCreateDate(String endLuCreateDate) {
		this.endLuCreateDate = endLuCreateDate;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	
}