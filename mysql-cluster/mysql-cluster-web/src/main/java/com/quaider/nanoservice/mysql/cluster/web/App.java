package com.quaider.nanoservice.mysql.cluster.web;

import com.quaider.nanoservice.mysql.cluster.dao.mapper.ProductMapper;
import com.quaider.nanoservice.mysql.cluster.service.ProductService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableAutoConfiguration
@MapperScan(basePackageClasses = {ProductMapper.class})
@ComponentScan(basePackageClasses = {ProductService.class, App.class})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
