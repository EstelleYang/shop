package com.o2o.DO;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class AreaDO {
    private Integer areaId;
    private String areaName;
    private Integer prority;
    private Date lastEditTime;
    private Date createTime;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getPrority() {
        return prority;
    }

    public void setPrority(Integer prority) {
        this.prority = prority;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
