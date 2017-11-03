package com.o2o.Dao;

import com.o2o.BaseTest;
import com.o2o.DO.AreaDO;
import com.o2o.DO.PersonInfo;
import com.o2o.DO.Shop;
import com.o2o.DO.ShopCategory;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

public class ShopDaoTest extends BaseTest{
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private Shop shop;
    @Autowired
    private ShopCategory shopCategory;
    @Autowired
    private AreaDO areaDO;
    @Autowired
    private PersonInfo owner;
    @Test
    public void queryShopCount(){
        owner.setUserId(1L);
        shop.setOwner(owner);
       int count =  shopDao.queryShopCount(shop);
        System.out.println(count);
    }
    @Test
    public void queryShopList(){
        owner.setUserId(1L);
        shop.setOwner(owner);
       List<Shop> list =  shopDao.queryshopList(shop,0,5);
        for (Shop shop:list){
            System.out.println(shop.getShopName());
        }
    }
    @Test
    public void queryShopInfo(){
        long shopId = 30L;
        shop = shopDao.queryShopInfo(shopId);
        System.out.println(shop.getShopName());
        System.out.println(shop.getArea().getAreaName());
    }
    @Test

    public void insertShopTest(){
        shopCategory.setShopCategoryId(11L);
        owner.setUserId(1L);
        areaDO.setAreaId(2);
        shop.setPhone("17621760869");
        shop.setEnableStatus(1);
        shop.setShopName("HoneyNail");
        shop.setShopAddr("天宝路65号");
        shop.setShopDesc("美甲店");
        shop.setOwner(owner);
        shop.setArea(areaDO);
        shop.setShopCategory(shopCategory);
        int result = shopDao.insertShop(shop);
        assertEquals(1,result);
    }
    @Test

    public void updateShopTest(){
        shop.setShopId(29L);
        areaDO.setAreaId(3);
        shop.setArea(areaDO);
        shop.setShopDesc("性价比高的美甲店a");
        int result = shopDao.updateShop(shop);
        assertEquals(1,result);
    }
}
