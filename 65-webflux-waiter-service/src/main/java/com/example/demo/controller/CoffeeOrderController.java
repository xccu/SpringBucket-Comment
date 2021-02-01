package com.example.demo.controller;

import com.example.demo.controller.request.NewOrderRequest;
import com.example.demo.model.CoffeeOrder;
import com.example.demo.service.CoffeeService;
import com.example.demo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/{id}")
    public Mono<CoffeeOrder> getOrder(@PathVariable("id") Long id) {
        return orderService.getById(id);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CoffeeOrder> create(@RequestBody NewOrderRequest newOrder) {
        log.info("Receive new Order {}", newOrder); //Request对象中取出Customer和Items
        return orderService.create(newOrder.getCustomer(), newOrder.getItems())
                .flatMap(id -> orderService.getById(id)); //返回id并查找
    }
}
