package com.humanoid.emobin.application.temperature.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MonthDaySummaryResponse {

    private String month; // YYYY-MM
    private double monthlyTemperature;
    private List<DailySummaryDto> dailySummaries;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DailySummaryDto {
        private LocalDate date;
        private double temperature;
    }
}