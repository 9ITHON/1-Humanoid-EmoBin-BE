package com.humanoid.emobin.infrastructure.openai;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanoid.emobin.domain.analysis.EmotionAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiResponseParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmotionAnalysis parse(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);

            String emotion = root.path("emotion").asText(null);

            // causes
            JsonNode causesNode = root.path("causes");
            List<String> causes = objectMapper.convertValue(causesNode, List.class);

            // causeDescriptions
            JsonNode descriptionsNode = root.path("causeDescriptions");
            List<String> causeDescriptions = objectMapper.convertValue(descriptionsNode, List.class);

            double depth = root.path("emotionDepth").asDouble(0.0);
            double temperature = root.path("temperature").asDouble(0.0);
            String message = root.path("message").asText("");

            return new EmotionAnalysis(
                    emotion,
                    causes,
                    depth,
                    temperature,
                    causeDescriptions,
                    message
            );
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + e.getMessage());
        }
    }
}