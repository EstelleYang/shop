package com.o2o.DTO;

import com.o2o.DO.Shop;
import com.o2o.enums.ShopStateEnum;

import java.util.List;

public class ShopExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;
    //店铺数量
    private int count;
    //操作店铺（增删改时用到）
    private Shop shop;
    //店铺列表
    private List<Shop>shopList;
    public ShopExecution(){

    }
    //店铺创建失败时的构造器
    public ShopExecution(ShopStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }
    //店铺创建成功时的构造器
    public ShopExecution(ShopStateEnum stateEnum,Shop shop){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }
    //店铺操作成功时的构造器
    public ShopExecution(ShopStateEnum stateEnum,List<Shop>shopList){
        this.stateInfo = stateEnum.getStateInfo();
        this.state = stateEnum.getState();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
