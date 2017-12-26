package com.o2o.Service.ServiceImpl;

import com.o2o.DO.Shop;
import com.o2o.DTO.ShopExecution;
import com.o2o.Dao.ShopDao;
import com.o2o.Service.ShopService;
import com.o2o.ShopExceptions.ShopOperationException;
import com.o2o.Util.ImgUtil;
import com.o2o.Util.PageSizeCalUtil;
import com.o2o.Util.PathUtil;
import com.o2o.enums.ShopStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Override
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String filename) {
        if (shop==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        //给店铺附初始值
        try{
            shop.setEnableStatus(0);
            shop.setLastEditTime(new Date());
            shop.setCreateTime(new Date());
            shop.setAdvice(new ShopExecution(ShopStateEnum.CHECK).getStateInfo());
            int result = shopDao.insertShop(shop);
            if (result<=0){
                throw new ShopOperationException("店铺创建失败");
            }else if (shopImgInputStream!=null){
                try {
                    addShopImg(shop, shopImgInputStream, filename);
                }catch (Exception e){
                    throw new ShopOperationException("addShopImgError："+e.getMessage());
                }
                //更新店铺的图片地址
                result = shopDao.updateShop(shop);
                if (result<=0){
                    throw new ShopOperationException("更新图片地址失败");
                }
            }

        }catch (Exception e){
            throw new ShopOperationException("addshoperror:"+e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    /**
     * 查询店铺信息
     * @param shopId
     * @return
     */
    @Override
    public Shop getShopByID(long shopId) {
        return shopDao.queryShopInfo(shopId);
    }

    /**
     *        更新店铺信息
     * @param shop
     * @param shopImgStream
     * @param filename
     * @return
     */
    @Override
    public ShopExecution modify(Shop shop, InputStream shopImgStream, String filename) throws ShopOperationException{
        if (null==shop||null==shop.getShopId()){
            return new ShopExecution(ShopStateEnum.NULL_SHOPID);
        }else {
            //判断是否处理图片信息,如果更新内容有图片更新，删除之前图片，添加新图片
            try {
                if (shopImgStream != null && filename != null && !"".equals(filename)) {
                    Shop shopitem = shopDao.queryShopInfo(shop.getShopId());
                    if (shopitem.getShopImg() != null) {
                        ImgUtil.deletePathOrfile(shopitem.getShopImg());
                    }
                    addShopImg(shop, shopImgStream, filename);
                }
                shop.setLastEditTime(new Date());
                int effectResult = shopDao.updateShop(shop);
                if (effectResult <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryShopInfo(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            }catch (Exception e){
                throw new ShopOperationException("modifyShopInfo failed"+e.getMessage());
            }
        }
    }

    /**
     * 获取店铺列表信息
     * @param shop
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ShopExecution getShopList(Shop shop, int pageIndex, int pageSize) {
        int rowIndex = PageSizeCalUtil.claPageIndex(pageIndex,pageSize);
        List<Shop> shopList = shopDao.queryshopList(shop,rowIndex,pageSize);
        int count = shopDao.queryShopCount(shop);
        ShopExecution shopExecution = new ShopExecution();
        if (shopList!=null) {
            shopExecution.setCount(count);
            shopExecution.setShopList(shopList);
        }else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }

        return shopExecution;
    }

    @Override
    public ShopExecution getAllShop(int offset, int pageSize) {
        List<Shop> shopList = shopDao.queryshopListNoCondition(offset,pageSize);
        int count = shopDao.shopCount();
        ShopExecution shopExecution = new ShopExecution();
        if (shopList!=null){
            shopExecution.setCount(count);
            shopExecution.setShopList(shopList);
        }else{
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    private void addShopImg(Shop shop, InputStream shopImgInputStream, String filename) {
        //获取图片相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImgUtil.generateThumbnail(shopImgInputStream,filename,dest);
        shop.setShopImg(shopImgAddr);
    }


}
