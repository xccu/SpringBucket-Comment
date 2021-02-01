package com.example.demo.service;

import com.example.demo.model.Coffee;
import com.example.demo.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    //返回Mono类型
    public Mono<Coffee> findById(Long id) {
        return coffeeRepository.findById(id);
    }

    //返回Flux类型
    public Flux<Coffee> findAll() {
        return coffeeRepository.findAll();
    }

    //返回Mono类型
    public Mono<Coffee> findByName(String name) {
        return coffeeRepository.findByName(name);
    }
}
