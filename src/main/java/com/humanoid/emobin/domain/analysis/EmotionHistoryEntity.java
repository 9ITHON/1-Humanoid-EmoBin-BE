package com.humanoid.emobin.domain.analysis;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmotionHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String emotion; // 예: "슬픔(Sadness)"

    private double emotionDepth;

    private double temperature;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;

    public static EmotionHistoryEntity from(
            Long memberId,
            String emotion,
            double depth,
            double temperature,
            String message,
            LocalDateTime createdAt
    ) {
        return EmotionHistoryEntity.builder()
                .memberId(memberId)
                .emotion(emotion)
                .emotionDepth(depth)
                .temperature(temperature)
                .message(message)
                .createdAt(createdAt)
                .build();
    }
}