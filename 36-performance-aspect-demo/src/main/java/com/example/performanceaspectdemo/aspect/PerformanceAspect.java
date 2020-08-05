package com.example.performanceaspectdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect         //切面注解
@Component      //注册成为bean
@Slf4j
public class PerformanceAspect {
    //    @Around("execution(* com.example.performanceaspectdemo.repository..*(..))")
    @Around("repositoryOps()") //拦截repositoryOps方法的执行
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {
        //方法启动前记录当前时间
        long startTime = System.currentTimeMillis();
        String name = "-";
        String result = "Y";
        try {
            //获取方法名称
            name = pjp.getSignature().toShortString();
            return pjp.proceed();
        } catch (Throwable t) {
            result = "N";
            throw t;
        } finally {
            //方法结束后记录当前时间
            long endTime = System.currentTimeMillis();
            //方法执行耗时
            log.info("{};{};{}ms", name, result, endTime - startTime);
        }
    }

    //拦截指定的包下面所有类的所有方法的执行
    @Pointcut("execution(* com.example.performanceaspectdemo.repository..*(..))")
    private void repositoryOps() {
    }
}
