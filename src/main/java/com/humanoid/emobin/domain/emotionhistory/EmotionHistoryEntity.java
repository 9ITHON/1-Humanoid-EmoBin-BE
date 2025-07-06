package com.humanoid.emobin.domain.emotionhistory;

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
    private Long emotionHistoryId;

    private Long memberId;

    private String emotion; // 예: 슬픔(Sadness)

    private LocalDateTime createdAt;

    public static EmotionHistoryEntity create(Long memberId, String emotion) {
        return EmotionHistoryEntity.builder()
                .memberId(memberId)
                .emotion(emotion)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
