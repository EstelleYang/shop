package com.o2o.Controller.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin",method = RequestMethod.GET)
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation")
    public String getShop(){
        return "/shop/shopoperation";
    }
    @RequestMapping(value = "/shoplist")
    public String getShoplist(){
        return "/shop/shoplist";
    }
    @RequestMapping(value = "/shopmanage")
    public String getShopManagementInfo(){
        return "/shop/shopmanager";
    }
}




