package com.o2o.Util.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * class Ajax请求响应封装类
 *
 * @author     tangyuhan
 * @version    v1.0
 * 2016-11-02 08:03:40
 */
public class Jsonp {
	/** 参数封装Map */
    private Map<String, Object> jsonp;
    
    /** 响应Code */
    private static final String RESPONSE_CODE = "responseCode";
    /** 返回消息 */
    private static final String MESSAGE = "msg";
    /** 响应数据结果集 */
    private static final String DATA = "data";
    /** 自定义响应结果 */
    private static final String RESULTMAP = "resultMap";

    private Jsonp() {
        jsonp = new HashMap<String, Object>();
    }

    private Jsonp(String code, Collection<?> data) {
        jsonp = new HashMap<String, Object>();
        setResponseCode(code);
        setData(data);
    }

    private Jsonp(String code, String msg) {
        jsonp = new HashMap<String, Object>();
        setResponseCode(code);
        setMsg(msg);
    }

    private Jsonp(String code, Collection<?> data, String msg) {
        jsonp = new HashMap<String, Object>();
        setResponseCode(code);
        setData(data);
        setMsg(msg);
    }

    private Jsonp(String code, Collection<?> data,Map<String,Object> resultMap) {
        jsonp = new HashMap<String, Object>();
        setResponseCode(code);
        setData(data);
        setResultMap(resultMap);
    }

    private Jsonp(String code, Collection<?> data, String msg,Map<String,Object> resultMap) {
        jsonp = new HashMap<String, Object>();
        setResponseCode(code);
        setData(data);
        setMsg(msg);
        setResultMap(resultMap);
    }

    /**
     * Method newInstance
     * @param code
     * @param data
     * @return
     */
    public static Jsonp newInstance(String code, Collection<?> data) {
        return new Jsonp(code, data);
    }

    /**
     * Method newInstance
     * @param code
     * @param msg
     * @return
     */
    public static Jsonp newInstance(String code, String msg) {
        return new Jsonp(code, msg);
    }

    /**
     * Method newInstance
     * @param code
     * @param data
     * @param msg
     * @return
     */
    public static Jsonp newInstance(String code, Collection<?> data, String msg) {
        return new Jsonp(code, data, msg);
    }

    /**
     * Method newInstance
     * @param code
     * @param data
     * @param resultMap
     * @return
     */
    public static Jsonp newInstance(String code, Collection<?> data, Map<String,Object> resultMap) {
        return new Jsonp(code, data,resultMap);
    }

    /**
     * Method newInstance
     * @param code
     * @param data
     * @param msg
     * @param resultMap
     * @return
     */
    public static Jsonp newInstance(String code, Collection<?> data, String msg,Map<String,Object> resultMap) {
        return new Jsonp(code, data, msg,resultMap);
    }

    /**
     * Method getData
     * @return
     */
    public Collection<?> getData() {
        return (Collection<?>) jsonp.get(DATA);
    }

    /**
     * Method setData
     * @param data
     * @return
     */
    public Jsonp setData(Collection<?> data) {
        if (null == data || data.size() < 1) {
            jsonp.put(DATA, null);
        } else {
            jsonp.put(DATA, data);
        }

        return this;
    }

    /**
     * Method getMsg
     * @return
     */
    public String getMsg() {
        return (String) jsonp.get(MESSAGE);
    }

    /**
     * Method setMsg
     * @param msg
     * @return
     */
    public Jsonp setMsg(String msg) {
        if (null == msg) {
            jsonp.put(MESSAGE, "");
        } else {
            jsonp.put(MESSAGE, msg);
        }

        return this;
    }

    /**
     * Method getResponseCode
     * @return
     */
    public String getResponseCode() {
        return (String) jsonp.get(RESPONSE_CODE);
    }

    /**
     * Method setResponseCode
     * @param code
     * @return
     */
    public Jsonp setResponseCode(String code) {
        if (null == code) {
            jsonp.put(RESPONSE_CODE, "");
        } else {
            jsonp.put(RESPONSE_CODE, code);
        }

        return this;
    }

    /**
     * Method setResultMap
     * @param resultMap
     * @return
     */
    public Jsonp setResultMap(Map<String,Object> resultMap){
        if(null == resultMap || resultMap.size()<1){
            jsonp.put(RESULTMAP,null);
        }else{
            jsonp.put(RESULTMAP,resultMap);
        }

        return this;
    }

    /**
     * Method getResultMap
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String,Object> getResultMap(){
        return ((Map<String,Object>)jsonp.get(RESULTMAP));
    }

}


//~ Formatted by Jindent --- http://www.jindent.com
