package com.humanoid.emobin.infrastructure.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanoid.emobin.domain.emotionAnalysis.entity.EmotionAnalysis;
import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.response.EmotionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiResponseParser {

    private final ObjectMapper objectMapper;

    public EmotionAnalysis parse(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);

            String emotion = root.path("emotion").asText(null);
            if (emotion == null || emotion.isBlank()) {
                emotion = "보통";
            }
            String message = root.path("message").asText("");

            List<String> causes = objectMapper.convertValue(
                    root.path("causes"),
                    new TypeReference<List<String>>() {}
            );
            if (causes == null || causes.isEmpty()) {
                causes = List.of("명확한 원인 분석 실패");
            }

            List<String> descriptions = objectMapper.convertValue(
                    root.path("causeDescriptions"),
                    new TypeReference<List<String>>() {}
            );
            if (descriptions == null || descriptions.isEmpty()) {
                descriptions = List.of("명확한 원인 분석 실패");
            }

            if (causes.size() != descriptions.size()) {
                causes = List.of("명확한 원인 분석 실패");
                descriptions = List.of("");
            }
            double depth = root.path("emotionDepth").asDouble(0.0);
            double temperature = root.path("temperature").asDouble(0.0);

            return new EmotionAnalysis(
                    emotion,
                    causes,
                    depth,
                    temperature,
                    descriptions,
                    message
            );
        } catch (CustomException e) {
            throw e; // 이미 커스텀 예외는 재던짐
        } catch (Exception e) {
            throw new CustomException(EmotionErrorCode.EMOTION_ANALYSIS_FAILED);
        }
    }
}