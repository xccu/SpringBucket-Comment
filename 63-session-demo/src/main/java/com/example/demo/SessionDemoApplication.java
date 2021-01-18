package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@SpringBootApplication
@RestController
@EnableRedisHttpSession //启用RedisHttpSession注解，将session保存在redis中
public class SessionDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionDemoApplication.class, args);
	}

	//http://localhost:8080/hello?name=spring
	@RequestMapping("/hello")
	public String printSession(HttpSession session, String name) { //从request中取得一个session
		//获取session：name
		String storedName = (String) session.getAttribute("name");

		//判断session是否有做过设置,没有则创建
		if (storedName == null) {
			//设置session并保存在redis中，服务器进程重启后，session依然存在
			session.setAttribute("name", name);
			storedName = name;
		}
		return "hello " + storedName;
	}

}
