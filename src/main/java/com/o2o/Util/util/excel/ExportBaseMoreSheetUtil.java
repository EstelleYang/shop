package com.o2o.Util.util.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.cubc.bill.client.utils.TemplateException;
import com.deppon.cubc.trade.search.client.model.trade.TradeOrderEsDO;
import com.deppon.cubc.web.common.constant.Constant;
import com.deppon.cubc.web.common.util.DateUtil;
import com.deppon.cubc.web.common.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 *<pre>
 *功能:适用于多个sheet的导出
 *作者：405859
 *日期：2017年2月19日下午3:21:57
 *</pre>
 */
@SuppressWarnings({ "rawtypes","unchecked"})
public class ExportBaseMoreSheetUtil<T> {

    /**
     * 自定义列集合
     * key:     属性名
     * value    header列名
     */
    protected Map<String,Object> customMap;

    //导出集合
    protected List<T> list;

    //类属性集合
    protected List<String> fieldList = new ArrayList<String>();

    /**
     * key:属性名
     * value:属性类型
     * 类属性集合
     */
    protected Map<String,Class<?>> fieldMap = new HashMap<String,Class<?>>();

    protected HSSFWorkbook wb;

    protected HSSFSheet sheet;

    protected HSSFCellStyle centerStyle;

    protected HSSFCellStyle leftStyle;

    protected HSSFCellStyle boldStyle;
    
    protected HttpServletRequest request;

    protected HttpServletResponse response;

    private final static Logger logger = LoggerFactory.getLogger(ExportBaseMoreSheetUtil.class);

    /**
     * 标题行号
     */
    protected int headerNum = 1;

    protected int rowNum = 1;
    /**
     * Excel列宽设置常量
     */
    private static final int ROW_ONE = 0;
    /**
     * Excel列宽设置常量
     */
    private static final int SIZE_SMALL = 10;
    /**
     * Excel列宽设置常量
     */
    private static final int SIZE_BIG = 25;
    /**
     * Excel列宽设置常量
     */
    private static final float SIZE_ADD = 0.72f;
    /**
     * Excel列宽设置常量
     */
    private static final int LENGTH= 256;

    /**
     *  Excel导出
     * @param customMapJson JSON串
     * @param fileName      Excel名称
     * @param list          导出集合
     * @param response      HttpServletResponse
     */
    public ExportBaseMoreSheetUtil(String fileName,List <String> customMapJsons, List <String> sheetNames, List <Collection<T>> lists, HttpServletResponse response,HttpServletRequest request){
        try {
            //创建EXCEL
            this.wb = new HSSFWorkbook();
            this.request = request;
            this.response = response;
            //填充EXCEL
            createWorkbook(customMapJsons,sheetNames,lists);
            //关闭流
            export(fileName);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    protected ExportBaseMoreSheetUtil(){}

    /**
     * 创建WorkBook
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    protected void createWorkbook(List <String> customMapJsons, List <String> sheetNames, List <Collection<T>> lists) throws UnsupportedEncodingException {
        try {
            for (int i = 0; i < lists.size(); i++) {
                //创建sheet
                this.sheet = wb.createSheet(sheetNames.get(i));
                //创建sheet表头
                LinkedHashMap customMap = JSON.parseObject(customMapJsons.get(i), LinkedHashMap.class);
                this.customMap = customMap;
                this.createColumns();
                //填充表头对应的数据
                this.list = (List<T>) lists.get(i);
                this.fieldsAsList(this.list.get(0).getClass());
                //导出
                getReport();
                //避免下一个sheet 导出 行数和列数错误
                this.rowNum = 1;
                this.headerNum = 1;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 创建Header宽度
     */
    protected void createColumns(){
    	//第一列为序号 所以宽度设置的小一些
        // sheet.setColumnWidth(((short)ROW_ONE), (int)((SIZE_SMALL + SIZE_ADD) * LENGTH));
        BigDecimal smallSize = BigDecimal.valueOf(SIZE_SMALL).add(BigDecimal.valueOf(SIZE_ADD)).multiply(BigDecimal.valueOf(LENGTH));
//        sheet.setColumnWidth(((short)ROW_ONE), (int)((SIZE_SMALL + SIZE_ADD) * LENGTH));
        sheet.setColumnWidth(((short)ROW_ONE), smallSize.intValue());
        //其它列宽度设置的相对大一些
        for (int i = 1; i <= customMap.size(); i++){
            BigDecimal bigSize = BigDecimal.valueOf(SIZE_BIG).add(BigDecimal.valueOf(SIZE_ADD)).multiply(BigDecimal.valueOf(LENGTH));
//            sheet.setColumnWidth(((short)i), (int)((SIZE_BIG + SIZE_ADD) * LENGTH));
            sheet.setColumnWidth(((short)i), bigSize.intValue());
        }
        //填充样式
        this.createStyle();
    }

    protected void createStyle(){
        this.centerStyle = createCenterStyle();
        this.boldStyle = ExportExcel.createBoldStyle(wb);
        boldStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);//前景颜色
        boldStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
        boldStyle.setBorderTop(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderLeft(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderBottom(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderRight(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setTopBorderColor(HSSFColor.BLACK.index);
        boldStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        boldStyle.setRightBorderColor(HSSFColor.BLACK.index);
        boldStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        boldStyle.setWrapText(true);
        this.createHead();
    }

    /**
     * 创建标题列
     */
    protected void createHead(){
        HSSFRow headRow = sheet.createRow(0);
        int cellType = HSSFCell.CELL_TYPE_STRING;
        Iterator iter = customMap.entrySet().iterator();
        ExportExcel.createCell(headRow,0, boldStyle,cellType,"序号");
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            Object value = entry.getValue();
            ExportExcel.createCell(headRow,headerNum++, boldStyle,cellType,value.toString());
        }
    }

    /**
     * 填充Excel
     * @throws IOException
     */
    protected void getReport() throws TemplateException {
        logger.debug("导出Excel总计{}条数据",list.size());
        logger.debug("导出Excel填充数据开始.......");
        for(int i = 0;i < list.size();i++){
            int cellType = HSSFCell.CELL_TYPE_STRING;
            HSSFRow row = sheet.createRow(rowNum);
            ExportExcel.createCell(row,0, centerStyle,cellType,rowNum++);//序号
            JSONObject jsonObject = JSONUtils.converEntityToJSONObject(list.get(i));
            int rowsCount = 1;
            Iterator itor = customMap.entrySet().iterator();
            while(itor.hasNext()){
                Map.Entry entry = (Map.Entry) itor.next();
                String key = entry.getKey().toString();
                if(!fieldList.contains(key) && !key.contains(".")) {
                    ExportExcel.createCell(row, rowsCount++, centerStyle, cellType, "");
                    continue;
                }
                //实体类中如果存在其它实体，则循环调用取值
                Object value = null;
                if (null != jsonObject) {
                    if (key.contains(".")) {
                        String[] keys = StringUtils.split(key, ".");
                        JSONObject jsonAttr = JSONUtils.converEntityToJSONObject(jsonObject.get(keys[0]));
                        for (int k = 1; k < keys.length - 1; k++) {
                            if (null != jsonAttr) {
                                jsonAttr = JSONUtils.converEntityToJSONObject(jsonObject.get(keys[k]));
                            }
                        }
                        if (null != jsonAttr) {
                            value = jsonAttr.get(keys[keys.length - 1]);
                        }
                    } else {
                        value = jsonObject.get(key);
                    }
                }
                if (null == value) {
                    ExportExcel.createCell(row, rowsCount++, centerStyle, cellType, "");
                    continue;
                }
                if(fieldMap.get(key) == Date.class && value.getClass().getName().equals(Constant.TYPE_LONG)){
                    value = DateUtil.formatDate(new Date((Long)value),Constant.YYMMDDHMS);
                }
                ExportExcel.createCell(row, rowsCount++, centerStyle, cellType, value);
            }
        }
    }

    /**
     * 导出
     * @throws IOException
     */
    protected void export(String fileName) throws IOException {
        ExportExcel.setBaseMoreHeader(response,request,fileName);
        ExportExcel.release(response, wb);
        ExportExcel.close(wb);
    }

    /**
     * 获取行对象
     * @param rownum
     * @return
     */
    protected Row getRow(int rownum){
        return this.sheet.getRow(rownum);
    }

    /**
     * 获取最后一个列号
     * @return
     */
    public int getLastCellNum(){
        return this.getRow(headerNum).getLastCellNum();
    }

    protected void fieldsAsList(Class cls){
        Field[] fields = cls.getDeclaredFields();
        for(Field field:fields){
            if(StringUtils.isNotBlank(field.getName())){
                if(!field.getType().toString().startsWith("class java") &&
                        field.getType().toString().startsWith("class com")){
                    String className = field.getType().toString();
                    try {
                        int index = className.indexOf(" ");
                        className = className.substring(index+1);
                        Class classs = Class.forName(className);
                        for(Field fieldAttr:classs.getDeclaredFields()){
                            fieldList.add(fieldAttr.getName());
                            fieldMap.put(fieldAttr.getName(),field.getType());
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        logger.error("ClassNotFoundException 没有找到类 {}",className);
                    }
                }
                fieldList.add(field.getName());
                fieldMap.put(field.getName(),field.getType());
            }
        }
        Field[] superFields = cls.getSuperclass().getDeclaredFields();
        if(superFields.length > 0){
            fieldsAsList(cls.getSuperclass());
        }
    }

    /**
     * 创建中间样式
     * @return
     */
    protected HSSFCellStyle createCenterStyle(){
        HSSFCellStyle centerStyle = ExportExcel.createTextStyle(wb);
        centerStyle.setBorderTop(HSSFCellStyle.SOLID_FOREGROUND);
        centerStyle.setBorderLeft(HSSFCellStyle.SOLID_FOREGROUND);
        centerStyle.setBorderBottom(HSSFCellStyle.SOLID_FOREGROUND);
        centerStyle.setBorderRight(HSSFCellStyle.SOLID_FOREGROUND);
        centerStyle.setTopBorderColor(HSSFColor.BLACK.index);
        centerStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        centerStyle.setRightBorderColor(HSSFColor.BLACK.index);
        centerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        return centerStyle;
    }

    /**
     * 创建左边样式
     * @return
     */
    protected HSSFCellStyle createLeftStyle(){
        HSSFCellStyle leftStyle = ExportExcel.createTextStyle(wb);
        leftStyle.setBorderTop(HSSFCellStyle.SOLID_FOREGROUND);
        leftStyle.setBorderLeft(HSSFCellStyle.SOLID_FOREGROUND);
        leftStyle.setBorderBottom(HSSFCellStyle.SOLID_FOREGROUND);
        leftStyle.setBorderRight(HSSFCellStyle.SOLID_FOREGROUND);
        leftStyle.setTopBorderColor(HSSFColor.BLACK.index);
        leftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        leftStyle.setRightBorderColor(HSSFColor.BLACK.index);
        leftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        leftStyle.setWrapText(true);
        return leftStyle;
    }


    public static void main(String[] args) {
        Class cls = new TradeOrderEsDO().getClass();
        Field[] fields = cls.getDeclaredFields();
        for(Field field:fields){
            if(StringUtils.isNotBlank(field.getName())){
                System.out.println(field.getType());
                if(!field.getType().toString().startsWith("class java") &&
                        field.getType().toString().startsWith("class com")){
                    try {
                        System.out.println("进来了");
                        String className = field.getType().toString();
                        int index = className.indexOf(" ");
                        className = className.substring(index+1);
                        System.out.println(className);
                        Class classs = Class.forName(className);
                        System.out.println(JSONObject.toJSONString(classs.getDeclaredFields()));
                    } catch (ClassNotFoundException e) {
                        System.out.println("错误");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public InputStream getInputStream(){
        InputStream excelStream = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            this.wb.write(out);
            excelStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
//            try {
//                wb.close();
//            } catch (IOException e) {
//                logger.error(ExceptionUtils.getStackTrace(e));
//            }
        }
        return excelStream;
    }

}
