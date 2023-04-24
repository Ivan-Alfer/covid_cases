package com.covid.covid_cases.controller;

import com.covid.covid_cases.model.CovidData;
import com.covid.covid_cases.model.CovidDataRequest;
import com.covid.covid_cases.model.CovidDataResponse;
import com.covid.covid_cases.service.CovidDataService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/covid-data")
@RequiredArgsConstructor
public class CovidDataController {

    private final CovidDataService covidDataService;

    @Operation(description = "Api return the maximum and minimum number of new cases per day during the selected day/period")
    @GetMapping
    public CovidDataResponse getCovidData(@Valid CovidDataRequest covidDataRequest) {
        if (covidDataRequest.getDateFrom() == null) {
            covidDataRequest.setDateFrom(LocalDate.now().minusDays(1));
        }
        if (covidDataRequest.getDateTo() == null) {
            covidDataRequest.setDateTo(LocalDate.now());
        }
        List<CovidData> covidData = covidDataService.fetchCovidDataByCountryAndDateRange(
                covidDataRequest.getCountries(),
                covidDataRequest.getDateFrom(),
                covidDataRequest.getDateTo()
        );
        return new CovidDataResponse(covidData);
    }

}
