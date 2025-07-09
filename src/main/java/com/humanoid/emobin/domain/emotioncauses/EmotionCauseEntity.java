package com.humanoid.emobin.domain.emotioncauses;
//감정 원인 enum 정의

import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmotionCauseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emotionCausesId;

    private String emotionCausesName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_history_id", nullable = false)
    private EmotionHistoryEntity emotionHistory;

    public static EmotionCauseEntity of(String name, String description, EmotionHistoryEntity history) {

        if (name == null || description == null || history == null) {
            throw new IllegalArgumentException("모든 인자는 필수입니다.");
        }

        return EmotionCauseEntity.builder()
                .emotionCausesName(name)
                .description(description)
                .emotionHistory(history)
                .build();
    }
}
