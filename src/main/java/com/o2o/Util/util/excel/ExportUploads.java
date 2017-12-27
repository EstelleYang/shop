package com.o2o.Util.util.excel;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author <a href="mailto:sanbian@pamirs.top">Sanbian</a>
 * @version 1.0
 * @since 17/2/17 下午2:19
 */
@SuppressWarnings({ "rawtypes","unchecked","unused","null"})
public class ExportUploads<T> extends ExportBase<T> {

    private final static Logger logger = LoggerFactory.getLogger(ExportUpload.class);

    public ExportUploads(String fileName, List<String> sheetNames, List<String> customMapJsonList, List<Collection<T>> collectionList) {
        try {
            logger.debug("导出文件名:{}",fileName);
            logger.debug("sheetNames:{}",sheetNames);
            logger.debug("customMapJsonList:{}",customMapJsonList);
            if(null == sheetNames || sheetNames.size() < 1){
                throw new NullPointerException("The sheetNames is empty");
            }
            if(null == customMapJsonList || customMapJsonList.size() < 1){
                throw new NullPointerException("The customMapJsonList is empty");
            }
            if(null == collectionList || collectionList.size() < 1){
                throw new NullPointerException("The list is empty");
            }
            if (sheetNames.size() != customMapJsonList.size() || sheetNames.size() != collectionList.size()){
                throw new NullPointerException("sheet的个数不一致");
            }

            this.wb = new HSSFWorkbook();
            for (int i = 0; i < sheetNames.size(); i++) {

                LinkedHashMap linkedHashMap = JSON.parseObject(customMapJsonList.get(i), LinkedHashMap.class);

                this.list = (List<T>) collectionList.get(i);
                this.fieldsAsList(this.list.get(0).getClass());
                this.customMap = linkedHashMap;

                String sheetName = sheetNames.get(i);

                //创建sheet
                this.sheet = wb.createSheet(sheetName);
                //创建列头
                this.createColumns();
                //填充Excel
                this.getReport();

                this.rowNum = 1;
                this.headerNum = 1;
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ExportUploads() {}

    public InputStream getInputStream(){
        InputStream excelStream = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            super.wb.write(out);
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
