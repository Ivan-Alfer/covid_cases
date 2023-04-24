package com.covid.covid_cases.service;

import com.covid.covid_cases.model.CovidDailyReport;
import com.covid.covid_cases.model.CovidData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CovidDataService {

    private final CovidApiService covidApiService;

    public List<CovidData> fetchCovidDataByCountryAndDateRange(List<String> countries, LocalDate start, LocalDate end) {
        return countries.stream()
                .map(country -> {
                    CovidDailyReport[] response = covidApiService.fetchCovidDailyReportsByCountryAndDate(country, start, end);
                    return calculateMinMaxCases(country, Arrays.asList(response));
                })
                .toList();
    }

    private CovidData calculateMinMaxCases(String country, List<CovidDailyReport> reports) {
        if (reports == null || reports.isEmpty()) {
            return new CovidData(country, 0, 0);
        }

        Collector<Integer, int[], CovidData> collector = Collector.of(
                () -> new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE},
                (result, newCases) -> {
                    result[0] = Math.max(result[0], newCases);
                    result[1] = Math.min(result[1], newCases);
                },
                (result1, result2) -> {
                    result1[0] = Math.max(result1[0], result2[0]);
                    result1[1] = Math.min(result1[1], result2[1]);
                    return result1;
                },
                result -> new CovidData(country, result[0], result[1])
        );

        return IntStream.range(1, reports.size())
                .map(i -> reports.get(i).cases() - reports.get(i - 1).cases())
                .boxed()
                .collect(collector);
    }

}
