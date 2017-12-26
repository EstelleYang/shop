package com.o2o.Dao;

import com.o2o.DO.Order;
import com.o2o.DO.Page;
import com.o2o.DO.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    public List<Order> getUserOrders(int userid);
    public User getUserById(int id);
    public List<Order> getOrderListPage(@Param("page") Page page, @Param("userid") int userid);
}
