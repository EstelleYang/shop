package com.o2o.Service.ServiceImpl;

import com.o2o.DO.Product;
import com.o2o.DO.ProductCategory;
import com.o2o.DO.Shop;
import com.o2o.DTO.ProductExecution;
import com.o2o.DTO.ShopExecution;
import com.o2o.Dao.ProductDao;
import com.o2o.Service.ProductService;
import com.o2o.enums.ProductStateEnum;
import com.o2o.enums.ShopStateEnum;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductDao productDao;
    @Override
    public ShopExecution insertProduct(List<Product> list) {
        ShopExecution shopExecution = new ShopExecution();
        int state = productDao.insertBanchProduct(list);
        if (state>0) {
            return new ShopExecution(ShopStateEnum.SUCCESS);
        }else {
            return  new ShopExecution(ShopStateEnum.INNER_ERROR);
        }
    }

    @Override
    public ShopExecution deleteProdut(List<Product> list) {
        int state = productDao.deleteBanchProduct(list);
        if (state>0){
            return new ShopExecution(ShopStateEnum.SUCCESS);
        }else {
            return new ShopExecution(ShopStateEnum.INNER_ERROR);
        }
    }

    @Override
    public ProductExecution queryProductByShopId(int offset, int pageSize, Shop shop) {
        List<Product> list = productDao.queryProductByShopId(offset,pageSize,shop.getShopId());
        int count = productDao.queryCount(shop.getShopId());
        ProductExecution productExecution = new ProductExecution();
        if (list!=null){
            productExecution.setProductList(list);
            productExecution.setCount(count);
        }else {
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
        }
        return productExecution;
    }

    @Override
    public List<ProductCategory> queryProductCategory(int offset, int pageSize, Shop shop) {
        return null;
    }

  /*  @Override
    public List<ProductCategory> queryProductCategory() {
        List<ProductCategory> list = productDao.queryProductCategory();
        return list;
    }*/

    @Override
    public Product queryProductById(int productId) {
        Map<String,Object> map = new HashedMap();
        return productDao.queryProductById(productId);
    }

    @Override
    public ProductExecution queryProductByShopName(int offset, int pageSize, Shop shop) {
        List<Product> list = productDao.queryAllProductByShop(offset,pageSize, shop.getShopName());
        int count = productDao.queryCountByShop(shop.getShopName());
        ProductExecution productExecution = new ProductExecution();
        if (list!=null){
            productExecution.setProductList(list);
            productExecution.setCount(count);
        }else {
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
        }
        return productExecution;
    }

    @Override
    public List<Product> queryAllProduct(long shopId) {
        return productDao.queryAllProduct(shopId);
    }
}
