package com.quaider.nanoservice.mysql.cluster.web.controller;

import com.quaider.nanoservice.mysql.cluster.entity.Order;
import com.quaider.nanoservice.mysql.cluster.entity.OrderItem;
import com.quaider.nanoservice.mysql.cluster.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/add")
    public String add(@RequestBody Order order) {
        orderService.insertOrder(order);

        return "order added";
    }

    @GetMapping("/{orderId}")
    public Order get(@PathVariable long orderId) {
        Order order = orderService.getById(orderId);
        List<OrderItem> orderItems = order.getOrderItems();
        return order;
    }

}
