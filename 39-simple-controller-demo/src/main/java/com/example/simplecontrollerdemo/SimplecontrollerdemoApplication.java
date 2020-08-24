package com.example.simplecontrollerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories  //使用jpa Repositories注解
@EnableCaching          //增加使用缓存注解
public class SimplecontrollerdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplecontrollerdemoApplication.class, args);
    }

}
