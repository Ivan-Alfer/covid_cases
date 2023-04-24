package com.covid.covid_cases.service;

import com.covid.covid_cases.config.CovidApiProperties;
import com.covid.covid_cases.model.CovidDailyReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CovidApiService {

    private final RestTemplate restTemplate;

    private final CovidApiProperties covidApiProperties;

    private static final String COUNTRY_URI = "/country/{country}/status/confirmed";

    @Cacheable(value = "covidDataCache", key = "{#country, #start, #end}")
    public CovidDailyReport[] fetchCovidDailyReportsByCountryAndDate(String country, LocalDate start, LocalDate end) {
        log.info("Fetching COVID-19 data from API server for countries: {} from {} to {}", country, start, end);
        String url = covidApiProperties.getBaseUrl() + COUNTRY_URI;
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("from", start.toString())
                .queryParam("to", end.toString())
                .build(country);
        ResponseEntity<CovidDailyReport[]> response = restTemplate.getForEntity(uri, CovidDailyReport[].class);
        return response.getBody();
    }

}
