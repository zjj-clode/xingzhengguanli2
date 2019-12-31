package com.thinkgem.jeesite.modules.cms.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
/**
 * 图片管理Entity
 * @author G6
 * @version 2019-01-11
 */
public class CmsImage extends DataEntity<CmsImage> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 名称
	private String image;		// 图片
	private String href;		// 链接
	private Integer weight=0;		// 权重，越大越靠前
	private Date weightDate;		// 权重期限
	
	
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
	 *  图片管理
	 */
	public CmsImage() {
		super();
	}

	/**
	 *  图片管理
	 * @param id
	 */
	public CmsImage(String id){
		super(id);
	}

	@Length(min=1, max=255, message="名称长度必须介于 1 和 255 之间")
	@ExcelField(title="名称", align=2, sort=1 )
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=255, message="图片长度必须介于 1 和 255 之间")
	@ExcelField(title="图片", align=2, sort=2 )
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Length(min=0, max=255, message="链接长度必须介于 0 和 255 之间")
	@ExcelField(title="链接", align=2, sort=3 )
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	@ExcelField(title="权重，越大越靠前", align=2, sort=4 )
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="权重期限", align=2, sort=5 )
	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}
	
}