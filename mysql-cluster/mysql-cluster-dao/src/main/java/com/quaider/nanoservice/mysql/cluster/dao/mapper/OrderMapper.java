package com.quaider.nanoservice.mysql.cluster.dao.mapper;

import com.quaider.nanoservice.mysql.cluster.entity.Order;
import com.quaider.nanoservice.mysql.cluster.entity.OrderItem;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

@Mapper
public interface OrderMapper {

    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "orderNumber", column = "order_number"),
            @Result(property = "buyerId", column = "buyer_id"),
            @Result(property = "payStatus", column = "pay_status"),
            @Result(property = "orderAmount", column = "order_amount"),
            @Result(property = "payAmount", column = "pay_amount"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "orderItems", column = "{orderId=id,userId=buyer_id}",
                    many = @Many(select = "com.quaider.nanoservice.mysql.cluster.dao.mapper.OrderItemMapper.getOrderItems", fetchType = FetchType.EAGER)
            )
    })
    @Select("select * from t_order where id = #{id}")
    Order getById(long id);

    @Options(useGeneratedKeys = true, keyColumn = "id")
    @Insert("insert into t_order(order_number, buyer_id, status, pay_status, order_amount, pay_amount, create_time) values(#{orderNumber},#{buyerId},#{status},#{payStatus},#{orderAmount},#{payAmount},#{createTime})")
    int insert(Order order);
}
