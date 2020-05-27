package com.example.programmatictransactiondemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootApplication
@Slf4j  //日志注解
public class ProgrammaticTransactionDemoApplication implements CommandLineRunner {

    //依赖注入获取实例
    @Autowired
    private TransactionTemplate transactionTemplate;   //事务模板
    @Autowired
    private JdbcTemplate jdbcTemplate;                 //jdbc模板

    public static void main(String[] args) {
        SpringApplication.run(ProgrammaticTransactionDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("COUNT BEFORE TRANSACTION: {}", getCount());
        //事务处理（无返回值的回调）
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'aaa')");
                log.info("COUNT IN TRANSACTION: {}", getCount());
                //事务设置为只能回滚
                transactionStatus.setRollbackOnly();
            }
        });

        log.info("COUNT AFTER TRANSACTION: {}", getCount());
    }

    private long getCount() {
        return (long) jdbcTemplate.queryForList("SELECT COUNT(*) AS CNT FROM FOO")
                .get(0).get("CNT");
    }
}
