package com.example.springbucks.service;

import com.example.springbucks.model.*;
import com.example.springbucks.repository.CoffeeRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public Optional<Coffee> findOneCoffee(String name) {

        //ExampleMatcher匹配name属性，忽略大小写
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact().ignoreCase());
        //通过name查找
        Optional<Coffee> coffee = coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(), matcher));

        log.info("Coffee Found: {}", coffee);
        return coffee;
    }
}
