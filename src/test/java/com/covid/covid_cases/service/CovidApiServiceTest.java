package com.covid.covid_cases.service;

import com.covid.covid_cases.config.CacheTestConfiguration;
import com.covid.covid_cases.config.CovidApiProperties;
import com.covid.covid_cases.model.CovidDailyReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CovidApiService.class)
@ContextConfiguration(classes = CacheTestConfiguration.class)
public class CovidApiServiceTest {

    @Autowired
    private CovidApiService covidApiService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private CovidApiProperties covidApiProperties;

    private CovidDailyReport[] sampleReports;

    @BeforeEach
    public void setUp() {
        sampleReports = new CovidDailyReport[]{
                new CovidDailyReport("Poland", LocalDate.of(2023, 3, 1), 100),
                new CovidDailyReport("Poland", LocalDate.of(2023, 3, 2), 200),
                new CovidDailyReport("Poland", LocalDate.of(2023, 3, 3), 300)
        };
    }

    @Test
    public void fetchCovidDailyReportsByCountryAndDateTest() {
        when(restTemplate.getForEntity(any(), any())).thenReturn(new ResponseEntity<>(sampleReports, HttpStatus.OK));

        CovidDailyReport[] response = covidApiService.fetchCovidDailyReportsByCountryAndDate(
                "Poland",
                LocalDate.of(2023, 3, 1),
                LocalDate.of(2023, 3, 3)
        );
        assertEquals(sampleReports.length, response.length);
        assertEquals(sampleReports[0].cases(), response[0].cases());
    }

    @Test
    public void testCache() {
        when(restTemplate.getForEntity(any(), any())).thenReturn(ResponseEntity.ok(sampleReports));

        // First API call
        CovidDailyReport[] response1 = covidApiService.fetchCovidDailyReportsByCountryAndDate(
                "Poland",
                LocalDate.of(2023, 3, 1),
                LocalDate.of(2023, 3, 3)
        );

        // Second API call (should return cached data)
        CovidDailyReport[] response2 = covidApiService.fetchCovidDailyReportsByCountryAndDate(
                "Poland",
                LocalDate.of(2023, 3, 1),
                LocalDate.of(2023, 3, 3)
        );
        assertArrayEquals(response1, response2);

        // Verify that the API was only called once
        verify(restTemplate, times(1)).getForEntity(any(), any());
    }
}
