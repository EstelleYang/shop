package com.o2o.Dao;

import com.o2o.DO.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDao {
    //新增店铺
    int insertShop(Shop shop);
    //更新店铺信息
    int updateShop(Shop shop);
    //查询店铺信息
    Shop queryShopInfo(long shopId);

    /**
     * 分页查询店铺，可输入的条件：店铺名（模糊查询），店铺状态，店铺类别，区域id，owner
     * @param shopCondition 查询条件
     * @param rowIndex:从第几行开始取数据
     * @param pageSize：返回的条数
     * @return 返回店铺列表
     */
    List<Shop> queryshopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

    /**
     * 查询固定条件下的店铺总数
     * @param shopCondition
     * @return int
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);
}
