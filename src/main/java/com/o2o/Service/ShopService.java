package com.o2o.Service;

import com.o2o.DO.Shop;
import com.o2o.DTO.ShopExecution;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    /**
     * 添加店铺信息
     * @param shop
     * @param shopImg
     * @param filename
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImg,String filename);

    /**
     * 通过店铺id查询店铺信息
     * @param shopId
     * @return Shop
     */
    Shop getShopByID(long shopId);

    /**
     * 更新店铺信息
     * @param shop
     * @param shopImgStream
     * @param filename
     * @return ShopExecution
     */
    ShopExecution modify(Shop shop,InputStream shopImgStream,String filename);

    /**
     * 查询店铺列表
     * 在数据库中分页，limit接收的变量rowIndex是数据库中的行数
     * 在前台分页时，接收的是页数pageIndex,所以在服务层应该将rowIndex与pageIndex做转换
     * @param shop
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopExecution getShopList(Shop shop,int pageIndex,int pageSize);

}
