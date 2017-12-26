package com.o2o.Dao;

import com.o2o.BaseTest;
import com.o2o.DO.Product;
import com.o2o.DO.ProductCategory;
import com.o2o.DO.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;
    @Test
    public void deleteTest(){
        List<Product> list = new ArrayList<>();
        Product product = new Product();
        Product product1 = new Product();
       product.setProductId(1L);
       product1.setProductId(4L);
       list.add(product);
       list.add(product1);
       int result = productDao.deleteBanchProduct(list);
       System.out.println(result);
    }
    @Test
    public void insertTest(){
        List<Product> list = new ArrayList<>();
        Product product = new Product();
        Product product1 = new Product();
        ProductCategory productCategory = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1L);
        productCategory.setProductCategoryId(1);
        product.setCreateTime(new Date());
        product.setEnableStatus(0);
        product.setLastEditTime(new Date());
        product.setNormalPrice("100");
        product.setProductCategory(productCategory);
        product.setPriority(0);
        product.setProductDesc("全世界最难喝的奶茶");
        product.setShop(shop);
        product.setProductName("死难喝奶茶");


        product1.setProductId(1L);
        productCategory.setProductCategoryId(1);
        product1.setCreateTime(new Date());
        product1.setEnableStatus(0);
        product1.setLastEditTime(new Date());
        product1.setNormalPrice("100");
        product1.setProductCategory(productCategory);
        product1.setPriority(0);
        product1.setProductDesc("全世界最难吃的肉");
        product1.setShop(shop);
        product1.setProductName("肥肉");
        list.add(product);
        list.add(product1);
       int result = productDao.insertBanchProduct(list);
       System.out.println(result);

    }
    @Test
    public void queryProductTest(){
        List<Product> productList = productDao.queryProductByShopId(0,10,1L);
        for (Product product:productList){
            System.out.println(product.getProductId()+':'+product.getProductName()+':'+product.getCreateTime());
        }
    }
    @Test
    public void queryCount(){
        int count = productDao.queryCount(1L);
        System.out.println(count);
    }
    @Test
    public void queryAllProduct(){
        List<Product> list = productDao.queryAllProduct(1L);
        for (Product product:list){
            System.out.println(product.getProductName());
        }
    }
    @Test
    public void queryProductCategory(){
        List<ProductCategory> list = productDao.queryProductCategory();
        for (ProductCategory productCategory:list){
            System.out.print(productCategory.getProductCategoryName()+"+");
        }
    }
    @Test
    public void queryProductCountByShop(){
        List list  = productDao.queryProductByShop();
        Iterator i = list.iterator();
        while (i.hasNext()){
           System.out.print(i.next().toString()+"+");
        }


    }
}
