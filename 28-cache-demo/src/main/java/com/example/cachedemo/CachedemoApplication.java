package com.example.cachedemo;

import com.example.cachedemo.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;




@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
@EnableCaching(proxyTargetClass = true) //缓存基于AOP拦截，会拦截类的执行,在当前类中使用缓存
public class CachedemoApplication implements ApplicationRunner {
    @Autowired
    private CoffeeService coffeeService;

    public static void main(String[] args) {
        SpringApplication.run(CachedemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //首次调用后结果会写入缓存
        log.info("Count: {}", coffeeService.findAllCoffee().size());
        for (int i = 0; i < 10; i++) {
            log.info("Reading from cache.");
            //再次调用，从缓存读取结果，不会调用数据库
            coffeeService.findAllCoffee();
        }
        //清理缓存
        coffeeService.reloadCoffee();
        log.info("Reading after refresh.");
        coffeeService.findAllCoffee().forEach(c -> log.info("Coffee {}", c.getName()));
    }
}