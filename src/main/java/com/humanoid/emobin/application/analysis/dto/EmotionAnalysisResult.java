package com.humanoid.emobin.application.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 프론트에 전달될 감정 분석 결과 DTO
public class EmotionAnalysisResult {

    // 유저 닉네임
    private String nickname;

    // 유저의 주 감정
    private String emotion;

    // 감정의 원인 2가지
    private List<String> causes;

    // 사용자에게 전달할 한마디 메시지
    private String message;

    public static EmotionAnalysisResult of(
            String nickname,
            String emotion,
            List<String> causes,
            String message
    ) {
        return new EmotionAnalysisResult(
                nickname,
                emotion,
                causes,
                message
        );
    }
}
