package com.o2o.Util.util;

import com.deppon.cubc.commons.lang.MEnum;
import com.deppon.cubc.trade.order.client.constant.CustomerTypeConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 通过类型获取枚举名称
 * @author 401681  yangdechao
 *
 */
public class GetEnumTypeListUtils {
	/**
	 * 客户类型集合
	 * @param type
	 * @return
	 */
	public static List<String>  getCustomerTypeList(){
		List<String> list = new ArrayList<String>();
		Map<String, MEnum> map = CustomerTypeConstants.getEnumMap(CustomerTypeConstants.class);
		if(map!=null){
			Iterator<Entry<String, MEnum>> entries = map.entrySet().iterator();  
			while (entries.hasNext()) {  
			    Entry<String, MEnum> entry = entries.next();  
			    if(entry!=null){
			    	 list.add(entry.getKey());
			    } 
			}  
		}
		return list;
	}
	
}
