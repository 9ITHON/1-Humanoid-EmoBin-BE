package com.humanoid.emobin.application.analysis;
//비즈니스 로직 서비스 (분석 → 저장 → 응답 생성)
import com.humanoid.emobin.application.analysis.dto.EmotionAnalysisCommand;
import com.humanoid.emobin.application.analysis.dto.EmotionAnalysisResult;
import com.humanoid.emobin.domain.analysis.EmotionAnalysis;
import com.humanoid.emobin.infrastructure.openai.OpenAiClient;
import com.humanoid.emobin.infrastructure.openai.OpenAiResponseParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    private final OpenAiClient openAiClient;
    private final OpenAiResponseParser openAiResponseParser;
    private final EmotionAnalysisRepository emotionAnalysisRepository;

    public EmotionAnalysisResult analyze(EmotionAnalysisCommand command) throws IOException {

        // 1. 감정 분석 실행 (Python)
        String resultText = openAiClient.analyzeEmotion(command.getText());

        // 2. 파싱 → 도메인 모델
        EmotionAnalysis analysis = openAiResponseParser.parse(resultText);

        // 3. DB 저장용 엔티티 변환
        EmotionAnalysisEntity entity = EmotionAnalysisEntity.from(
                command.getmemberId(),
                analysis.getEmotion(),
                analysis.getCauses(),
                analysis.getEmotionDepth(),
                analysis.getTemperature(),
                analysis.getMessage(),
                LocalDateTime.now()
        );

        emotionAnalysisRepository.save(entity);

        // 4. 결과 반환
        return new EmotionAnalysisResult(
                analysis.getEmotion(),
                analysis.getCauses(),
                analysis.getEmotionDepth(),
                analysis.getTemperature(),
                analysis.getMessage()
        );
    }
}