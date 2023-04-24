package com.covid.covid_cases.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CovidData {
    private String country;
    private int maxCases;
    private int minCases;
}
