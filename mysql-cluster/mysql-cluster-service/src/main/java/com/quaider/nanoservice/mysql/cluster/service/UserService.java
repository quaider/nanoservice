package com.quaider.nanoservice.mysql.cluster.service;

import com.quaider.nanoservice.mysql.cluster.dao.mapper.UserMapper;
import com.quaider.nanoservice.mysql.cluster.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void initUser() {
        int count = userMapper.count();
        int i = count;

        while (i < count + 1000) {
            User user = new User();
            user.setUserName("user_" + i);
            user.setPassword("123456");
            user.setNickName("kratos-" + UUID.randomUUID().toString());
            user.setCreateTime(new Date());

            userMapper.insert(user);
            i++;
        }
    }

    public int count() {
        return userMapper.count();
    }

}
