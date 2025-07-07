package com.humanoid.emobin.presentation.temperature;

import com.humanoid.emobin.application.temperature.EmotionTemperatureService;
import com.humanoid.emobin.application.temperature.dto.MonthDaySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/et")
@RequiredArgsConstructor
public class EmotionTemperatureController {

    private final EmotionTemperatureService emotionTemperatureService;

    // GET /api/et/monthDay?date=2024-03
    @GetMapping("/monthDay")
    public ResponseEntity<MonthDaySummaryResponse> getMonthDaySummary(
            @AuthenticationPrincipal Long memberId,
            @RequestParam("date") String yearMonthStr
    ) {
        MonthDaySummaryResponse result = emotionTemperatureService.getMonthAndDaySummary(memberId, yearMonthStr);
        return ResponseEntity.ok(result);
    }
}