package com.example.demo.service;

import com.example.demo.model.CoffeeOrder;
import com.example.demo.model.OrderState;
import com.example.demo.repository.CoffeeOrderRepository;
import com.example.demo.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CoffeeOrderRepository orderRepository;
    @Autowired
    private CoffeeRepository coffeeRepository;

    //调用get方法
    public Mono<CoffeeOrder> getById(Long id) {
        return orderRepository.get(id);
    }

    //创建coffee对象
    public Mono<Long> create(String customer, List<String> items) {
        return Flux.fromStream(items.stream())
                .flatMap(n -> coffeeRepository.findByName(n))       //通过coffeeName查找Coffee，返回Flux<Coffee>
                .collectList()                                      //Flux转List
                .flatMap(l ->
                        orderRepository.save(CoffeeOrder.builder()  //构造CoffeeOrder对象并保存
                                .customer(customer)
                                .items(l)                           //将list传给items
                                .state(OrderState.INIT)
                                .createTime(new Date())
                                .updateTime(new Date())
                                .build())
                );
    }
}
