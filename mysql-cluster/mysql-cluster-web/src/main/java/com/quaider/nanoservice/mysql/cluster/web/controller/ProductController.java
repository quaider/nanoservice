package com.quaider.nanoservice.mysql.cluster.web.controller;

import com.quaider.nanoservice.mysql.cluster.entity.Product;
import com.quaider.nanoservice.mysql.cluster.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public Product add(@RequestBody Product product) {
        productService.insert(product);
        return product;
    }

    @GetMapping("/{productId}")
    public Product get(@PathVariable Long productId) {
        return productService.getById(productId);
    }

}
