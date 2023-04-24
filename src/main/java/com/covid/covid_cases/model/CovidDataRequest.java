package com.covid.covid_cases.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class CovidDataRequest {

    @NotEmpty
    public List<String> countries;
    public LocalDate dateFrom;
    public LocalDate dateTo;

}
