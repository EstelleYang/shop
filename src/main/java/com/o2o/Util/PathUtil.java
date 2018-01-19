package com.o2o.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public String timeChange(int i){
        List<String> timeList=new ArrayList<String>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -i);
        date = calendar.getTime();
        String changeDate=sdf.format(date);
        return changeDate;
    }
}
