package com.humanoid.emobin.infrastructure.openai;
//감정, 원인, 깊이 파싱 로직

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.humanoid.emobin.domain.analysis.EmotionCategory;
import com.humanoid.emobin.domain.analysis.EmotionCauseType;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

public class OpenAiResponseParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmotionAnalysisResult parse(String json) throws IOException {
        JsonNode root = objectMapper.readTree(json);

        // 예: "슬픔(Sadness)" → "슬픔"
        String emotionRaw = root.get("emotion").asText();
        String baseEmotion = extractBaseEmotion(emotionRaw);
        String message = root.get("message").asText();
        EmotionCategory emotion = EmotionCategory.fromLabel(baseEmotion);

        List<EmotionCauseType> causes = Arrays.stream(root.get("causes")
                        .findValuesAsText(""))
                .map(EmotionCauseType::fromLabel)
                .collect(Collectors.toList());

        double depth = root.get("emotionDepth").asDouble();
        double temperature = root.get("temperature").asDouble();

        return new EmotionAnalysisResult(
                nickname,
                emotion,
                causes,
                message,
                daily_temperature,
                monthly_temperature);
    }

    private String extractBaseEmotion(String label) {
        int idx = label.indexOf("(");
        return idx != -1 ? label.substring(0, idx) : label;
    }
}
