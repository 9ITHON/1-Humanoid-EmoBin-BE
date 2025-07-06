package com.humanoid.emobin.application.analysis.dto;


import lombok.Getter;

@Getter
public class EmotionAnalysisCommand {
    //유저 ID
    private Long memberId;
    //분석할 텍스트
    private String text;

    public EmotionAnalysisCommand(Long memberId, String text) {
        this.memberId = memberId;
        this.text = text;
    }

    // 기본 생성자 (JSON 역직렬화용)
    protected EmotionAnalysisCommand() {}
}

