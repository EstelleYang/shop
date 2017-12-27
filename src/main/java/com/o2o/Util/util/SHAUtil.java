package com.o2o.Util.util;

import com.deppon.cubc.web.common.constant.NumberConstants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class SHAUtil {

	 /*** 
     * SHA加密 生成40位SHA码
     * @param 待加密字符串
     * @return 返回40位SHA码
     */
    public static String shaEncode(String inStr){
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        StringBuffer hexValue = new StringBuffer();
		try {
			byte[] byteArray = inStr.getBytes("UTF-8");
			byte[] md5Bytes = sha.digest(byteArray);
			
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

    /**
     * 测试主函数
     * @param args
     * @throws Exception
     */
   /* public static void main(String args[]) throws Exception {
        String str = new String("admin");
        System.out.println("原始：" + str);
        System.out.println("SHA后：" + shaEncode(str));
    }*/
}
