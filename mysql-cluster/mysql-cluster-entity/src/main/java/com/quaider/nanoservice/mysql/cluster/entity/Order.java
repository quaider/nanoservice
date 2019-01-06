package com.quaider.nanoservice.mysql.cluster.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Order {
    private long id;
    private String orderNumber;
    private long buyerId;
    private short status;
    private short payStatus;
    private BigDecimal orderAmount;
    private BigDecimal payAmount;
    private String remark;
    private Date createTime;
    private Date updateTime;

    private List<OrderItem> orderItems;

    public Order() {
        orderItems = new ArrayList<>();
    }
}
