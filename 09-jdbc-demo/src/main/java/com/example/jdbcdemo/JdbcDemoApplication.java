package com.example.jdbcdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;


@SpringBootApplication
@Slf4j
public class JdbcDemoApplication implements CommandLineRunner {

    //通过依赖注入获取实例
    @Autowired
    private FooDao fooDao;
    @Autowired
    private BatchFooDao batchFooDao;

    public static void main(String[] args) {
        SpringApplication.run(JdbcDemoApplication.class, args);
    }

    @Bean
    @Autowired
    public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FOO").usingGeneratedKeyColumns("ID");
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    //重写run方法
    @Override
    public void run(String... args) throws Exception {

        fooDao.insertData();       //插入数据
        batchFooDao.batchInsert(); //批量插入数据
        fooDao.listData();         //获取listData
    }

}