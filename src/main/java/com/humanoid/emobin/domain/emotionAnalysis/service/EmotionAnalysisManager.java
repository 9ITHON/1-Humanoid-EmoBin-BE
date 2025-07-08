package com.humanoid.emobin.domain.emotionAnalysis.service;


import com.humanoid.emobin.domain.emotionAnalysis.dto.EmotionAnalysisCommand;
import com.humanoid.emobin.domain.emotionAnalysis.dto.EmotionAnalysisResult;
import com.humanoid.emobin.domain.emotionAnalysis.entity.EmotionAnalysis;
import com.humanoid.emobin.domain.emotionTemperature.dailySummary.service.DailySummaryService;
import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisManager {

    private final EmotionAnalysisService emotionAnalysisService;
    private final DailySummaryService dailySummaryService;
    private final MemberRepository memberRepository;

    public EmotionAnalysisResult analyze(Long memberId, EmotionAnalysisCommand command) throws IOException {
        // 1. 감정 분석 수행 (온도 포함)
        EmotionAnalysis analysis = emotionAnalysisService.analyzeRaw(memberId, command);

        // 2. Member 조회 (온도 저장용)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 3. 일간 감정 온도 저장
        dailySummaryService.updateOrCreate(member, analysis.getTemperature(), LocalDate.now());

        // 4. 응답 생성 및 반환
        return new EmotionAnalysisResult(
                member.getNickname(),
                analysis.getEmotion(),
                analysis.getCauses(),
                analysis.getMessage()
        );
    }
}