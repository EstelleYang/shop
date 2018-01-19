package com.o2o.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel导出父类
 * 最大支持 65000/次
 * @author     tangyuhan 349910
 * @version    v1.0
 * 2016-11-17 11:32:54
 */
@SuppressWarnings({ "rawtypes","unchecked"})
public class ExportBase<T> {

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

    protected HttpServletResponse response;

    private final static Logger logger = LoggerFactory.getLogger(ExportBase.class);

    /**
     * 标题行号
     */
    protected int headerNum = 1;

    protected int rowNum = 1;

    /**
     *  Excel导出
     * @param customMapJson JSON串
     * @param fileName      Excel名称
     * @param list          导出集合
     * @param response      HttpServletResponse
     */
	public ExportBase(String customMapJson, String fileName, Collection<T> list, HttpServletResponse response){
        this(JSON.parseObject(customMapJson,LinkedHashMap.class),fileName,list,response);
    }


    /**
     *  Excel导出
     * @param customMap     字段集合
     * @param fileName      Excel名称
     * @param list          导出集合
     * @param response      HttpServletResponse
     */
	@SuppressWarnings("null")
	public ExportBase(Map<String,Object> customMap, String fileName, Collection<T> list, HttpServletResponse response){
        try {
            logger.debug("导出自定字段:{}",JSONObject.toJSONString(customMap));
            logger.debug("导出文件名:{}",fileName);
            if(null == list || list.size() < 1){
                throw new NullPointerException("The list is empty");
            }
            this.list = (List<T>) list;
            this.fieldsAsList(this.list.get(0).getClass());
            this.customMap = customMap;
            this.response = response;
            createWorkbook(fileName);
            getReport();
            export(fileName);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    protected ExportBase(){}

    /**
     * 创建WorkBook
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    protected void createWorkbook(String fileName) throws UnsupportedEncodingException {
        this.wb = new HSSFWorkbook();
        this.sheet = wb.createSheet(fileName);
        this.createColumns();
    }

    /**
     * 创建Header宽度
     */
    protected void createColumns(){
        sheet.setColumnWidth(((short)0), (int)
                ((10 + NumberConstants.DOUBLE_0_72) * NumberConstants.NUMBER_256));
        for (int i = 1; i <= customMap.size(); i++){
            sheet.setColumnWidth(((short)i),
                    (int)((NumberConstants.NUMBER_25 + NumberConstants.DOUBLE_0_72) * NumberConstants.NUMBER_256));
        }
        this.createStyle();
    }

    protected void createStyle(){
        this.centerStyle = createCenterStyle();
        this.boldStyle = ExportExcel.createBoldStyle(wb);
//        boldStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);//前景颜色
        //德邦蓝
        boldStyle.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
        HSSFFont font= wb.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setFontHeightInPoints((short)11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldStyle.setFont(font);
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
	protected void getReport() throws IOException {
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
        ExportExcel.setHeader(response, fileName);
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





}
