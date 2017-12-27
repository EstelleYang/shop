package com.o2o.Util.util.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * class description
 *
 * @author tangyuhan 349910
 * @version v1.0
 *          Date:2017/1/16 16:49 下午
 */
@SuppressWarnings({ "unchecked","unused","null"})
public class ExportUpload<T> extends ExportBase<T> {

    private final static Logger logger = LoggerFactory.getLogger(ExportUpload.class);

    public ExportUpload(String customMapJson, String fileName, Collection<T> list) {
        this(JSON.parseObject(customMapJson, LinkedHashMap.class), fileName, list);
    }

    public ExportUpload(Map<String, Object> customMap, String fileName, Collection<T> list) {
        try {
            logger.debug("导出自定字段:{}", JSONObject.toJSONString(customMap));
            logger.debug("导出文件名:{}",fileName);
            if(null == list || list.size() < 1){
                throw new NullPointerException("The list is empty");
            }
            this.list = (List<T>) list;
            this.fieldsAsList(this.list.get(0).getClass());
            this.customMap = customMap;
            createWorkbook(fileName);
            getReport();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ExportUpload() {}

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
