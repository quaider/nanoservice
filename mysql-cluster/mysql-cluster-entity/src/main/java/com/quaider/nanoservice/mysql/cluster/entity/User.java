package com.quaider.nanoservice.mysql.cluster.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private long id;
    private String userName;
    private String password;
    private String nickName;
    private Date createTime;
    private Date updateTime;
}
