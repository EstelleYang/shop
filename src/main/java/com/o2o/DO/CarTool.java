package com.o2o.DO;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class CarTool {
    /**
     * OrderItem：订单------包含不同id的商品，商品数量
     * 购物车用Map泛型表示，将订单放到购物车
     */
    Map<Integer,OrderItem> item ;

    public Map<Integer, OrderItem> getItem() {
        return item;
    }

    public void setItem(Map<Integer, OrderItem> item) {
        this.item = item;
    }


    //初始化购物车
    public CarTool(){
        if (item==null){
            item = new HashMap<>();
        }
    }
    public void addProduct(int productId, OrderItem orderItem,int quantity){
        if(item.containsKey(productId)){
            OrderItem _orderItem = item.get(productId);
            orderItem.setQuantity(_orderItem.getQuantity()+orderItem.getQuantity());
        }else {
            Product product = new Product();
            product.setProductId(productId);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
        }
        item.put(productId,orderItem);
    }
    public void updateProduct(int productId,int quantity){
        OrderItem orderItem = item.get(productId);
        orderItem.setQuantity(quantity);
        item.put(productId,orderItem);
    }
    public double totalPrice(){
        double totalPrice = 0;
        for(Iterator it = item.values().iterator();it.hasNext();){
            OrderItem orderItem = (OrderItem)it.next();
            totalPrice = orderItem.getQuantity()*Double.parseDouble(orderItem.getProduct().getPromotionPrice());
        }
            return totalPrice;
        }
}