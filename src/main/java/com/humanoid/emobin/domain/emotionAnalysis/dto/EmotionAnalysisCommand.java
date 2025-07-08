package com.humanoid.emobin.domain.emotionAnalysis.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmotionAnalysisCommand {

    @NotBlank(message = "분석할 텍스트는 비어 있을 수 없습니다.")
    private String text;

    protected EmotionAnalysisCommand() {}

    public EmotionAnalysisCommand(String text) {
        this.text = text;
    }
}
