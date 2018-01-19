package com.o2o.Controller;

import com.o2o.DO.PersonInfo;
import com.o2o.DO.Product;
import com.o2o.DO.Shop;
import com.o2o.DTO.ProductExecution;
import com.o2o.DTO.ShopExecution;
import com.o2o.Dao.ProductDao;
import com.o2o.Service.ProductService;
import com.o2o.Service.ServiceImpl.ExportExcelInfo;
import com.o2o.Service.ShopService;
import com.o2o.Util.DateUtil;
import com.o2o.Util.ExcelUtil;
import com.o2o.Util.ExportBase;
import com.o2o.Util.ExportExecle;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping(value = "/shoplists")
public class ShopListController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ExportExcelInfo exportExcelInfo;
    @RequestMapping(value = "/allShop")
    @ResponseBody
    public Map<String,Object> getAllShop(@RequestParam int pageSize, @RequestParam int offset){
        Map<String,Object> modelmap = new HashMap<>();
        List<Shop> shopList = new ArrayList<>();
        try{
            ShopExecution shopExecution = shopService.getAllShop(offset,pageSize);
            modelmap.put("rows",shopExecution.getShopList());
            modelmap.put("total",shopExecution.getCount());
        }catch (Exception e){
            modelmap.put("success",false);
            modelmap.put("errMsg",e.getMessage());
        }
        return modelmap;
    }
    @RequestMapping(value = "/allProduct")
    @ResponseBody
    public Map<String,Object> getAllProductByShopID(@RequestParam int pageSize,@RequestParam int offset,@RequestParam int data){
        Map<String,Object> modelmap = new HashMap<>();
        Shop shop = new Shop();
        shop.setShopId((long)data);
        try {
            ProductExecution productExecution = productService.queryProductByShopId(offset,pageSize,shop);
            modelmap.put("rows",productExecution.getProductList());
            modelmap.put("total",productExecution.getCount());
        }catch (Exception e){
            modelmap.put("success",false);
            modelmap.put("errMsg",e.getMessage());
        }
        return modelmap;
    }
    @RequestMapping(value = "allProductByshopName")
    @ResponseBody
    public Map<String,Object> getAllProductByShop(@RequestParam int pageSize,@RequestParam int offset,@RequestParam String data){
        Map<String,Object> modelmap = new HashMap<>();
        Shop shop = new Shop();
       shop.setShopName(data);
        try {
            ProductExecution productExecution = productService.queryProductByShopId(offset,pageSize,shop);
            modelmap.put("rows",productExecution.getProductList());
            modelmap.put("total",productExecution.getCount());
        }catch (Exception e){
            modelmap.put("success",false);
            modelmap.put("errMsg",e.getMessage());
        }
        return modelmap;
    }

    @RequestMapping(value = "/export")
    public void exportData(String queryShopInfoId, String custColumns,
                           HttpServletResponse response, HttpServletRequest request, HttpSession session){
        List<Product> exportList = new ArrayList<>();
        long shopId = Long.parseLong(queryShopInfoId);
        List<Product> list = productService.queryAllProduct(shopId);
        exportList.addAll(list);
        exportExcel(custColumns,response,exportList);
    }

    public void exportExcel(String custColumns,HttpServletResponse response,List<Product>exportList){
        String fileName = DateUtil.formatDate(new Date(),"yyyyMMddHHmmss");
        ExportBase<Product> exportBase = new ExportBase<>(custColumns,fileName,exportList,response);
    }

}

