package com.humanoid.emobin.application.analysis.dto;


import lombok.Getter;

@Getter
public class EmotionAnalysisCommand {

    private String text;

    // 클라이언트에서 JSON으로 받을 생성자
    protected EmotionAnalysisCommand() {}

    public EmotionAnalysisCommand(String text) {
        this.text = text;
    }
}
