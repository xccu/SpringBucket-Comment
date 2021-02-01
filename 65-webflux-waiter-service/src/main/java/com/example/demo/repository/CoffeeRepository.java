package com.example.demo.repository;

import com.example.demo.model.Coffee;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import reactor.core.publisher.Mono;

/**
 * 使用 R2dbcRepository
 */
public interface CoffeeRepository extends R2dbcRepository<Coffee, Long> {

    /**
     * findByName自定义方法
     * @param name
     * @return
     */
    @Query("select * from t_coffee where name=$1")
    Mono<Coffee> findByName(String name);
}
