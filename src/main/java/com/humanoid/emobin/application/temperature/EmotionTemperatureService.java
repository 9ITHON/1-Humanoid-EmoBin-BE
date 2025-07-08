package com.humanoid.emobin.application.temperature;

import com.humanoid.emobin.application.temperature.dto.MonthDaySummaryResponse;
import com.humanoid.emobin.domain.emotionTemperature.dailySummary.DailySummaryEntity;
import com.humanoid.emobin.domain.emotionTemperature.dailySummary.repository.DailySummaryRepository;
import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.MonthlySummaryEntity;
import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.MonthlySummaryRepository;
import com.humanoid.emobin.domain.member.Member;
import com.humanoid.emobin.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.humanoid.emobin.application.temperature.dto.MonthDaySummaryResponse.*;

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

        List<DailySummaryDto> dailyDtoList = dailySummaries.stream()
                .map(e -> new DailySummaryDto(e.getLocalDate(), e.getDailyTemperature()))
                .toList();

        return MonthDaySummaryResponse.builder()
                .month(yearMonthStr)
                .monthlyTemperature(monthlySummary != null ? monthlySummary.getMonthlyTemperature() : 0.0)
                .dailySummaries(dailyDtoList)
                .build();
    }
}