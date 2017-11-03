package com.o2o.Controller.superadmin;

import com.o2o.DO.AreaDO;
import com.o2o.Service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/superadmin")
public class AreaController {
    Logger logger = LoggerFactory.getLogger(AreaController.class);
    @Autowired
    private AreaService areaService;
    @RequestMapping(value = "/listarea",method= RequestMethod.GET)
    @ResponseBody
    private Map<String,Object>listArea(){
        logger.info("====start=====");
        long startTime = System.currentTimeMillis();
        //存放方法返回值
        Map<String,Object>modelMap = new HashMap<>();
        List<AreaDO>list = new ArrayList<>();
        try {
            list = areaService.getAreaList();
            modelMap.put("rows",list);
            modelMap.put("total",list.size());
        }catch (Exception e){
            e.printStackTrace();
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
        }
        logger.error("test error");
        long endTime = System.currentTimeMillis();
        //程序调优
        logger.debug("costTime:[{}ms]",endTime-startTime);
        logger.info("======end======");
    return modelMap;
    }
}
