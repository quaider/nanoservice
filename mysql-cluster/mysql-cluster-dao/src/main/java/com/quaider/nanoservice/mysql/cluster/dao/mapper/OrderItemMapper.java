package com.quaider.nanoservice.mysql.cluster.dao.mapper;

import com.quaider.nanoservice.mysql.cluster.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderItemMapper {
    @Results({
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "orderNumber", column = "order_number"),
            @Result(property = "buyerId", column = "buyer_id"),
            @Result(property = "productId", column = "product_id"),
            @Result(property = "productName", column = "product_name"),
            @Result(property = "productPrice", column = "product_price"),
            @Result(property = "productNumber", column = "product_number"),
            @Result(property = "productAmount", column = "product_amount"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("select * from t_order_item where buyer_id=#{userId} and order_id=#{orderId}")
    List<OrderItem> getOrderItems(Map map);

    @Insert("insert into t_order_item(order_id,order_number,buyer_id,product_id,product_name,product_price,product_number,product_amount,create_time) values(#{orderId},#{orderNumber},#{buyerId},#{productId},#{productName},#{productPrice},#{productNumber},#{prouctAmount},#{createTime})")
    int insert(OrderItem orderItem);
}
