package com.quaider.nanoservice.mysql.cluster.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {
    private long id;
    private String productCode;
    private String productName;
    private BigDecimal productPrice;
    private Date createTime;
    private Date updateTime;
}
