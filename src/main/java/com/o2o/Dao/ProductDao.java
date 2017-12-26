package com.o2o.Dao;

import com.o2o.DO.Product;
import com.o2o.DO.ProductCategory;
import com.o2o.DO.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductDao {
    public int insertBanchProduct(List<Product> products);

    public int deleteBanchProduct(List<Product> products);

    public List<Product> queryProductByShopId(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("shopId") Long shopId);

    public int queryCount(@Param("shopId") Long shopId);

    public List<Product> queryAllProduct(@Param("shopId") Long shopId);

    public List<ProductCategory> queryProductCategory();

    public Product queryProductById(int productId);

    public List<Integer>queryProductByShop();

    public List<Product>queryAllProductByShop(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("shopName") String shopName);

    public int queryCountByShop(@Param("shopName")String shopName);
}
