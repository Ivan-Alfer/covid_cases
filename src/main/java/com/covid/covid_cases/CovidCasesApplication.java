package com.covid.covid_cases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CovidCasesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidCasesApplication.class, args);
	}

}
