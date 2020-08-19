package com.example.reactivespringbucks.service;

import com.example.reactivespringbucks.model.CoffeeOrder;
import com.example.reactivespringbucks.repository.CoffeeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    @Autowired
    private CoffeeOrderRepository repository;
    @Autowired
    private DatabaseClient client;

    /**
     * 保存CoffeeOrder
     * @param order
     * @return
     */
    public Mono<Long> create(CoffeeOrder order) {
        return repository.save(order);
    }
}
