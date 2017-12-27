package com.o2o.Util.util;

import org.apache.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体对象转Map类型工具类
 * @author 401681
 *
 */
public class ObjectMapUtils {
	private static Logger log = Logger.getLogger(ObjectMapUtils.class);
	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map  
	public static Map<String, Object> transBean2Map(Object obj) {  
	    if (obj == null) {  
	        return null;  
	    }  
	    Map<String, Object> map = new HashMap<String, Object>();  
	    try {  
	        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
	        for (PropertyDescriptor property : propertyDescriptors) {  
	        	if(property!=null){
	        		 String key = property.getName();  
	        		 // 过滤class属性  
	 	            if (!"class".equals(key)) {  
	 	                // 得到property对应的getter方法  
	 	                Method getter = property.getReadMethod(); 
	 	                if(getter!=null){
	 	                	Object value = getter.invoke(obj);  
	 	 	                map.put(key, value);  
	 	                }
	 	            }  
	        	}	           
	        }  
	    } catch (Exception e) {  
	    	log.error("transBean2Map Error {}" ,e);  
	    }  
	    return map;  
	  
	}  
	// 将一个map对象转化为bean
    public static void transMap2Bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
