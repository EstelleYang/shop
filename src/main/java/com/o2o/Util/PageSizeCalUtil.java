package com.o2o.Util;

public class PageSizeCalUtil {
    public static int claPageIndex(int pageIndex,int pageSize){
        return (pageIndex>0)?(pageIndex-1)*pageSize:0;
    }
}
