package com.o2o.DTO;

import java.util.ArrayList;
import java.util.List;

public class EchartsData {
    public List<String> legend = new ArrayList<>();//数据分组
    public List<String> category = new ArrayList<>();//横坐标
    public List<Series> series = new ArrayList<>();//纵坐标
    public EchartsData(List<String> legendList,List<String>categoryList,List<Series> seriesList){
        super();
        this.legend = legendList;
        this.series = seriesList;
        this.category = categoryList;
    }
}