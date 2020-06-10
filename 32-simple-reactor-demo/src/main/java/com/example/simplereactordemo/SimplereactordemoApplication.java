package com.example.simplereactordemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@Slf4j
public class SimplereactordemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SimplereactordemoApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Flux.range(1, 6) //创建1到6的序列
                //.publishOn(Schedulers.elastic()) //只请求了4个
                .doOnRequest(n -> log.info("Request {} number", n)) //打印请求数 注意顺序造成的区别
				//.publishOn(Schedulers.elastic())
                .doOnComplete(() -> log.info("Publisher COMPLETE 1")) //序列1到6请求完成后打印Publisher COMPLETE 1
                .map(i -> {//打印当前线程信息
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    //return 10 / (i - 3); //抛出异常测试
					return i;
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2"))
				.subscribeOn(Schedulers.single())

                /*.onErrorResume(e -> {
					log.error("Exception {}", e.toString()); //抛出异常信息
					return Mono.just(-1);
				})*/

				.onErrorReturn(-1) //当异常发生时，返回指定的默认值并终止后续操作
                .subscribe( //订阅
                        i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()), //输出异常
                        () -> log.info("Subscriber COMPLETE"),
						s -> s.request(4) //只取回4个请求
                );
        Thread.sleep(2000);
    }
}
