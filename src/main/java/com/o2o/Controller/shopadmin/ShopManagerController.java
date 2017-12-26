package com.o2o.Controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.DO.AreaDO;
import com.o2o.DO.PersonInfo;
import com.o2o.DO.Shop;
import com.o2o.DO.ShopCategory;
import com.o2o.DTO.ShopExecution;
import com.o2o.Service.AreaService;
import com.o2o.Service.ShopCategoryService;
import com.o2o.Service.ShopService;
import com.o2o.ShopExceptions.ShopOperationException;
import com.o2o.Util.CodeUtil;
import com.o2o.Util.HttpServletRequestUtil;
import com.o2o.enums.ShopStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagerController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getInitShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        try {
            List<ShopCategory> shopCategoryList = new ArrayList<>();
            List<AreaDO> areaList = new ArrayList<>();
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errorMsg",e.getMessage());
        }
        return modelMap;
    }
    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        // 2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo user = new PersonInfo();
            user.setUserId(1L);
            request.getSession().setAttribute("user",user);
            PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se;
            try {
                se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                    if (shopList==null||shopList.size()==0){
                        shopList = new ArrayList<>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }
    @RequestMapping(value = "getShopInfoById",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId>-1){
            try {
                Shop shop = shopService.getShopByID(shopId);
                List<AreaDO> areaList = areaService.getAreaList();
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errorMsg",e.getMessage());
            }
        }
        else{
            modelMap.put("success",false);
            modelMap.put("errorMsg","empty shopId !");
        }
        return modelMap;
    }
    @RequestMapping(value = "/modifyShopInfo",method = RequestMethod.POST)
    private Map<String,Object> modifyShopInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("error","验证输入有误");
            return modelMap;
        }
        //接收并转化传入的数据
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        //字符串转化为对象类型
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null ;
        try {
            shop = mapper.readValue(shopStr,Shop.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断request传进来的数据类型是否为multipart类型
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
            shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
        }
        //更新店铺
        if (shop!=null&&shop.getShopId()!=null) {
            ShopExecution shopExecution = null;
            try {
                if (shopImg == null) {
                    shopExecution = shopService.modify(shop, null, null);
                } else {
                    shopExecution = shopService.modify(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }
                if (shopExecution.getState()==ShopStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg","店铺更新失败");
                }
            }catch (ShopOperationException se){
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getMessage());
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }
    }
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getshoplist(HttpServletRequest request){
        Map<String,Object> modelmap = new HashMap<>();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        request.getSession().setAttribute("user",user);
        user = (PersonInfo) request.getSession().getAttribute("user");
        List<Shop> shopList = new ArrayList<>();
        try{
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition,1,10);
            modelmap.put("shopList",shopExecution.getShopList());
            modelmap.put("user",user);
            modelmap.put("success",true);
        }catch (Exception e){
            modelmap.put("success",false);
            modelmap.put("errMsg",e.getMessage());
        }
        return modelmap;
    }
    @RequestMapping(value = "/shopmanagementinfo",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId<=0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj==null){
                modelMap.put("redirect",true);
                modelMap.put("url","/O2O/shopadmin/shoplist");
            }else{
                Shop currentShop = (Shop)currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else{
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("success",false);
        }
        return modelMap;
    }

}
