package com.springer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource("classpath:spring/container/spring-config.xml")
@ComponentScan
public class ExeciseApplication {
    private static ClassPathXmlApplicationContext appContext = null;

	public static void main(String[] args) {
		SpringApplication.run(ExeciseApplication.class, args);
	}
}
