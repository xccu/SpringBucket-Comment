package com.example.demo;

import com.example.demo.controller.PerformanceInteceptor;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;


//继承WebMvcConfigurer，用作配置类
@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
public class DemoApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加Inteceptor
        registry.addInterceptor(new PerformanceInteceptor())
                .addPathPatterns("/coffee/**")  //指定拦截/coffee/下的url请求
                .addPathPatterns("/order/**");  //指定拦截/order/下的url请求
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    /**
     * Jackson ObjectMapper自定义
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonBuilderCustomizer() {
        return builder -> {
            //定义返回的ResponseBody json缩进
            builder.indentOutput(true);
            //定义返回的ResponseBody 日期时区
            builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        };
    }
}
