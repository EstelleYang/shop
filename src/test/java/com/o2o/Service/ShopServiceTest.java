package com.o2o.Service;

import com.o2o.BaseTest;
import com.o2o.DO.Shop;
import com.o2o.DTO.ShopExecution;
import com.o2o.ShopExceptions.ShopOperationException;
import com.o2o.Util.PathUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ShopServiceTest extends BaseTest{
    @Autowired
    private ShopService shopService;
    @Test
    public void modifyShopInfo() throws ShopOperationException,FileNotFoundException{
        Shop shop = new Shop();
        shop.setShopId(29L);
        shop.setEnableStatus(0);
        //shop.getEnableStatus()
        shop.setShopName("啊哈哈哈店");
        File shopImg = new File("E:/lufei.jpg");
        InputStream inputStream = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.modify(shop,inputStream,"lufei.jpg");
        System.out.println("新的图片路径："+ PathUtil.getImgBasePath()+shopExecution.getShop().getShopImg());
    }
}
