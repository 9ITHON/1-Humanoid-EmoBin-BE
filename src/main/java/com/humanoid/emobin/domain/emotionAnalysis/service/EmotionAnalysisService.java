package com.humanoid.emobin.domain.emotionAnalysis.service;

import com.humanoid.emobin.domain.emotionAnalysis.entity.EmotionAnalysis;
import com.humanoid.emobin.domain.emotionAnalysis.dto.EmotionAnalysisCommand;
import com.humanoid.emobin.domain.emotioncauses.EmotionCauseEntity;
import com.humanoid.emobin.domain.emotioncauses.EmotionCauseRepository;
import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryEntity;
import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryRepository;
import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.repository.MemberRepository;


import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.response.EmotionErrorCode;
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
        if (analysis == null || analysis.getEmotion() == null || analysis.getCauses() == null) {
            throw new CustomException(EmotionErrorCode.NULL_ANALYSIS_RESULT);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(EmotionErrorCode.MEMBER_DATA_NOT_FOUND));

        EmotionHistoryEntity history = EmotionHistoryEntity.create(member, analysis.getEmotion());
        emotionHistoryRepository.save(history);

        List<String> causes = analysis.getCauses();
        List<String> descriptions = analysis.getCauseDescriptions();
        List<EmotionCauseEntity> causeEntities = new ArrayList<>();
        if (causes.size() != descriptions.size()) {
            throw new CustomException(EmotionErrorCode.INVALID_RESPONSE_FROM_AI);
        }

        for (int i = 0; i < causes.size(); i++) {
            EmotionCauseEntity causeEntity = EmotionCauseEntity.of(
                    causes.get(i),
                    descriptions.get(i),
                    history
            );
            causeEntities.add(causeEntity);
        }
        emotionCauseRepository.saveAll(causeEntities);

        return analysis;
    }

}
