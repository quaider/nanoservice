package com.quaider.nanoservice.mysql.cluster.service;

import com.quaider.nanoservice.mysql.cluster.dao.mapper.ProductMapper;
import com.quaider.nanoservice.mysql.cluster.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductMapper productMapper;

    public List<Product> getAll() {
        return productMapper.getAll();
    }

    public boolean existsByCode(String code) {
        return productMapper.existsByCode(code) > 0;
    }

    public Product getById(long id) {
        return productMapper.getById(id);
    }

    public long insert(Product product) {
        if (product == null || StringUtils.isEmpty(product.getProductCode()))
            throw new IllegalArgumentException("product");

        if (existsByCode(product.getProductCode()))
            throw new RuntimeException("product is already exists");

        if (product.getCreateTime() == null) product.setCreateTime(new Date());
        return productMapper.insert(product);
    }

}
