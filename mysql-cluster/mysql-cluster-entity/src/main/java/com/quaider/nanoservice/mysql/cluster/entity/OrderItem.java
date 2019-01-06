package com.quaider.nanoservice.mysql.cluster.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItem {
    private long id;
    private long orderId;
    private long buyerId;
    private String orderNumber;
    private long productId;
    private String productName;
    private BigDecimal productPrice;
    private int productNumber;
    private BigDecimal productAmount;
    private Date createTime;
    private Date updateTime;
}
