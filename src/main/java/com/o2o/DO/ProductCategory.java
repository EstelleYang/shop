package com.o2o.DO;

import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class ProductCategory {
    private long productCategoryId;
    private String productCategoryName;
    private Integer priority;
    private Date createTime;
    private Shop shop;

    public long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
