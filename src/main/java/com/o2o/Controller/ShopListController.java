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
import com.o2o.Util.ExcelUtil;
import com.o2o.Util.ExportExecle;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public void export(HttpServletResponse response,@RequestParam long shopId) throws ClassNotFoundException, IntrospectionException, IllegalAccessException, ParseException, InvocationTargetException, UnsupportedEncodingException {
        String filename = "shopId"+shopId;
        if (shopId>0){
            response.reset();
            Map<String,Object> map = new HashMap<>();
            File file = new File(filename+".xlsx");
          /*  response.setHeader("Content-Type","text/plain");*/
            response.setHeader("Content-Disposition","attachment;filename=" + new String(filename.getBytes(),"utf-8"));
           /* response.addHeader("Content-Length","" + file.length());*/

            XSSFWorkbook workbook = null;
            //导出Excel对象
            workbook = exportExcelInfo.exportExcelInfo(shopId);
            OutputStream outputStream;
            try{
                outputStream = response.getOutputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
              //  bufferedOutputStream.flush();
                workbook.write(bufferedOutputStream);

                bufferedOutputStream.close();
            }catch (IOException e){

            }
        }
    }





    @RequestMapping(value = "/exportExecle")
    public void ExportExecle(HttpServletResponse response,@RequestParam long shopId) throws IOException {
        List<Product> productList = new ArrayList<>();
//        String s = data;
        productList = productDao.queryAllProduct(shopId);
        String fileName = "ExportExecle.xls";
        fileName = new String(fileName.getBytes("GBK"),"iso8859-1");
        OutputStream outputStream = response.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        //定义单元格报头
        String worksheetTitle = "导出信息";
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建单元格样式
        HSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short)200);
        cellStyleTitle.setFont(font);
        // 工作表名
        String id = "id";
        String productName = "productName";
        String normalPrice = "normalPrice";
        String lastPrice = "lastPrice";
        String createTime = "createTime";
        String lastTime = "lastTime";

        HSSFSheet sheet = workbook.createSheet();
        ExportExecle exportExcel = new ExportExecle(workbook, sheet);
        // 创建报表头部
        exportExcel.createNormalHead(worksheetTitle, 6);
        // 定义第一行
        HSSFRow row1 = sheet.createRow(1);
        HSSFCell cell1 = row1.createCell(0);

        //第一行第一列

        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new HSSFRichTextString(id));
        //第一行第er列
        cell1 = row1.createCell(1);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new HSSFRichTextString(productName));

        //第一行第san列
        cell1 = row1.createCell(2);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new HSSFRichTextString(normalPrice));

        //第一行第si列
        cell1 = row1.createCell(3);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new HSSFRichTextString(lastPrice));

        //第一行第wu列
        cell1 = row1.createCell(4);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new HSSFRichTextString(createTime));

        //第一行第liu列
        cell1 = row1.createCell(5);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new HSSFRichTextString(lastTime));

        //第一行第qi列
        cell1 = row1.createCell(6);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new HSSFRichTextString(lastTime));


        //定义第二行
        HSSFRow row = sheet.createRow(2);
        HSSFCell cell = row.createCell(1);
        Product product = new Product();
        for (int i = 0; i < productList.size(); i++) {
            product = productList.get(i);
            row = sheet.createRow(i + 2);

            cell = row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(new HSSFRichTextString(product.getProductId() + ""));

            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(new HSSFRichTextString(product.getProductName()));

            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(new HSSFRichTextString(product.getNormalPrice() + ""));

            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(new HSSFRichTextString(product.getPromotionPrice() + ""));

            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(product.getCreateTime())));

            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(product.getLastEditTime())));

            cell = row.createCell(6);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(product.getLastEditTime())));
            cell.setCellStyle(cellStyle);

        }
        try {
            bufferedOutputStream.flush();
            workbook.write(bufferedOutputStream);

            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output   is   closed ");
        } finally {
            productList.clear();
        }
    }
}

