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


     /*
     操作： Publisher(发布/生产者) / Subscriber（订阅/消费者）
     集合序列：Flux [ 0..N ]  0或N个元素  Mono [ 0..1 ] 0或1个元素
     遇到下一个元素时的操作： onNext()
     序列完成时的操作：      onComplete()
     发生错误时的操作：      onError()
     */

    /*
    Backpressure
    Subscription
    onRequest()、onCancel()、onDispose() 设置请求个数，取消，终止订阅过程
    */

    /*
    线程调度 Schedulers
    线程操作： immediate() / single() / newSingle()        当前线程/复用线程/新启动线程
    线程池操作：elastic() / parallel() / newParallel()     缓存池
    错误处理
    onError / onErrorReturn / onErrorResume              相当于try catch/遇到异常返回默认值/用特定lambda表达式进行异常处理
    doOnError / doFinally                                发生错误时执行/相当于finally
    */

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //创建1到6的序列,如果不指定任何线程信息，默认都在主线程main上执行
        Flux.range(1, 6)
                //只请求了4个
                //.publishOn(Schedulers.elastic())

                .doOnRequest(n -> log.info("Request {} number", n))                     //打印请求数和订阅线程 注意顺序造成的区别
				.publishOn(Schedulers.elastic())                                        //设置发布线程，消息发布设置在elastic线程池

                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))                   //序列1到6请求完成后打印Publisher COMPLETE 1

                //map实现元素转换（映射）
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);              //打印当前线程信息
                    //return 10 / (i - 3);                                              //抛出异常测试
					return i;
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2"))
				.subscribeOn(Schedulers.single())                                       //设置订阅线程，单独的线程进行订阅

                //抛出异常时，执行特定的lambda表达式
                /*.onErrorResume(e -> {
					log.error("Exception {}", e.toString());
					return Mono.just(-1);
				})*/

                //当异常发生时，返回指定的默认值并终止后续操作
				.onErrorReturn(-1)

                //订阅(订阅发生之前什么都不会做)
                .subscribe(  //传入consumer
                        i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),   //对元素进行消费
                        e -> log.error("error {}", e.toString()),                       //对异常信息进行消费
                        () -> log.info("Subscriber COMPLETE")                           //订阅结束后的处理
						//s -> s.request(4)                                             //设置请求元素的个数，只取回4个请求,多余的未被发布出来 Publisher COMPLETE不输出
                );
        Thread.sleep(2000);
    }
}
