package com.humanoid.emobin.domain.emotionTemperature;

import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.dto.MonthDaySummaryResponse;
import com.humanoid.emobin.domain.emotionTemperature.dailySummary.DailySummaryEntity;
import com.humanoid.emobin.domain.emotionTemperature.dailySummary.repository.DailySummaryRepository;
import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.MonthlySummaryEntity;
import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.MonthlySummaryRepository;
import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionTemperatureService {

    private final MemberRepository memberRepository;
    private final DailySummaryRepository dailySummaryRepository;
    private final MonthlySummaryRepository monthlySummaryRepository;

    public MonthDaySummaryResponse getMonthAndDaySummary(Long memberId, String yearMonthStr) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        LocalDate monthStart = LocalDate.parse(yearMonthStr + "-01");
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        // 월간 요약
        MonthlySummaryEntity monthlySummary = monthlySummaryRepository
                .findByMemberAndMonth(member, monthStart)
                .orElse(null); // 없으면 null

        // 일간 요약
        List<DailySummaryEntity> dailySummaries = dailySummaryRepository.findByMemberAndLocalDateBetween(
                member, monthStart, monthEnd
        );

        List<MonthDaySummaryResponse.DailySummaryDto> dailyDtoList = dailySummaries.stream()
                .map(e -> new MonthDaySummaryResponse.DailySummaryDto(
                        e.getLocalDate(),
                        Math.round(e.getDailyTemperature() * 10.0) / 10.0  // 🔧 소수점 1자리 반올림
                ))
                .toList();

        return MonthDaySummaryResponse.builder()
                .month(yearMonthStr)
                .monthlyTemperature(monthlySummary != null ? monthlySummary.getMonthlyTemperature() : 0.0)
                .dailySummaries(dailyDtoList)
                .build();
    }
}