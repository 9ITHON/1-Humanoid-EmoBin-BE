package com.humanoid.emobin.application.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
//유저 id와 텍스트를 통해 얻은 감정 분석 결과를 담는 DTO
public class EmotionAnalysisResult {
    //유저 이름
    private String nickname;
    //유저의 주 감정
    private String emotion;
    //감정의 원인 2가지
    private List<String> causes;
    //한마디
    private String message;
    //해당날의 감정온도
    private double daily_temperature;
    //해당달의 감정온도
    private double monthly_temperature;

    public static EmotionAnalysisResult of(
            String nickname,
            String emotion,
            List<String> causes,
            String message,
            double daily_temperature,
            double monthly_temperature
    ) {
        return new EmotionAnalysisResult(
                nickname,
                emotion,
                causes,
                message,
                daily_temperature,
                monthly_temperature
        );
    }
}