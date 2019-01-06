package com.quaider.nanoservice.mysql.cluster.dao.mapper;

import com.quaider.nanoservice.mysql.cluster.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("select * from t_product")
    @Results(id = "productMap", value = {
            @Result(property = "productCode", column = "product_code"),
            @Result(property = "productName", column = "product_name"),
            @Result(property = "productPrice", column = "product_price"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<Product> getAll();

    @Insert("insert into t_product(product_code, product_name,product_price,create_time,update_time) values(#{productCode}, #{productName}, #{productPrice}, #{createTime}, #{updateTime});")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    long insert(Product product);

    @ResultType(int.class)
    @Select("select count(id) from t_product where product_code = #{code}")
    int existsByCode(String code);

    @Select("select * from t_product where id = #{id}")
    @ResultMap(value = "productMap")
    Product getById(long id);

    @ResultType(value = ArrayList.class)
    @ResultMap(value = "productMap")
    @Select("<script>" +
            "select * from t_product where id in " +
            "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>" +
            "</script>")
    List<Product> getByIds(List<Long> list);
}
