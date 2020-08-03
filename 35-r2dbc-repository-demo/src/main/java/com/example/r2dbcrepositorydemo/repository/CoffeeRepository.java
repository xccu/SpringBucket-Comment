package com.example.r2dbcrepositorydemo.repository;

import com.example.r2dbcrepositorydemo.model.Coffee;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

//继承了ReactiveCrudRepository接口，指定数据实体类型Coffee和主键类型Long
public interface CoffeeRepository extends ReactiveCrudRepository<Coffee, Long> {
    @Query("select * from t_coffee where name = $1")
    Flux<Coffee> findByName(String name);
}
