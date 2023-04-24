package com.covid.covid_cases.service;

import com.covid.covid_cases.model.CovidDailyReport;
import com.covid.covid_cases.model.CovidData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CovidDataServiceTest {

    @Autowired
    private CovidDataService covidDataService;

    @MockBean
    private CovidApiService covidApiService;

    private CovidDailyReport[] usSampleReports;
    private CovidDailyReport[] plSampleReports;

    @BeforeEach
    public void setUp() {
        usSampleReports = new CovidDailyReport[]{
                new CovidDailyReport("US", LocalDate.of(2023, 3, 1), 100),
                new CovidDailyReport("US", LocalDate.of(2023, 3, 2), 300),
                new CovidDailyReport("US", LocalDate.of(2023, 3, 3), 400)
        };
        plSampleReports = new CovidDailyReport[]{
                new CovidDailyReport("UK", LocalDate.of(2023, 3, 1), 50),
                new CovidDailyReport("UK", LocalDate.of(2023, 3, 2), 80),
                new CovidDailyReport("UK", LocalDate.of(2023, 3, 3), 120)
        };
    }

    @Test
    public void fetchCovidDataByCountryAndDateRangeTest() {
        List<String> countries = List.of("US");
        LocalDate start = LocalDate.of(2023, 3, 1);
        LocalDate end = LocalDate.of(2023, 3, 3);

        when(covidApiService.fetchCovidDailyReportsByCountryAndDate("US", start, end)).thenReturn(usSampleReports);

        List<CovidData> covidData = covidDataService.fetchCovidDataByCountryAndDateRange(countries, start, end);

        assertEquals(1, covidData.size());
        assertEquals("US", covidData.get(0).getCountry());
        assertEquals(200, covidData.get(0).getMaxCases());
        assertEquals(100, covidData.get(0).getMinCases());
    }

    @Test
    public void fetchCovidDataByMultipleCountriesAndDateRangeTest() {
        List<String> countries = List.of("US", "PL");
        LocalDate start = LocalDate.of(2023, 3, 1);
        LocalDate end = LocalDate.of(2023, 3, 3);

        when(covidApiService.fetchCovidDailyReportsByCountryAndDate("US", start, end)).thenReturn(usSampleReports);
        when(covidApiService.fetchCovidDailyReportsByCountryAndDate("PL", start, end)).thenReturn(plSampleReports);

        List<CovidData> covidData = covidDataService.fetchCovidDataByCountryAndDateRange(countries, start, end);

        assertEquals(2, covidData.size());

        assertEquals("US", covidData.get(0).getCountry());
        assertEquals(200, covidData.get(0).getMaxCases());
        assertEquals(100, covidData.get(0).getMinCases());

        assertEquals("PL", covidData.get(1).getCountry());
        assertEquals(40, covidData.get(1).getMaxCases());
        assertEquals(30, covidData.get(1).getMinCases());
    }

    @Test
    public void fetchCovidDataWithNoCasesTest() {
        List<String> countries = List.of("US");
        LocalDate start = LocalDate.of(2023, 3, 1);
        LocalDate end = LocalDate.of(2023, 3, 3);

        when(covidApiService.fetchCovidDailyReportsByCountryAndDate("US", start, end)).thenReturn(new CovidDailyReport[0]);

        List<CovidData> covidData = covidDataService.fetchCovidDataByCountryAndDateRange(countries, start, end);

        assertEquals(1, covidData.size());
        assertEquals("US", covidData.get(0).getCountry());
        assertEquals(0, covidData.get(0).getMaxCases());
        assertEquals(0, covidData.get(0).getMinCases());
    }

    @Test
    public void fetchCovidDataWithEmptyCountryListTest() {
        List<String> countries = List.of();
        LocalDate start = LocalDate.of(2023, 3, 1);
        LocalDate end = LocalDate.of(2023, 3, 3);

        List<CovidData> covidData = covidDataService.fetchCovidDataByCountryAndDateRange(countries, start, end);

        assertEquals(0, covidData.size());
    }
}