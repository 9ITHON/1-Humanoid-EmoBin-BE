package com.humanoid.emobin.infrastructure.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanoid.emobin.domain.emotionAnalysis.entity.EmotionAnalysis;
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
            String message = root.path("message").asText("");

            List<String> causes = objectMapper.convertValue(
                    root.path("causes"),
                    new TypeReference<List<String>>() {}
            );

            List<String> descriptions = objectMapper.convertValue(
                    root.path("causeDescriptions"),
                    new TypeReference<List<String>>() {}
            );

            if (causes.size() != descriptions.size()) {
                throw new IllegalArgumentException("원인 개수와 설명 개수가 다릅니다.");
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
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + e.getMessage() + "\n원본: " + json, e);
        }
    }
}