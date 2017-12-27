package com.o2o.Util.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.deppon.cubc.bill.client.constant.BillStatusConstants;
import com.deppon.cubc.bill.client.constant.BillSupplierTypeConstants2;
import com.deppon.cubc.commons.lang.MEnum;
import com.deppon.cubc.trade.order.client.constant.CustomerTypeConstants;
import com.deppon.cubc.trade.order.client.constant.OrderSubTypeConstants;
import com.deppon.cubc.trade.order.client.constant.PickUpTypeConstants;

/**
 * 通过类型获取枚举名称
 * @author 401681  yangdechao
 *
 */
public class GetEnumNameUtils {
	/**
	 * 提货方式
	 * @param type
	 * @return
	 */
	public static String getPickUpTypeName(String type) {
		String result = "";
		if (!StringUtils.isEmpty(type)) {
			MEnum<?> menum = PickUpTypeConstants.getEnum(PickUpTypeConstants.class, type);
			if(menum!=null){
				result = menum.getCname();
			}
		}
		return result;
	}
	/**
	 * 流水子类型
	 * @param type
	 * @return
	 */
	public static String getOrderSubTypeName(String type){
		String result = "";
		if (!StringUtils.isEmpty(type)) {
			MEnum<?> menum = OrderSubTypeConstants.getEnum(OrderSubTypeConstants.class, type);
			if(menum!=null){
				result = menum.getCname();
			}
		}
		return result;
	}
	/**
	 * 供应商类型
	 * @param type
	 * @return
	 */
	public static String getSupplierTypeName(String type){
		String result = "";
		if (!StringUtils.isEmpty(type)) {
			MEnum<?> menum = OrderSubTypeConstants.getEnum(BillSupplierTypeConstants2.class, type);
			if(menum!=null){
				result = menum.getCname();
			}
		}
		return result;
	}
	/**
	 * 对账单确认状态
	 * @param type
	 * @return
	 */
	public static String getBillStatusName(String type){
		String result = "";
		if (!StringUtils.isEmpty(type)) {
			MEnum<?> menum = BillStatusConstants.getEnum(BillStatusConstants.class, type);
			if(menum!=null){
				result = menum.getCname();
			}
		}
		return result;
	}
	/**
	 * 客户类型
	 * @param type
	 * @return
	 */
	public static String getCustomerTypeName(String type){
		String result = "";
		if (!StringUtils.isEmpty(type)) {
			MEnum<?> menum =  CustomerTypeConstants.getEnum(CustomerTypeConstants.class, type);
			if(menum!=null){
				result = menum.getCname();
			}
		}
		return result;
	}
	
	/**
	 * 供应商对账单类型
	 * @param type
	 * @return
	 */
	public static String getBillSupplierTypeName(String type){
		String result = "";
		if (!StringUtils.isEmpty(type)) {
			MEnum<?> menum =  BillSupplierTypeConstants2.getEnum(BillSupplierTypeConstants2.class, type);
			if(menum!=null){
				result = menum.getCname();
			}
		}
		return result;
	}
}
