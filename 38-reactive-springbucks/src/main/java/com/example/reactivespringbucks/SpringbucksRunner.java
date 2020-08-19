package com.example.reactivespringbucks;

import com.example.reactivespringbucks.model.*;
import com.example.reactivespringbucks.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * 单独的启动类
 */
@Component
@Slf4j
public class SpringbucksRunner implements ApplicationRunner {

    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //初始化缓存
        coffeeService.initCache()
                .then(
                        //通过name查找
                        coffeeService.findOneCoffee("mocha")
                                .flatMap(c -> {
                                    //创建订单
                                    CoffeeOrder order = createOrder("Li Lei", c);
                                    return orderService.create(order);
                                })
                                .doOnError(t -> log.error("error", t)))
                //订阅：输出
                .subscribe(o -> log.info("Create Order: {}", o));
        log.info("After Subscribe");
        Thread.sleep(5000);
    }

    private CoffeeOrder createOrder(String customer, Coffee... coffee) {
        return CoffeeOrder.builder()
                .customer(customer)
                .items(Arrays.asList(coffee))
                .state(OrderState.INIT)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }

    /*redis缓存:
    keys *
            1) "springbucks-menu"
            2) "springbucks-capuccino"
            3) "springbucks-mocha"
            4) "springbucks-espresso"
            5) "springbucks-latte"
            6) "springbucks-macchiato"
    */
}
