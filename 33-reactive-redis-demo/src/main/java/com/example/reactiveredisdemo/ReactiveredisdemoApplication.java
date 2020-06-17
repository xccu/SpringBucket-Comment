package com.example.reactiveredisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class ReactiveredisdemoApplication implements ApplicationRunner {
   private static final String KEY = "COFFEE_MENU";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveredisdemoApplication.class, args);
    }

    //自定义的ReactiveStringRedisTemplate 传入一个ReactiveRedisConnectionFactory
    @Bean
    ReactiveStringRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveStringRedisTemplate(factory);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //取出hash操作的operation，定义key，hashkey，hashvalue均为string
        ReactiveHashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        CountDownLatch cdl = new CountDownLatch(1);

        //查询t_coffee表
        List<Coffee> list = jdbcTemplate.query(
                "select * from t_coffee", (rs, i) ->
                        Coffee.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .price(rs.getLong("price"))
                                .build()
        );

        //通过flux取出list中所有元素
        Flux.fromIterable(list)
                //以另一个单线程发布
                .publishOn(Schedulers.single())
                //发布结束后打印list ok
                .doOnComplete(() -> log.info("list ok"))
                //flatMap元素映射，每一个元素放入到redis hash中
                .flatMap(c -> {
                    log.info("try to put {},{}", c.getName(), c.getPrice());
                    return hashOps.put(KEY, c.getName(), c.getPrice().toString());
                })
                //执行完毕后打印set ok
                .doOnComplete(() -> log.info("set ok"))

                //设置COFFEE_MENU有效期为一分钟
                .concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))
                //执行完毕后打印expire ok
                .doOnComplete(() -> log.info("expire ok"))
                //发生错误，记录异常
                .onErrorResume(e -> {
                    log.error("exception {}", e.getMessage());
                    return Mono.just(false);
                })
                //订阅
                .subscribe(b -> log.info("Boolean: {}", b),
                        e -> log.error("Exception {}", e.getMessage()),
                        () -> cdl.countDown()); //当新线程运行结束后，主线程才退出

        log.info("Waiting");
        //主线程等待其他线程的运行结果
        cdl.await();
    }

}


 /*
        打印内容：
        [           main] c.e.r.ReactiveredisdemoApplication       : Waiting
        [       single-1] c.e.r.ReactiveredisdemoApplication       : try to put espresso,2000
        [       single-1] c.e.r.ReactiveredisdemoApplication       : try to put latte,2500
        [       single-1] c.e.r.ReactiveredisdemoApplication       : try to put capuccino,2500
        [       single-1] c.e.r.ReactiveredisdemoApplication       : try to put mocha,3000
        [       single-1] c.e.r.ReactiveredisdemoApplication       : try to put macchiato,3000
        [       single-1] c.e.r.ReactiveredisdemoApplication       : list ok
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : Boolean: true
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : Boolean: true
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : Boolean: true
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : Boolean: true
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : Boolean: true
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : set ok
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : Boolean: true
        [ioEventLoop-4-1] c.e.r.ReactiveredisdemoApplication       : expire ok
        [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
        [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
*/