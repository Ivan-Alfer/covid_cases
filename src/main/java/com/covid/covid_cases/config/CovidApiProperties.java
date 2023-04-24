package com.covid.covid_cases.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "covid-api")
@Data
public class CovidApiProperties {
    private String baseUrl;
}
