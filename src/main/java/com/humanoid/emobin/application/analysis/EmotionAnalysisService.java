package com.humanoid.emobin.application.analysis;

import com.humanoid.emobin.application.analysis.dto.EmotionAnalysisCommand;
import com.humanoid.emobin.domain.analysis.EmotionAnalysis;
import com.humanoid.emobin.domain.emotioncauses.EmotionCauseEntity;
import com.humanoid.emobin.domain.emotioncauses.EmotionCauseRepository;
import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryEntity;
import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryRepository;
import com.humanoid.emobin.domain.member.Member;
import com.humanoid.emobin.domain.member.MemberRepository;
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
    private final MemberRepository memberRepository;


    public EmotionAnalysis analyzeRaw(Long memberId, EmotionAnalysisCommand command) throws IOException {
        String rawResult = openAiClient.analyzeEmotion(command.getText());
        EmotionAnalysis analysis = openAiResponseParser.parse(rawResult);

        // 감정 이력 및 원인 저장 (기존과 동일)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID=" + memberId));

        EmotionHistoryEntity history = EmotionHistoryEntity.create(member, analysis.getEmotion());
        emotionHistoryRepository.save(history);

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

        return analysis; // 온도 포함된 분석 결과 그대로 반환
    }

}
