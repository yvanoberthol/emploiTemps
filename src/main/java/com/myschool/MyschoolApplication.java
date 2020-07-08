package com.myschool;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyschoolApplication {

	@Autowired
	private com.myschool.InitialDataSet initialDataSet;

	public static void main(String[] args) {
		SpringApplication.run(MyschoolApplication.class, args);
	}

	@Bean
	InitializingBean init() {
		return initialDataSet.load();
	}

}
