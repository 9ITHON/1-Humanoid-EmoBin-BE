package com.humanoid.emobin.application.analysis.dto;


import lombok.Getter;

@Getter
public class EmotionAnalysisCommand {
    //유저 ID
    private Long userId;
    //분석할 텍스트
    private String text;

    public EmotionAnalysisCommand(Long userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    // 기본 생성자 (JSON 역직렬화용)
    protected EmotionAnalysisCommand() {}
}

