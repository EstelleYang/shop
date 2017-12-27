package com.o2o.Util.util.excel;

import com.alibaba.fastjson.JSONObject;
import com.deppon.cubc.web.common.constant.Constant;
import com.deppon.cubc.web.common.util.DateUtil;
import com.deppon.cubc.web.common.util.JSONUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

public class ExportUtil<T> {
	
	private final static Logger logger = LoggerFactory.getLogger(ExportUtil.class);
	
	/**
	 * 创建WorkBook
	 * @author 349354
	 * @createTime 2017年2月7日 下午2:56:25
	 * @param fileName
	 */
	public HSSFWorkbook createWorkbook(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        ExportExcel.setHeader(response, fileName);
        return new HSSFWorkbook();
    }
	
	public void createSheet(HSSFWorkbook wb, Map<String,Object> customMap, String sheetname, List<T> list, HttpServletResponse response) throws IOException {
		//保存字段
		List<String> fieldList = new ArrayList<String>();
		Map<String,Class<?>> fieldMap = new HashMap<String, Class<?>>();
		
		//样式
		HSSFCellStyle centerStyle = createCenterStyle(wb);
		HSSFCellStyle boldStyle = createBoldStyle(wb);
		
		 /**
	     * 标题行号
	     */
		int headerNum = 1;
		int rowNum = 1;
		
		try {
            if(null == list || list.size() < 1){
                throw new NullPointerException("The list is empty");
            }
            
            //将泛型类中的字段封装到对象中
            this.fieldsAsList(fieldList, fieldMap, list.get(0).getClass());
            
            //创建sheet
            HSSFSheet sheet = wb.createSheet(sheetname);
            //创建列头
            this.createColumns(wb, sheet, customMap);
            this.createHead(sheet, customMap, boldStyle, headerNum);
            
            //填充Excel
            this.getReport(sheet, customMap, fieldList, fieldMap, list, rowNum, centerStyle);
            
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
	}
	
	/**
     * 导出
     * @throws IOException
     */
	public void export(HSSFWorkbook wb, HttpServletResponse response) throws IOException {
        ExportExcel.release(response, wb);
        ExportExcel.close(wb);
    }
	
	/**
	 * 将泛型类中的字段封装到对象中
	 * @author 349354
	 * @createTime 2017年2月7日 下午3:55:09
	 * @param cls
	 */
	protected void fieldsAsList(List<String> fieldList, Map<String,Class<?>> fieldMap, Class cls){
		
        Field[] fields = cls.getDeclaredFields();
        for(Field field:fields){
            if(StringUtils.isNotBlank(field.getName())){
                fieldList.add(field.getName());
                fieldMap.put(field.getName(),field.getType());
            }
        }
        Field[] superFields = cls.getSuperclass().getDeclaredFields();
        if(superFields.length > 0){
            fieldsAsList(fieldList, fieldMap, cls.getSuperclass());
        }
    }
	
	/**
     * 填充Excel
     * @throws IOException
     */
	protected void getReport(HSSFSheet sheet, Map<String,Object> customMap, List<String> fieldList,
                             Map<String,Class<?>> fieldMap, List<T> list, int rowNum, HSSFCellStyle centerStyle) throws Exception {
        for(int i = 0;i < list.size();i++){
            int cellType = HSSFCell.CELL_TYPE_STRING;
            HSSFRow row = sheet.createRow(rowNum);
            ExportExcel.createCell(row,0, centerStyle,cellType,rowNum++);//序号
            JSONObject jsonObject = JSONUtils.converEntityToJSONObject(list.get(i));
            int rowsCount = 1;
            Iterator itor = customMap.entrySet().iterator();
            while(itor.hasNext()){
                Map.Entry entry = (Map.Entry) itor.next();
                if(fieldList.contains(entry.getKey().toString())) {
                    String key = entry.getKey().toString();
                    Object value = jsonObject.get(key);
                    if (ObjectUtils.equals(null, value)) {
                        ExportExcel.createCell(row, rowsCount++, centerStyle, cellType, "");
                        continue;
                    }
                    if(fieldMap.get(key) == Date.class && value.getClass().getName().equals(Constant.TYPE_LONG)){
                        value = DateUtil.formatDate(new Date((Long)value), Constant.YYMMDDHMS);
                    }
                    ExportExcel.createCell(row, rowsCount++, centerStyle, cellType, value);
                }
            }
        }
    }
	
	/**
     * 创建Header宽度
     */
    protected void createColumns(HSSFWorkbook wb, HSSFSheet sheet, Map<String,Object> customMap){
        sheet.setColumnWidth(((short)0), (int)((NumberUtils.int_10 + NumberUtils.double_72) * NumberUtils.int_256));
        for (int i = 1; i <= customMap.size(); i++){
            sheet.setColumnWidth(((short)i), (int)((NumberUtils.int_25 + NumberUtils.double_72) * NumberUtils.int_256));
        }
    }

    /**
     * 创建标题列
     */
    protected void createHead(HSSFSheet sheet, Map<String,Object> customMap, HSSFCellStyle boldStyle, int headerNum){
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
     * 创建中间样式
     * @return
     */
    protected HSSFCellStyle createCenterStyle(HSSFWorkbook wb){
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
     * 创建粗体样式
     * @return
     */
    protected HSSFCellStyle createBoldStyle(HSSFWorkbook wb){
        HSSFCellStyle boldStyle = ExportExcel.createBoldStyle(wb);
        boldStyle.setBorderTop(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderLeft(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderBottom(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setBorderRight(HSSFCellStyle.SOLID_FOREGROUND);
        boldStyle.setTopBorderColor(HSSFColor.BLACK.index);
        boldStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        boldStyle.setRightBorderColor(HSSFColor.BLACK.index);
        boldStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        boldStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        return boldStyle;
    }

    /**
     * 创建左边样式
     * @return
     */
    protected HSSFCellStyle createLeftStyle(HSSFWorkbook wb){
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
