package com.o2o.Util.util.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.cubc.web.common.constant.Constant;
import com.deppon.cubc.web.common.constant.NumberConstants;
import com.deppon.cubc.web.common.util.DateUtil;
import com.deppon.cubc.web.common.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 统计字段(BigDecimal、Integer、int、Long)Excel导出
 * 如Excel导出满足不了需求，继承ExportBase重写方法
 *
 * @author     tangyuhan 349910
 * @version    v1.0
 * 2016-11-19 11:31:39
 */
@SuppressWarnings({ "rawtypes","unchecked","unused","null"})
public class ExportStatistics<T> extends ExportBase<T> {

    private final static Logger logger = LoggerFactory.getLogger(ExportStatistics.class);

    public ExportStatistics(String customMapJson, String fileName, Collection<T> list, HttpServletResponse response,
                            String...columnNames) {
        this(JSON.parseObject(customMapJson, LinkedHashMap.class), fileName, list, response, columnNames);
    }

    public ExportStatistics(Map<String, Object> customMap, String fileName, Collection<T> list, HttpServletResponse response,
                            String... columnNames) {
        try {
            logger.debug("导出文件名:{}",fileName);
            if (null == list || list.size() < 1) {
                throw new NullPointerException("The list is empty");
            }
            this.list      = (List<T>) list;
            this.fieldsAsList(this.list.get(0).getClass());
            this.customMap = customMap;
            this.response  = response;
            createWorkbook(fileName);
            statisticsColumn(columnNames);
            export(fileName);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ExportStatistics() {}

    @Override
    protected void createColumns() {
        sheet.setColumnWidth(((short)0), (int)((NumberConstants.NUMBER_10 + NumberConstants.DOUBLE_0_72) * NumberConstants.NUMBER_256));
        for (int i = 1; i <= customMap.size()+1; i++){
            sheet.setColumnWidth(((short)i),
                    (int)((NumberConstants.NUMBER_25 + NumberConstants.DOUBLE_0_72) * NumberConstants.NUMBER_256));
        }
        createStyle();
    }

    /**
     * 校验字段类型
     * @param columnName 统计的字段名称、对象属性名
     *
     * @throws Exception
     */
    protected void statisticsColumn(String... columnName) throws IOException {
        for (String object : columnName) {
            Boolean flag = false;
            for (Field field : list.get(0).getClass().getDeclaredFields()) {
                if (field.getName().equals(object)) {
                    flag = true;
                    String type = field.getGenericType().toString();
                    if (!type.equals("class java.lang.Integer")
                            &&!type.equals("class java.math.BigDecimal")
                            &&!type.equals(Constant.TYPE_CLASS_LONG)
                            &&!type.equals("int")) {
                        throw new ClassCastException(
                                object + " input parameter type does not match,Can only Integer、BigDecimal、Long ");
                    }
                }
            }
            if (!flag) {
                throw new NullPointerException(object + " " + list.get(0).getClass() + " is Non existent");
            }
        }
        this.getReport(columnName);
    }

    /**
     * 带统计字段填充Excel
     * @param columnNames
     *
     * @throws IOException
     */
    protected void getReport(String... columnNames) throws IOException {
        logger.debug("导出Excel总计{}条数据",list.size());
        logger.debug("导出Excel填充数据开始.......");
        Map<String, BigDecimal> statisList = null;
        statisList = new LinkedHashMap<String, BigDecimal>();
        for (String obj : columnNames) {
            statisList.put(obj, new BigDecimal(0));
        }
        for (int i = 0; i < list.size(); i++) {
            int cellType = HSSFCell.CELL_TYPE_STRING;
            HSSFRow row = sheet.createRow(rowNum);
            ExportExcel.createCell(row, 0, centerStyle, cellType, rowNum++);    // 序号
            JSONObject jsonObject = JSONUtils.converEntityToJSONObject(list.get(i));
            int rowsCount  = 1;
            Iterator itor = customMap.entrySet().iterator();
            while (itor.hasNext()) {
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
                        logger.debug("导出自定义字段存在其他实体，则遍历获取值....");
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
                if (statisList.containsKey(key)) {
                    statisList.put(key, statisList.get(key).add(new BigDecimal(value.toString())));
                }
                ExportExcel.createCell(row, rowsCount++, centerStyle, cellType, value);
            }
        }

        // 统计
        int     cellType = HSSFCell.CELL_TYPE_STRING;
        HSSFRow row      = sheet.createRow(rowNum);
        ExportExcel.createCell(row, 0, centerStyle, cellType, "统计");    // 序号
        BigDecimal sum   = new BigDecimal(0);
        int        count = 1;
        Iterator   itro  = customMap.entrySet().iterator();
        while (itro.hasNext()) {
            String key = ((Map.Entry) itro.next()).getKey().toString();
            if (statisList.containsKey(key)) {
                sum = sum.add(statisList.get(key));
                ExportExcel.createCell(row, count, centerStyle, cellType, statisList.get(key));
            }
            count++;
        }
        ExportExcel.createCell(row, getLastCellNum(), centerStyle, cellType, "总计:" + sum);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
