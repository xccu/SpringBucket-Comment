package com.example.datasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@Slf4j   //日志使用注解
public class DatasourceDemoApplication implements CommandLineRunner {

    //依赖注入获取实例
    //DataSource数据源
    @Autowired
    private DataSource dataSource;
    //JdbcTemplate是Spring的一部分,是对数据库的操作在jdbc的封装
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DatasourceDemoApplication.class, args);
    }

    //重写run方法
    @Override
    public void run(String... args) throws Exception {
        showConnection();
        showData();
    }

    private void showConnection() throws SQLException {
        log.info(dataSource.toString());
        Connection conn = dataSource.getConnection();
        log.info(conn.toString());
        conn.close();
    }

    private void showData() {
		jdbcTemplate.queryForList("SELECT * FROM FOO")
				.forEach(row -> log.info(row.toString()));

    }

}
