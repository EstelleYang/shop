package com.o2o.Util.util;
import java.util.UUID;

/**
 * UUID
 * @author 406937 刘凡
 * @createTime 2017年3月3日 下午1:39:47
 * @param
 */
public class UUIDUtils{
	/**
	 *  
	 * 获取UUID
	 * @author 406937 刘凡
	 * @createTime 2017年3月3日 下午1:39:57
	 * @param
	 * @return
	 */
	public static String getUUID()
	{
		//返回UUID
		return UUID.randomUUID().toString().replace("-", "");
	}
}