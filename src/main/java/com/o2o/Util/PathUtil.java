package com.o2o.Util;

/**
 * 1 .根据执行环境的不同，提供不同的根路径。图片存放路径
 */
public class PathUtil {
    private static String seperator = System.getProperty("file.separator");
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "E:/projectImg/image/";
        }else {
            basePath = "/home/projectImg/image";
        }
        basePath = basePath.replace("/",seperator);
        return basePath;
    }
    /**
     * 将图片存储到各自店铺的路径下
     */
    public static String getShopImagePath(long shopId){
        String imgPath = "upload/item/shop/"+shopId+"/";
        return imgPath.replace("/",seperator);
    }
}
