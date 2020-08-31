package com.example.context;

import com.example.foo.FooConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
@Slf4j
public class ContextHierarchyDemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ContextHierarchyDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//AnnotationConfigApplicationContext加载FooConfig配置类
		ApplicationContext fooContext = new AnnotationConfigApplicationContext(FooConfig.class);

		//ClassPathXmlApplicationContext加载applicationContext.xml文件
		//父级别ApplicationContext为fooContext
		ClassPathXmlApplicationContext barContext = new ClassPathXmlApplicationContext(
				new String[] {"applicationContext.xml"}, fooContext);

		//通过fooContext获取bean
		TestBean bean = fooContext.getBean("testBeanX", TestBean.class);

		//hello foo
		bean.hello();
		log.info("=============");

		bean = barContext.getBean("testBeanX", TestBean.class);
		//hello Bar
		bean.hello();

		bean = barContext.getBean("testBeanY", TestBean.class);
		//hello foo
		bean.hello();
	}
}
