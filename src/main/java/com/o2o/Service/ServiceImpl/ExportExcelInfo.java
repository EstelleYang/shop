package com.o2o.Service.ServiceImpl;

import com.o2o.DO.ExcelBean;
import com.o2o.DO.Product;
import com.o2o.Dao.ProductDao;
import com.o2o.Util.ExcelUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

@Service
public class ExportExcelInfo {
    @Autowired
    private ProductDao productDao;
    public XSSFWorkbook exportExcelInfo(Long shopId)throws InvocationTargetException, ClassNotFoundException, IntrospectionException, ParseException, IllegalAccessException {
    List<Product> list = productDao.queryAllProduct(shopId);
    List<ExcelBean> excel = new ArrayList<>();
    Map<Integer,List<ExcelBean>> map = new LinkedHashMap<>();
    XSSFWorkbook xssfWorkbook = null;
    //设置标题栏
        excel.add(new ExcelBean("ID","productId",0));
        excel.add(new ExcelBean("商品名称","productName",0));
        excel.add(new ExcelBean("原价","normalPrice",0));
        excel.add(new ExcelBean("现价","promotionPrice",0));
        excel.add(new ExcelBean("上架时间","createTime",0));
        excel.add(new ExcelBean("更新时间","lastEditTime",0));
        map.put(0,excel);
        String sheetName = "shopId"+shopId;
        xssfWorkbook = ExcelUtil.createExcelFile(Product.class,list,map,sheetName);
        return xssfWorkbook;
    }
}
