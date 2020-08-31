package com.example.foo;

import com.example.context.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 配置类
 */
@Configuration
@EnableAspectJAutoProxy  //开启AspectJAutoProxy支持
public class FooConfig {
    /**
     * 配置TestBean，名为testBeanX
     * @return
     */
    @Bean
    public TestBean testBeanX() {
        return new TestBean("foo");
    }

    /**
     * 配置TestBean，名为testBeanY
     * @return
     */
    @Bean
    public TestBean testBeanY() {
        return new TestBean("foo");
    }

    /**
     * Aop切面声明,子类别context不会被aop增强
     * @return
     */
    @Bean
    public FooAspect fooAspect() {
        return new FooAspect();
    }
}
