package com.example.foo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * 切面
 */
@Aspect
@Slf4j
public class FooAspect {

    /**
     * testBean执行完成后打印after hello()
     * 拦截规则：所有以testBean开头的bean
     */
    @AfterReturning("bean(testBean*)")
    public void printAfter() {
        log.info("after hello()");
    }
}
