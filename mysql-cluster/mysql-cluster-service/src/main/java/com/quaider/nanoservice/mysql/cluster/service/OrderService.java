package com.quaider.nanoservice.mysql.cluster.service;

import com.quaider.nanoservice.mysql.cluster.dao.mapper.OrderItemMapper;
import com.quaider.nanoservice.mysql.cluster.dao.mapper.OrderMapper;
import com.quaider.nanoservice.mysql.cluster.dao.mapper.ProductMapper;
import com.quaider.nanoservice.mysql.cluster.entity.Order;
import com.quaider.nanoservice.mysql.cluster.entity.OrderItem;
import com.quaider.nanoservice.mysql.cluster.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ProductMapper productMapper;

    public Order getById(long id) {
        return orderMapper.getById(id);
    }

    public long insertOrder(Order order) {

        if (order == null) throw new RuntimeException("order must not be null");
        if (order.getOrderItems() == null || order.getOrderItems().size() <= 0)
            throw new RuntimeException("order must has order items");
        if (order.getBuyerId() <= 0) throw new RuntimeException("order must belong to a user");

        List<Long> productIds = order.getOrderItems().stream().map(oi -> oi.getProductId()).collect(Collectors.toList());
        List<Product> products = productMapper.getByIds(productIds);

        Date now = new Date();
        order.setCreateTime(now);
        order.setStatus((short) 1);
        order.setPayStatus((short) 1);
        order.setOrderNumber(System.currentTimeMillis() + "");

        BigDecimal total = BigDecimal.valueOf(0);

        for (OrderItem oi : order.getOrderItems()) {
            Optional<Product> optional = products.stream().filter(f -> f.getId() == oi.getProductId()).findFirst();
            if (optional == null) continue;
            Product p = optional.get();

            BigDecimal oiAmount = p.getProductPrice().multiply(BigDecimal.valueOf(oi.getProductNumber()));
            oi.setCreateTime(now);
            oi.setBuyerId(order.getBuyerId());
            oi.setProductName(p.getProductName());
            oi.setProductPrice(p.getProductPrice());
            oi.setProductAmount(oiAmount);
            oi.setOrderNumber(order.getOrderNumber());

            total = total.add(oiAmount);

        }

        order.setOrderAmount(total);
        order.setPayAmount(total);

        orderMapper.insert(order);

        for (OrderItem oi : order.getOrderItems()) {
            oi.setOrderId(order.getId());
            orderItemMapper.insert(oi);
        }

        return order.getId();
    }
}
