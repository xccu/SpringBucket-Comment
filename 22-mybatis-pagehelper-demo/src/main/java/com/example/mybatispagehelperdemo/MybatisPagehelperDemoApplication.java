package com.example.mybatispagehelperdemo;

import com.example.mybatispagehelperdemo.mapper.CoffeeMapper;
import com.example.mybatispagehelperdemo.model.Coffee;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@MapperScan("com.example.mybatispagehelperdemo.mapper")
public class MybatisPagehelperDemoApplication implements ApplicationRunner {

    @Autowired
    private CoffeeMapper coffeeMapper;

    public static void main(String[] args) {
        SpringApplication.run(MybatisPagehelperDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //使用RowBounds分页
        //第1页，每页3条记录
        coffeeMapper.findAllWithRowBounds(new RowBounds(1, 3))
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        //第2页，每页3条记录
        coffeeMapper.findAllWithRowBounds(new RowBounds(2, 3))
                .forEach(c -> log.info("Page(2) Coffee {}", c));
        log.info("===================");

        //页面大小为0，取出所有记录
        coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        log.info("===================");

        //使用参数传入页码和页大小
        coffeeMapper.findAllWithParam(1, 3)
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        List<Coffee> list = coffeeMapper.findAllWithParam(2, 3);
        //打印页面信息
        PageInfo page = new PageInfo(list);
        log.info("PageInfo: {}", page);
    }

}
