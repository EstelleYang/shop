package com.o2o.Service;

import com.o2o.DO.Product;
import com.o2o.DO.ProductCategory;
import com.o2o.DO.Shop;
import com.o2o.DTO.ProductExecution;
import com.o2o.DTO.ShopExecution;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    public ShopExecution insertProduct(List<Product> list);
    public ShopExecution deleteProdut(List<Product> list);
    public ProductExecution queryProductByShopId(int offset, int pageSize, Shop shop);
    public List<ProductCategory> queryProductCategory(int offset, int pageSize, Shop shop);
    public Product queryProductById(int productId);
    public ProductExecution queryProductByShopName(int offset, int pageSize, Shop shop);
}

