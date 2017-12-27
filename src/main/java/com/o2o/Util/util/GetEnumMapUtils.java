package com.o2o.Util.util;

import com.deppon.cubc.bill.client.constant.BillStatusConstants;
import com.deppon.cubc.bill.client.constant.BillSupplierTypeConstants2;
import com.deppon.cubc.commons.lang.MEnum;
import com.deppon.cubc.trade.order.client.constant.CustomerTypeConstants;
import com.deppon.cubc.trade.order.client.constant.OrderSubTypeConstants;
import com.deppon.cubc.trade.order.client.constant.PickUpTypeConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 将枚举转换为key-value形式
 * @author 401681  yangdechao
 *
 */
public class GetEnumMapUtils {
	/**
	 * 提货方式类型和中文Map
	 * @param type
	 * @author 401681  yangdechao
	 * @return
	 */
	public static Map<String, Object>  getPickUpMap(){
		Map<String, Object>  result = new HashMap<String, Object>();
		Map<String, MEnum> map = PickUpTypeConstants.getEnumMap(PickUpTypeConstants.class);
		if(map!=null){
			Iterator<Entry<String, MEnum>> entries = map.entrySet().iterator();  
			while (entries.hasNext()) {  
			    Entry<String, MEnum> entry = entries.next();  
			    if(entry!=null){
			    	String name = GetEnumNameUtils.getPickUpTypeName(entry.getKey());
			    	 result.put(entry.getKey(), name);
			    } 
			}  
		}
		return result;
	}
	/**
	 * 流水子类型和中文Map
	 * @param type
	 * @author 401681  yangdechao
	 * @return
	 */
	public static Map<String, Object>  getOrderSubMap(){
		Map<String, Object>  result = new HashMap<String, Object>();
		Map<String, MEnum> map = OrderSubTypeConstants.getEnumMap(OrderSubTypeConstants.class);
		if(map!=null){
			Iterator<Entry<String, MEnum>> entries = map.entrySet().iterator();  
			while (entries.hasNext()) {  
			    Entry<String, MEnum> entry = entries.next();  
			    if(entry!=null){
			    	String name = GetEnumNameUtils.getOrderSubTypeName(entry.getKey());
			    	 result.put(entry.getKey(), name);
			    } 
			}  
		}
		return result;
	}
	
	/**
	 * 对账单确认状态和中文Map
	 * @param type
	 * @author 401681  yangdechao
	 * @return
	 */
	public static Map<String, Object>  getConfirmStatusMap(){
		Map<String, Object>  result = new HashMap<String, Object>();
		Map<String, MEnum> map = BillStatusConstants.getEnumMap(BillStatusConstants.class);
		if(map!=null){
			Iterator<Entry<String, MEnum>> entries = map.entrySet().iterator();  
			while (entries.hasNext()) {  
			    Entry<String, MEnum> entry = entries.next();  
			    if(entry!=null){
			    	String name = GetEnumNameUtils.getBillStatusName(entry.getKey());
			    	 result.put(entry.getKey(), name);
			    } 
			}  
		}
		return result;
	}
	/**
	 * 对账单确认状态和中文Map
	 * @param type
	 * @author 401681  yangdechao
	 * @return
	 */
	public static Map<String, Object>  getCustomerTypeMap(){
		Map<String, Object>  result = new HashMap<String, Object>();
		Map<String, MEnum> map = CustomerTypeConstants.getEnumMap(CustomerTypeConstants.class);
		if(map!=null){
			Iterator<Entry<String, MEnum>> entries = map.entrySet().iterator();  
			while (entries.hasNext()) {  
			    Entry<String, MEnum> entry = entries.next();  
			    if(entry!=null){
			    	String name = GetEnumNameUtils.getCustomerTypeName(entry.getKey());
			    	 result.put(entry.getKey(), name);
			    } 
			}  
		}
		return result;
	}
	
	/**
	 * 对账单类型和中文Map
	 * @param type
	 * @author 401681  yangdechao
	 * @return
	 */
	public static Map<String, Object>  getBillSupplierTypeMap(){
		Map<String, Object>  result = new HashMap<String, Object>();
		Map<String, MEnum> map = BillSupplierTypeConstants2.getEnumMap(BillSupplierTypeConstants2.class);
		if(map!=null){
			Iterator<Entry<String, MEnum>> entries = map.entrySet().iterator();  
			while (entries.hasNext()) {  
			    Entry<String, MEnum> entry = entries.next();  
			    if(entry!=null){
			    	String name = GetEnumNameUtils.getSupplierTypeName(entry.getKey());
			    	 result.put(entry.getKey(), name);
			    } 
			}  
		}
		return result;
	}
}
