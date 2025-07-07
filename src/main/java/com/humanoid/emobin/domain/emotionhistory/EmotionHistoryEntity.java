package com.humanoid.emobin.domain.emotionhistory;

import com.humanoid.emobin.domain.member.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String emotion; // 예: 슬픔(Sadness)

    private LocalDateTime createdAt;

    public static EmotionHistoryEntity create(Member member, String emotion) {
        return EmotionHistoryEntity.builder()
                .member(member)
                .emotion(emotion)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
