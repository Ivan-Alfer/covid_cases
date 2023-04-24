package com.covid.covid_cases.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record CovidDailyReport(
        @JsonProperty("Country") String country,
        @JsonProperty("Date") LocalDate date,
        @JsonProperty("Cases") int cases
) {}
