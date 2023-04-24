package com.covid.covid_cases.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CovidDataResponse {
    private List<CovidData> covidDataList;
}
