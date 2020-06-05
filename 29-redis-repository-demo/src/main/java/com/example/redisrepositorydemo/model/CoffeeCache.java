package com.example.redisrepositorydemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

//定义缓存实体类
@RedisHash(value = "springbucks-coffee", timeToLive = 60) //RedisHash定义对应的Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoffeeCache {
    @Id
    private Long id; //ID
    @Indexed
    private String name; //二级索引
    private Money price;
}
