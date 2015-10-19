package com.naivebayes.application;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
@Configuration
@SpringBootApplication
@EnableJpaRepositories(basePackages={"com.naivebayes"})
@ComponentScan(basePackages={"com.naivebayes"})
@EnableCaching
@EnableScheduling
public class NaiveBayesApplication {

	public static void main(String[] args) {
		
		// mvn spring-boot:run
		// 	curl -X POST http://localhost:8080/naive/predict/24339557
		// mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
		ApplicationContext ctx = SpringApplication.run(NaiveBayesApplication.class, args);

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}
	
	

	@Bean
	public CacheManager cacheManager() {
		final GuavaCacheManager manager = new GuavaCacheManager("NaiveBayes");
		manager.setAllowNullValues(false);
		System.out.println("Cache Manger Initialized");
		return manager;
	}
	

}
