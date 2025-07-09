package com.humanoid.emobin.domain.emotionTemperature.controller;

import com.humanoid.emobin.domain.emotionTemperature.EmotionTemperatureService;
import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.dto.MonthDaySummaryResponse;
import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.response.ApiResponse;
import com.humanoid.emobin.global.response.EmotionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/emotion-temperature")
@RequiredArgsConstructor
public class EmotionTemperatureController {

    private final EmotionTemperatureService emotionTemperatureService;

    // GET /api/emotion-temperature/summary?month=2024-03
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<MonthDaySummaryResponse>> getMonthDaySummary(
            @AuthenticationPrincipal Long memberId,
            @RequestParam("month") String yearMonthStr
    ) {
        if (memberId == null || yearMonthStr == null || yearMonthStr.isBlank()) {
            throw new CustomException(EmotionErrorCode.INVALID_REQUEST);
        }
        MonthDaySummaryResponse result = emotionTemperatureService.getMonthAndDaySummary(memberId, yearMonthStr);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}