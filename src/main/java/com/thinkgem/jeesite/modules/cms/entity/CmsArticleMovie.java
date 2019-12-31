package com.thinkgem.jeesite.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 文章视频Entity
 * @author gff
 * @version 2018-05-18
 */
public class CmsArticleMovie extends DataEntity<CmsArticleMovie> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 文章标题
	private String link;		// 文章链接
	
	
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
	
	
	public CmsArticleMovie() {
		super();
	}

	public CmsArticleMovie(String id){
		super(id);
	}

	@Length(min=0, max=255, message="文章标题长度必须介于 0 和 255 之间")
	@ExcelField(title="文章标题", align=2, sort=1 ,dictType="")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="文章链接长度必须介于 0 和 255 之间")
	@ExcelField(title="文章链接", align=2, sort=2 ,dictType="")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}