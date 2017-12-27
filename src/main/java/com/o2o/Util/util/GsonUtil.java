package com.o2o.Util.util;

import com.alibaba.fastjson.JSONObject;
import com.deppon.cubc.web.common.constant.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

public class GsonUtil {

	private static Logger log = Logger.getLogger(GsonUtil.class);


	private static class GsonHolder {
		private static final Gson INSTANCE = new GsonBuilder().setDateFormat(Constant.YYMMDDHMS).create();
	}

	/**
	 * 获取Gson实例，由于Gson是线程安全的，这里共同使用同一个Gson实例
	 */
	public static Gson getInstance() {
		return GsonHolder.INSTANCE;
	}

	/**
	 * 将Object格式数据转成JSONObject对象
	 * @param object
	 * @return
	 */
	public static JSONObject converEntityToJSONObject(Object object){
		try {
			 String json = GsonUtil.getInstance().toJson(object);
			return object == null ? new JSONObject() : JSONObject.parseObject(json);
		} catch (Exception e) {
			log.error("json解析错误:"+e.getMessage());
		}
		return null;
		
	}
}
