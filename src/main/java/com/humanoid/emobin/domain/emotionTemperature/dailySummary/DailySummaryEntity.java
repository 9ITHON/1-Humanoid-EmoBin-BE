package com.humanoid.emobin.domain.emotionTemperature.dailySummary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import com.humanoid.emobin.domain.member.Member;

@Entity
@Table(name = "daily_summary")
@Getter
@Setter
public class DailySummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailySummaryId;
    // 몇일 인지 기입
    private LocalDate localDate;
    // 해당 날짜의 감정 온도
    private double dailyTemperature;

    private Long dailyCount;

    // 연관된 멤버 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
