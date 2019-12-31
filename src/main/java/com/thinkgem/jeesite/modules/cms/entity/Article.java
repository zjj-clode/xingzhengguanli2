/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 文章Entity
 *
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Article extends DataEntity<Article> {
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_TEMPLATE = "frontViewArticle";

	private Category category;// 分类编号
	private String title; // 标题
	private String subtitle; // 子标题
	private String link; // 外部链接
	private String color; // 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String image; // 文章图片
	private String appImage; // 文章app列表图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer weight; // 权重，越大越靠前
	private Date weightDate;// 权重期限，超过期限，将weight设置为0
	private Integer hits; // 点击数
	private String posid; // 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
	private String customContentView; // 自定义内容视图
	private String viewConfig; // 视图参数
	private String attachment;//附件
	private Date releaseDate;//发布时间
	private String articleType;//文章类型（普通、优势、标杆）针对云智
	private String movieLink;//视频连接
	
	private User user; //创建者?
	private ArticleData articleData; //文章副表
	private List<String> categoryIdList=Lists.newArrayList();
	private List<Category> categoryList=Lists.newArrayList();
	
	private String schoolWord;//school_word校教字
	private String code;//code号
	private String signIssue;//sign_issue签发
	private String themeStatement;//theme_statement主题词
	private String report;//report报
	private String send;//send发
	private String copies;//copiesXX份
	private String section;//发表部门
	private String releasetime;//发布日期

	//
	private Date beginDate; // 开始时间
	private Date endDate; // 结束时间
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

	public Article() {
		super();
		weight = 0;
		hits = 0;
		posid = "";
	}

	public Article(String id) {
		this();
		this.id = id;
	}

	public Article(Category category) {
		this();
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min = 0, max = 255)
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Length(min = 0, max = 50)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min = 0, max = 255)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;//CmsUtils.formatImageSrcToDb(image);
	}

	public String getAppImage() {
		return appImage;
	}

	public void setAppImage(String appImage) {
		this.appImage = appImage;
	}

	@Length(min = 0, max = 255)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min = 0, max = 255)
	public String getDescription() {
		return description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	@Length(min = 0, max = 10)
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public String getCustomContentView() {
		return customContentView;
	}

	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}

	public String getViewConfig() {
		return viewConfig;
	}

	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}

	public ArticleData getArticleData() {
		return articleData;
	}

	public void setArticleData(ArticleData articleData) {
		this.articleData = articleData;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	

	
	public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<String> getCategoryIdList() {
        return categoryIdList;
    }

    public void setCategoryIdList(List<String> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }

    public List<String> getPosidList() {
		List<String> list = Lists.newArrayList();
		if (posid != null) {
			for (String s : StringUtils.split(posid, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setPosidList(List<String> list) {
		posid = "," + StringUtils.join(list, ",") + ",";
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public String getMovieLink() {
		return movieLink;
	}

	public void setMovieLink(String movieLink) {
		this.movieLink = movieLink;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getUrl() {
		return CmsUtils.getUrlDynamic(this);
	}

	public String getImageSrc() {
		return CmsUtils.formatImageSrcToWeb(image);
	}

	public String getAppImageSrc() {
		return CmsUtils.formatImageSrcToWeb(appImage);
	}

	public String getAttachmentUrl() {
		return CmsUtils.formatImageSrcToWeb(attachment);
	}

	/**
	 * 是否有附件
	 */
	public boolean isHasAttachment() {
		return StringUtils.isNotBlank(attachment);
	}

	public String getSchoolWord() {
		return schoolWord;
	}

	public void setSchoolWord(String schoolWord) {
		this.schoolWord = schoolWord;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSignIssue() {
		return signIssue;
	}

	public void setSignIssue(String signIssue) {
		this.signIssue = signIssue;
	}

	public String getThemeStatement() {
		return themeStatement;
	}

	public void setThemeStatement(String themeStatement) {
		this.themeStatement = themeStatement;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getSend() {
		return send;
	}

	public void setSend(String send) {
		this.send = send;
	}

	public String getCopies() {
		return copies;
	}

	public void setCopies(String copies) {
		this.copies = copies;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

}
