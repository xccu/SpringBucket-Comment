package com.example.r2dbcrepositorydemo;

import com.example.r2dbcrepositorydemo.converter.MoneyReadConverter;
import com.example.r2dbcrepositorydemo.converter.MoneyWriteConverter;
import com.example.r2dbcrepositorydemo.repository.CoffeeRepository;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.dialect.Dialect;
import org.springframework.data.r2dbc.function.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
@EnableR2dbcRepositories
public class R2dbcrepositorydemoApplication extends AbstractR2dbcConfiguration
        implements ApplicationRunner {

    @Autowired
    private CoffeeRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(R2dbcrepositorydemoApplication.class, args);
    }

    //实现AbstractR2dbcConfiguration中的ConnectionFactory
    @Bean
    public ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .inMemory("testdb")
                        .username("sa")
                        .build());
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        Dialect dialect = getDialect(connectionFactory());
        CustomConversions.StoreConversions storeConversions =
                CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder());
        return new R2dbcCustomConversions(storeConversions,
                Arrays.asList(new MoneyReadConverter(), new MoneyWriteConverter()));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CountDownLatch cdl = new CountDownLatch(2);

        //flux默认方法：通过ID查找
        repository.findAllById(Flux.just(1L, 2L))
                .map(c -> c.getName() + "-" + c.getPrice().toString())  //通过map映射转换为String输出
                .doFinally(s -> cdl.countDown())                        //结束后执行countDown
                .subscribe(c -> log.info("Find {}", c));                //订阅：打印结果

        //通过name查找
        repository.findByName("mocha")
                .doFinally(s -> cdl.countDown())
                .subscribe(c -> log.info("Find {}", c));

        cdl.await();
    }

}
