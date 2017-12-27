package com.o2o.Util.util.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deppon.cubc.web.common.util.GsonUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;


@SuppressWarnings({ "rawtypes","unchecked"})
public class InvoiceExportBase<T> extends ExportBase<T> {


    private final static Logger logger = LoggerFactory.getLogger(InvoiceExportBase.class);

    /**
     *  Excel导出
     * @param customMapJson JSON串
     * @param fileName      Excel名称
     * @param list          导出集合
     * @param response      HttpServletResponse
     */
	public InvoiceExportBase(String customMapJson, String fileName, Collection<T> list, HttpServletResponse response){
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
	public InvoiceExportBase(Map<String,Object> customMap, String fileName, Collection<T> list, HttpServletResponse response){
        try {
            if(null == list || list.size() < 1){
                throw new NullPointerException("The list is empty");
            }
            super.list = (List<T>) list;
            super.fieldsAsList(this.list.get(0).getClass());
            super.customMap = customMap;
            super.response = response;
            super.createWorkbook(fileName);
            this.getReport();
            super.export(fileName);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(),e);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    protected InvoiceExportBase(){}


    /**
     * 填充Excel
     * @throws IOException
     */
	protected void getReport() throws IOException {
        for(int i = 0;i < list.size();i++){
            int cellType = HSSFCell.CELL_TYPE_STRING;
            HSSFRow row = sheet.createRow(rowNum);
            ExportExcel.createCell(row,0, centerStyle,cellType,rowNum++);//序号
            JSONObject jsonObject = GsonUtil.converEntityToJSONObject(list.get(i));
            int rowsCount = 1;
            Iterator itor = customMap.entrySet().iterator();
            while(itor.hasNext()){
                Map.Entry entry = (Map.Entry) itor.next();
                if(fieldList.contains(entry.getKey().toString())) {
                    String key = entry.getKey().toString();
                    Object value = jsonObject.get(key);
                    if (ObjectUtils.equals(null, value)) {
                    	rowsCount++;
                        continue;
                    }
                    ExportExcel.createCell(row, rowsCount++, centerStyle, cellType, value.toString());
                }
            }
        }
    }

}
