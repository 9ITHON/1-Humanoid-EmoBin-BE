package com.humanoid.emobin.domain.analysis;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EmotionAnalysis {
    private String emotion;
    private List<String> causes; // 원인 2개
    private double emotionDepth;
    private double temperature;
    private List<String> causeDescriptions;
    private String message;
}
