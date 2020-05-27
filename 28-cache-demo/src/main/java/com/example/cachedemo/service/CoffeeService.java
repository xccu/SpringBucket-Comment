package com.example.cachedemo.service;

import com.example.cachedemo.model.Coffee;
import com.example.cachedemo.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
@CacheConfig(cacheNames = "coffee") //启用了缓存的方法,缓存名称为coffee
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    @Cacheable//缓存注解，执行后返回对象会被放入缓存中
    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    @CacheEvict//缓存清理注解,调用后清理缓存
    public void reloadCoffee() {
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Coffee Found: {}", coffee);
        return coffee;
    }
}
