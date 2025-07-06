package com.humanoid.emobin.application.analysis;

import com.humanoid.emobin.application.analysis.dto.EmotionAnalysisCommand;
import com.humanoid.emobin.application.analysis.dto.EmotionAnalysisResult;
import com.humanoid.emobin.domain.analysis.EmotionAnalysis;
import com.humanoid.emobin.domain.emotioncauses.EmotionCauseEntity;
import com.humanoid.emobin.domain.emotioncauses.EmotionCauseRepository;
import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryEntity;
import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryRepository;
import com.humanoid.emobin.domain.member.MemberRepository;
import com.humanoid.emobin.domain.member.MemberService;
import com.humanoid.emobin.infrastructure.openai.OpenAiClient;
import com.humanoid.emobin.infrastructure.openai.OpenAiResponseParser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//비즈니스 로직 서비스 (분석 → 저장 → 응답 생성)
@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    private final OpenAiClient openAiClient;
    private final OpenAiResponseParser openAiResponseParser;
    private final EmotionHistoryRepository emotionHistoryRepository;
    private final EmotionCauseRepository emotionCauseRepository;
    private final MemberService memberService;

    public EmotionAnalysisResult analyze(Long memberId, EmotionAnalysisCommand command) throws IOException {

        // 1. 분석 실행 (Python)
        String rawResult = openAiClient.analyzeEmotion(command.getText());

        // 2. 결과 파싱
        EmotionAnalysis analysis = openAiResponseParser.parse(rawResult);

        // 3. emotion_history 저장 (memberId는 JWT에서 받은 것 사용)
        EmotionHistoryEntity history = EmotionHistoryEntity.create(
                memberId,
                analysis.getEmotion()
        );
        emotionHistoryRepository.save(history);

        // 4. emotion_causes 저장
        List<String> causes = analysis.getCauses();
        List<String> descriptions = analysis.getCauseDescriptions();

        List<EmotionCauseEntity> causeEntities = new ArrayList<>();
        for (int i = 0; i < causes.size(); i++) {
            EmotionCauseEntity causeEntity = EmotionCauseEntity.of(
                    causes.get(i),
                    descriptions.get(i),
                    history
            );
            causeEntities.add(causeEntity);
        }

        emotionCauseRepository.saveAll(causeEntities);

        // 5. 닉네임 조회 (memberId로)
        String nickname = memberService.getNickname(memberId);

        // 6. 응답 반환
        return new EmotionAnalysisResult(
                nickname,
                analysis.getEmotion(),
                analysis.getCauses(),
                analysis.getMessage(),
                0.0, // dailyTemperature (임시)
                0.0  // monthlyTemperature (임시)
        );
    }
}
