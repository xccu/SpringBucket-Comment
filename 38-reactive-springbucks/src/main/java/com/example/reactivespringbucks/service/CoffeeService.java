package com.example.reactivespringbucks.service;

import com.example.reactivespringbucks.model.Coffee;
import com.example.reactivespringbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class CoffeeService {
    private static final String PREFIX = "springbucks-";
    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private ReactiveRedisTemplate<String, Coffee> redisTemplate;

    /**
     * 缓存操作
     * @return
     */
    public Flux<Boolean> initCache() {
        //从H2数据库读取并写入redis缓存
        return coffeeRepository.findAll()
                .flatMap(c -> redisTemplate.opsForValue()
                        .set(PREFIX + c.getName(), c)
                        .flatMap(b -> redisTemplate.expire(PREFIX + c.getName(), Duration.ofMinutes(1)))
                        .doOnSuccess(v -> log.info("Loading and caching {}", c)));
    }

    public Mono<Coffee> findOneCoffee(String name) {

        //从redis查找，如果找不到则从数据库查找
        return redisTemplate.opsForValue().get(PREFIX + name)
                .switchIfEmpty(coffeeRepository.findByName(name)
                        .doOnSuccess(s -> log.info("Loading {} from DB.", name)));
    }
}
