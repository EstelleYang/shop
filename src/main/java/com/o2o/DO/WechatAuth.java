package com.o2o.DO;

import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class WechatAuth {
    private long wechatAuthId;
    private PersonInfo personInfo;
    private String openId;
    private Date createTime;

    public long getWechatAuthId() {
        return wechatAuthId;
    }

    public void setWechatAuthId(long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
