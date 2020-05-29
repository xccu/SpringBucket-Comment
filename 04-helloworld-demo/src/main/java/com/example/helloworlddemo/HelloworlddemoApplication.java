package com.example.helloworlddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class HelloworlddemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloworlddemoApplication.class, args);
    }

    //利用@RequestMapping配置url和方法之间的映射
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}

