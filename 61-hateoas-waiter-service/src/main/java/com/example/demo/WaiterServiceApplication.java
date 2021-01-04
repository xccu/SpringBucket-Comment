package com.example.demo;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class WaiterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaiterServiceApplication.class, args);
	}

	/**
	 * Hibernate5支持
	 * @return
	 */
	@Bean
	public Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}

	/**
	 * 定制jacksonBuilder
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonBuilderCustomizer() {
		return builder -> {
			//输出缩进的json
			builder.indentOutput(true);
			//设置时区为上海
			builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		};
	}
}
