package com.humanoid.emobin.domain.emotionAnalysis.controller;
//REST API Controller
// /api/analysis

import com.humanoid.emobin.domain.emotionAnalysis.dto.EmotionAnalysisCommand;
import com.humanoid.emobin.domain.emotionAnalysis.dto.EmotionAnalysisResult;
import com.humanoid.emobin.domain.emotionAnalysis.service.EmotionAnalysisManager;
import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.response.ApiResponse;
import com.humanoid.emobin.global.response.EmotionErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class EmotionAnalysisController {

    private final EmotionAnalysisManager emotionAnalysisManager;

    @PostMapping
    public ResponseEntity<ApiResponse<EmotionAnalysisResult>> analyzeEmotion(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid EmotionAnalysisCommand command
    ) {
        try {
            EmotionAnalysisResult result = emotionAnalysisManager.analyze(memberId, command);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IOException e) {
            throw new CustomException(EmotionErrorCode.EMOTION_ANALYSIS_FAILED);
        }
    }
}
