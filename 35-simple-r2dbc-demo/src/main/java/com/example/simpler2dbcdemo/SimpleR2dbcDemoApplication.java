package com.example.simpler2dbcdemo;

import com.example.simpler2dbcdemo.converter.MoneyReadConverter;
import com.example.simpler2dbcdemo.converter.MoneyWriteConverter;
import com.example.simpler2dbcdemo.model.Coffee;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.dialect.Dialect;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.function.convert.R2dbcCustomConversions;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class SimpleR2dbcDemoApplication extends AbstractR2dbcConfiguration
		implements ApplicationRunner {
	@Autowired
	private DatabaseClient client;

	public static void main(String[] args) {
		SpringApplication.run(SimpleR2dbcDemoApplication.class, args);
	}

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

		client.execute()
				.sql("select * from t_coffee")                   //执行select语句
				.as(Coffee.class)                                   //转换成Coffee对象
				.fetch()                                            //获取结果集
				.first()                                            //获取第一条
				.doFinally(s -> cdl.countDown())
//				.subscribeOn(Schedulers.elastic())
				.subscribe(c -> log.info("Fetch execute() {}", c)); //订阅：打印结果

		client.select()                                                     //使用select查询
				.from("t_coffee")                                        //指定查询的表明
				.orderBy(Sort.by(Sort.Direction.DESC, "id"))     //按照id降序排列
				.page(PageRequest.of(0, 3))                      //分页：大小3，第0页
				.as(Coffee.class)                                           //转换成Coffee对象
				.fetch()                                                    //获取结果集
				.all()                                                      //获取全部
				.doFinally(s -> cdl.countDown())                            //结束后执行countDown
//				.subscribeOn(Schedulers.elastic())
				.subscribe(c -> log.info("Fetch select() {}", c));          //订阅：打印结果

		log.info("After Starting.");
		cdl.await();
	}
}
