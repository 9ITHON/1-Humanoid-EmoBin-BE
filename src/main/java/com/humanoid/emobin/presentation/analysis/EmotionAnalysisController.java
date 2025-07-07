package com.humanoid.emobin.presentation.analysis;
//REST API Controller
// /api/analysis

import com.humanoid.emobin.application.analysis.dto.EmotionAnalysisCommand;
import com.humanoid.emobin.application.analysis.dto.EmotionAnalysisResult;
import com.humanoid.emobin.domain.analysis.EmotionAnalysisManager;
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
    public ResponseEntity<EmotionAnalysisResult> analyzeEmotion(
            @AuthenticationPrincipal Long memberId,
            @RequestBody EmotionAnalysisCommand command
    ) throws IOException {
        EmotionAnalysisResult result = emotionAnalysisManager.analyze(memberId, command);
        return ResponseEntity.ok(result);
    }
}
