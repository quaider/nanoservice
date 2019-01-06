package com.quaider.nanoservice.mysql.cluster.web.controller;

import com.quaider.nanoservice.mysql.cluster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/init")
    public String init() {
        userService.initUser();
        return "ok";
    }

    @GetMapping("/count")
    public int count() {
        return userService.count();
    }
}
