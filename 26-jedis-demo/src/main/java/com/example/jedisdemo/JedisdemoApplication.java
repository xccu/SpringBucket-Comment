package com.example.jedisdemo;

import com.example.jedisdemo.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
public class JedisdemoApplication  implements ApplicationRunner {

    @Autowired
    private CoffeeService coffeeService;
    //JedisPool注入
    @Autowired
    private JedisPool jedisPool;
    //JedisPoolConfig注入
    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    public static void main(String[] args) {
        SpringApplication.run(JedisdemoApplication.class, args);
    }

    //配置Bean jedisPool
    @Bean(destroyMethod = "close")
    public JedisPool jedisPool(@Value("${redis.host}") String host) {
        return new JedisPool(jedisPoolConfig(), host);
    }

    //配置Bean JedisPoolConfig
    @Bean
    @ConfigurationProperties("redis")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        //输出jedisPoolConfig配置信息
        log.info(jedisPoolConfig.toString());

        //获取jedis实例
        //try-with-resource语法，try结束后会把这个resource关闭,类似c#的using()语法
        try (Jedis jedis = jedisPool.getResource()) {

            //执行HSET命令以Hash类型写入Redis
            coffeeService.findAllCoffee().forEach(c -> {
                jedis.hset("springbucks-menu",
                        c.getName(),
                        Long.toString(c.getPrice().getAmountMinorLong()));
            });

            //对应Redis命令：
            //HGETALL springbucks-menu
            Map<String, String> menu = jedis.hgetAll("springbucks-menu");
            log.info("Menu: {}", menu);

            //对应Redis命令：
            //HGET springbucks-menu espresso
            String price = jedis.hget("springbucks-menu", "espresso");
            log.info("espresso - {}",
                    Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(price)));
        }
    }

}
