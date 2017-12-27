package com.o2o.Util.util;
import com.deppon.cubc.web.common.constant.NumberConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * MD5加密算法工具类
 * @author 401681
 *
 */
public class MD5Util {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);

	 /*** 
     * MD5加密 生成32位md5码
     * @param inStr 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
        	LOGGER.error("{}",e);
            return "";
        }
        StringBuffer hexValue = new StringBuffer();
		try {
			byte[] byteArray = inStr.getBytes("UTF-8");
			byte[] md5Bytes = md5.digest(byteArray);
			
			for (int i = 0; i < md5Bytes.length; i++) {
			    int val = ((int) md5Bytes[i]) & 0xff;
			    if (val < NumberConstants.NUMBER_16) {
			        hexValue.append("0");
			    }
			    hexValue.append(Integer.toHexString(val));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return hexValue.toString();
    }

    public static String convertMD5(String inStr){
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    /**
     * 测试主函数
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        String str = new String("appman");
        LOGGER.info("原始：{}" , str);
        LOGGER.info("MD5后：{}" , md5Encode(str));
    }
}
