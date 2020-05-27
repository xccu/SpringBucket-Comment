package com.example.mybatisdemo;

import com.example.mybatisdemo.mapper.CoffeeMapper;
import com.example.mybatisdemo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@MapperScan("com.example.mybatisdemo.mapper")  //指定扫描的包
public class MybatisdemoApplication implements ApplicationRunner {

    @Autowired
    private CoffeeMapper coffeeMapper;

    public static void main(String[] args) {
        SpringApplication.run(MybatisdemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //使用bulid实例化对象
        Coffee c = Coffee.builder().name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0)).build();
        //保存并返回影响的结果集数
        int count = coffeeMapper.save(c);
        log.info("Save {} Coffee: {}", count, c);

        c = Coffee.builder().name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 25.0)).build();
        count = coffeeMapper.save(c);
        log.info("Save {} Coffee: {}", count, c);

        //通过id查找
        c = coffeeMapper.findById(c.getId());
        log.info("Find Coffee: {}", c);
    }
}
