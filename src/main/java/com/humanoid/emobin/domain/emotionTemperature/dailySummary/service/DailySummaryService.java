package com.humanoid.emobin.domain.emotionTemperature.dailySummary.service;


import com.humanoid.emobin.domain.emotionTemperature.dailySummary.DailySummaryEntity;
import com.humanoid.emobin.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.humanoid.emobin.domain.emotionTemperature.dailySummary.repository.DailySummaryRepository;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailySummaryService {

    private final DailySummaryRepository dailySummaryRepository;

    public void updateOrCreate(Member member, double analyzedTemperature, LocalDate date) {
        DailySummaryEntity summary = dailySummaryRepository
                .findByMemberAndLocalDate(member, date)
                .orElseGet(() -> {
                    // 기존 데이터가 없을 경우
                    DailySummaryEntity newSummary = new DailySummaryEntity();
                    newSummary.setMember(member);
                    newSummary.setLocalDate(date);
                    newSummary.setDailyTemperature(36.5 + analyzedTemperature);
                    newSummary.setDailyCount(1L);
                    return newSummary;
                });

        // 기존 데이터가 존재하면 → 평균 계산 방식 적용
        if (summary.getDailyCount() != null && summary.getDailyCount() > 0) {
            long count = summary.getDailyCount();
            double previousTotal = summary.getDailyTemperature() * count;
            double newTotal = previousTotal + 36.5 + analyzedTemperature;
            long newCount = count + 1;
            double newAverage = newTotal / newCount;

            summary.setDailyTemperature(newAverage);
            summary.setDailyCount(newCount);
        }

        dailySummaryRepository.save(summary);
    }
}