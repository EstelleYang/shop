package com.o2o.DTO;

import com.o2o.DO.Product;
import com.o2o.enums.ProductStateEnum;

import java.util.List;

public class ProductExecution {
    private int state;
    private String stateInfo;
    private Product product;
    private int count;
    private List<Product> productList;

    public ProductExecution(){}
    //失败的构造器
    public ProductExecution(ProductStateEnum productStateEnum){
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
    }
    //成功的构造器
    public ProductExecution(ProductStateEnum productStateEnum,Product product){
        this.stateInfo = productStateEnum.getStateInfo();
        this.state = productStateEnum.getState();
        this.product = product;
    }
    public ProductExecution(ProductStateEnum productStateEnum,List<Product> productList){
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.productList =productList;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
