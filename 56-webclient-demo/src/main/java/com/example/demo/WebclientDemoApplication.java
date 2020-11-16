package com.example.demo;

import com.example.demo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class WebclientDemoApplication implements ApplicationRunner {
	@Autowired
	private WebClient webClient;

	public static void main(String[] args) {
		new SpringApplicationBuilder(WebclientDemoApplication.class)
				.web(WebApplicationType.NONE)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

	/**
	 * WebClient构造
	 * @param builder
	 * @return
	 */
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		//host构造
		return builder.baseUrl("http://localhost:8080").build();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		CountDownLatch cdl = new CountDownLatch(2);


		webClient.get()											//get请求
				.uri("/coffee/{id}", 1)				//uri和参数
				.accept(MediaType.APPLICATION_JSON_UTF8)		//响应类型为JSON_UTF8
				.retrieve()										//获取响应结果
				.bodyToMono(Coffee.class)						//响应正文转换为coffee类型
				.doOnError(t -> log.error("Error: ", t))		//出错回调
				.doFinally(s -> cdl.countDown())				//操作结束回调：countDown主线程等待异步请求处理完成
				.subscribeOn(Schedulers.single())				//single线程中订阅
				.subscribe(c -> log.info("Coffee 1: {}", c));

		//构造请求正文
		Mono<Coffee> americano = Mono.just(
				Coffee.builder()
						.name("americano")
						.price(Money.of(CurrencyUnit.of("CNY"), 25.00))
						.build()
		);


		webClient.post()												//post请求
				.uri("/coffee/")										//uri
				.body(americano, Coffee.class)							//请求正文
				.retrieve()												//获取响应结果
				.bodyToMono(Coffee.class)								//响应正文转换为coffee类型
				.doFinally(s -> cdl.countDown())						//操作结束回调：countDown主线程等待异步请求处理完成
				.subscribeOn(Schedulers.single())						//single线程中订阅
				.subscribe(c -> log.info("Coffee Created: {}", c));

		cdl.await();

		webClient.get()											//get请求
				.uri("/coffee/")								//uri
				.retrieve()										//获取响应结果
				.bodyToFlux(Coffee.class)						//返回多个对象类型（list）
				.toStream()										//Flux转Stream并遍历打印
				.forEach(c -> log.info("Coffee in List: {}", c));
	}
}
