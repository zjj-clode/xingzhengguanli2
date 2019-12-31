package com.thinkgem.jeesite.modules.cms.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
/**
 * 轮播图Entity
 * @author gf
 * @version 2018-01-24
 */
public class CmsLinkLbt extends DataEntity<CmsLinkLbt> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 图片标题
	private String image;		// 图片
	private String href;		// 链接地址
	private Integer weight=0;		// 权重，越大越靠前
	private Date weightDate;		// 权重期限
	
	
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
	
	
	public CmsLinkLbt() {
		super();
	}

	public CmsLinkLbt(String id){
		super(id);
	}

	@Length(min=1, max=255, message="图片标题长度必须介于 1 和 255 之间")
	@ExcelField(title="图片标题", align=2, sort=1 ,dictType="")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="图片长度必须介于 0 和 255 之间")
	@ExcelField(title="图片", align=2, sort=2 ,dictType="")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Length(min=0, max=255, message="链接地址长度必须介于 0 和 255 之间")
	@ExcelField(title="链接地址", align=2, sort=3 ,dictType="")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	@ExcelField(title="权重，越大越靠前", align=2, sort=4 ,dictType="")
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="权重期限", align=2, sort=5 ,dictType="")
	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}
}