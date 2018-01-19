package com.o2o.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author: 496934  Date: 2018/1/19 Time: 15:53
 */
public class JSONUtils {

        private static ObjectMapper mapper = new ObjectMapper();
        private static Logger log = Logger.getLogger(JSONUtils.class);
        private static final String PAGE_TOTAL = "total";
        private static final String PAGE_ROWS = "rows";

        /**
         * 将json数据转成指定类型的对象
         * @param content
         * @param valueType
         * @return
         */
        public static <T>T convertJson2Object(String content, Class<T> valueType){
            if(StringUtils.isEmpty(content)) {
                return null;
            }
            return JSON.parseObject(content, valueType);
        }

        /**
         * 将Object对象转成指定类型的对象
         * @param object
         * @param objectType
         * @return
         */
        public static <T>T converEntityToObject(Object object,Class<T> objectType){
            try {
                return object == null ? null : JSON.parseObject(JSON.toJSONString(object),objectType);
            } catch (Exception e) {
                log.error("json解析错误:"+e.getMessage());
            }
            return null;
        }

        /**
         * 将对象格式化成json格式数据
         * @param obj
         * @return
         */
        public static String convertObject2Json(Object obj) {
            return JSON.toJSONString(obj, true);
        }

        /**
         * 将json格式数据转成Map对象
         * @param content
         * @return
         */
        public static Map<String, Object> convertJson2Map(String content){
            try {
                return mapper.readValue(content,  new TypeReference<Map<String,Object>>() { });
            } catch (IOException e) {
                log.error("json解析错误:"+e.getMessage());
            }
            return null;
        }

        /**
         * 将Object格式数据转成JSONObject对象
         * @param object
         * @return
         */
        public static JSONObject converEntityToJSONObject(Object object){
            try {
                return object == null ? new JSONObject() : JSONObject.parseObject(JSON.toJSONString(object));
            } catch (Exception e) {
                log.error("json解析错误:"+e.getMessage());
            }
            return null;
        }

        /**
         * 将后台传过来的数据封装返回前台
         * @param total			总条数
         * @param resultList	分页数据集
         * @return
         */
        public static Map<String, Object> resultMap(Long total,Collection<?> resultList){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(PAGE_TOTAL, total);
            map.put(PAGE_ROWS,  resultList);
            return map;
        }

        /**
         * 将后台传过来的数据封装返回前台
         * @param total			总条数
         * @param resultList	分页数据集
         * @return
         */
        public static Map<String, Object> resultMap(Long total,Collection<?> resultList,Map<String,Object> resultData){
            Map<String, Object> map = new  HashMap<String, Object>();
            map.put(PAGE_TOTAL, total);
            map.put(PAGE_ROWS,  resultList);
            if(!resultData.isEmpty()){
                Iterator iterator = resultData.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry entry = (Map.Entry) iterator.next();
                    map.put(entry.getKey().toString(),entry.getValue());
                }
            }
            return map;
        }
    }

