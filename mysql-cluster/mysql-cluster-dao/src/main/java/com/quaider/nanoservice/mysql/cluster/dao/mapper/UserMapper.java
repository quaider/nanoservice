package com.quaider.nanoservice.mysql.cluster.dao.mapper;

import com.quaider.nanoservice.mysql.cluster.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Options(useGeneratedKeys = true, keyColumn = "id")
    @Insert("insert into t_user(user_name, password, nick_name, create_time) values (#{userName}, #{password}, #{nickName}, #{createTime})")
    int insert(User user);

    @ResultType(int.class)
    @Select("select count(*) from t_user")
    int count();
}
