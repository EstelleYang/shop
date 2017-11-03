package com.o2o.Dao;

import com.o2o.BaseTest;
import com.o2o.DO.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Test
    public void queryShopCategoryTest(){
        List<ShopCategory> list = shopCategoryDao.queryShopCategory(new ShopCategory());
        Iterator<ShopCategory> iterator = list.iterator();
        while (iterator.hasNext()){
           String name =  iterator.next().getShopCategoryName();
           System.out.println(name);
        }

    }

}
