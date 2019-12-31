package com.thinkgem.jeesite.modules.wechat.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WechatContentMessage {
    
    @XmlElement(name="ToUserName")
    private String toUserName;
    
    @XmlElement(name="FromUserName")
    private String fromUserName;
    
    @XmlElement(name="CreateTime")
    private Long createTime;
    
    @XmlElement(name="MsgType")
    private String msgType;
    
    @XmlElement(name="Content")
    private String content;
    
    @XmlElement(name="MsgId")
    private Integer msgId;

    
    @XmlTransient
    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    @XmlTransient
    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    @XmlTransient
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @XmlTransient
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @XmlTransient
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @XmlTransient
    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }
    
    
}
