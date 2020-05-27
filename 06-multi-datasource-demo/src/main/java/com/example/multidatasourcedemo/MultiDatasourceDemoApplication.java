package com.example.multidatasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

//查看依赖：http://localhost:8080/actuator/beans

//排除掉自动配置的类
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class})
@Slf4j
public class MultiDatasourceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDatasourceDemoApplication.class, args);
    }

    //注解创建datasourceproperty，将配置信息传入赋值
    @Bean
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties() {
        return new DataSourceProperties();
    }

    //创建数据源
    @Bean
    public DataSource fooDataSource() {
        DataSourceProperties dataSourceProperties = fooDataSourceProperties();
        log.info("foo datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    //创建事务管理器
    @Bean
    @Resource
    public PlatformTransactionManager fooTxManager(DataSource fooDataSource) {
        return new DataSourceTransactionManager(fooDataSource);
    }

    @Bean
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource barDataSource() {
        DataSourceProperties dataSourceProperties = barDataSourceProperties();
        log.info("bar datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager barTxManager(DataSource barDataSource) {
        return new DataSourceTransactionManager(barDataSource);
    }
}