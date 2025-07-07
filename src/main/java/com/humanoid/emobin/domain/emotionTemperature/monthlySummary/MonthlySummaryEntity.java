package com.humanoid.emobin.domain.emotionTemperature.monthlySummary;

import com.humanoid.emobin.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "monthly_summary")
@Getter
@Setter
public class MonthlySummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthlySummaryId;
    // 연관된 멤버 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //몇월 인지 기입
    private LocalDate month;
    //해당 월의 감정온도 매월 1일 기준으로 계산
    //일별 감정온도 테이블의 SUM후에 SIZE로 나누어 계산
    private double monthlyTemperature;

}
