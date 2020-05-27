package com.example.declarativetransactiondemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FooServiceImpl implements FooService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //事务处理sql
    @Override
    @Transactional
    public void insertRecord() {
        //正常执行，事务提交
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('AAA')");
    }

    //回滚事务，指定抛出的异常
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('BBB')");
        throw new RollbackException(); //抛出异常，事务回滚
    }

    //类的内部调用事务的代理方法，不会有事务执行（方法本身没有事务）
    @Override
    public void invokeInsertThenRollback() throws RollbackException {
        insertThenRollback();
    }
}
