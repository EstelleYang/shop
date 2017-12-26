package com.o2o.Controller.productadmin;

import com.alibaba.fastjson.JSONObject;
import com.o2o.DO.*;
import com.o2o.DTO.EchartsData;
import com.o2o.DTO.Series;
import com.o2o.Dao.ProductDao;
import com.o2o.Dao.ShopDao;
import com.o2o.Service.ProductService;
import com.o2o.Service.ShopService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CarTool carTool;
    @Autowired
    private OrderItem orderItem;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ProductDao productDao;

    /*@RequestMapping(value = "/queryProductC")
    @ResponseBody
    public Map<String,Object> queryProductCategory(){
        productService.queryProductCategory();
        Map<String,Object> modelMap = new HashedMap();
        List<ProductCategory> list = productService.queryProductCategory();
        modelMap.put("categoryList",list);
        return modelMap;
    }*/

    /*public Map<String,Object> addToCar(@RequestParam int productId){
        if (null==orderItem){

        }
       // carTool.addProduct(productId,orderItem);

   }*/
    @RequestMapping(value = "/aProduct")
    @ResponseBody
    public Map<String,Object> queryProductById(@RequestParam int productId){
        Product product = productService.queryProductById(productId);
        Map<String,Object> modelMap = new HashedMap();
        modelMap.put("productName",product.getProductName());
        modelMap.put("productDesc",product.getProductDesc());
        modelMap.put("normalPrice",product.getNormalPrice());
        modelMap.put("promotionPrice",product.getPromotionPrice());
        return modelMap;
    }
    @RequestMapping(value = "/getEcharts")
    @ResponseBody
    public EchartsData getCharts() {
        /*Map<String,Object> modelMap = new HashedMap();
        List listShop = new ArrayList();
        List listProductCount = new ArrayList();
        listShop = shopDao.queryAllShop();
        listProductCount = productDao.queryProductByShop();
        *//*for (int i = 0; i < 3; i++) {
            List<Integer> list = new ArrayList<Integer>();
            for (int j = 1; j < 13; j++) {
                list.add((int)(Math.random()*40));
            }*//*
        for(int i = 0;i<listShop.size();i++){
            List<Integer> list =  new ArrayList<Integer>();
            for (int j = 1; j <2; j++) {
                list.add(Integer.parseInt(listProductCount.get(j).toString()));
            }
            Series series = new Series(listShop.get(i).toString(),Series.TYPE_BAR,list);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("name", series.toName());
            jsonObject2.put("type","bar");
            jsonObject2.put("data",series.data);
            listProductCount.add(jsonObject2);
        }


        modelMap.put("listShop",listShop);
        modelMap.put("productCount",listProductCount);
        return modelMap;
    }*/
        List<String> legend = new ArrayList<>(Arrays.asList(new String[]{"店铺名"}));//数据分组
        List<String> category = new ArrayList<>();//横坐标
        List<Shop> list =  shopDao.queryAllShop();
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(4);
        List<Series> series = new ArrayList<>();//纵坐标
        List<Integer> shopList = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            category.add(list.get(i).getShopName());
        }
        List<Integer> countList = productDao.queryProductByShop();

        for (int i=0;i<countList.size();i++){
            shopList.add(countList.get(i).intValue());
        }

        series.add(new Series("商品数量", "bar", shopList));
        series.add(new Series("商品","bar",list1));
        EchartsData data = new EchartsData(legend, category, series);
        return data;
    }
}
