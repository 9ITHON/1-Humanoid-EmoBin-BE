package com.humanoid.emobin.domain.emotionTemperature.monthlySummary;


import com.humanoid.emobin.domain.emotionTemperature.dailySummary.DailySummaryEntity;
import com.humanoid.emobin.domain.emotionTemperature.dailySummary.repository.DailySummaryRepository;
import com.humanoid.emobin.domain.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlySummaryService {

    private final MonthlySummaryRepository monthlySummaryRepository;
    private final DailySummaryRepository dailySummaryRepository;

    @Transactional
    public void updateOrCreateMonthlySummary(Member member, LocalDate now) {
        // 1. 월말이 아닐 경우 중단
        if (!now.equals(now.withDayOfMonth(now.lengthOfMonth()))) {
            return; // 월 마지막 날이 아닐 경우 수행 안 함
        }

        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDate monthEnd = now;

        // 1. 해당 월의 일별 요약 모두 조회
        List<DailySummaryEntity> dailySummaries = dailySummaryRepository.findByMemberAndLocalDateBetween(
                member, monthStart, monthEnd
        );

        // 2. 일별 데이터가 하나도 없으면 저장하지 않음
        if (dailySummaries.isEmpty()) {
            return;
        }

        // 3. 평균 계산
        double avg = dailySummaries.stream()
                .mapToDouble(DailySummaryEntity::getDailyTemperature)
                .average()
                .orElse(0.0);
        // 4. 이미 존재하면 업데이트, 아니면 무시
        double roundedAvg = Math.round(avg * 10.0) / 10.0;

        monthlySummaryRepository.findByMemberAndMonth(member, monthStart)
                .ifPresentOrElse(summary -> {

                    summary.setMonthlyTemperature(roundedAvg);
                    monthlySummaryRepository.save(summary);
                }, () -> {
                    MonthlySummaryEntity newSummary = new MonthlySummaryEntity();
                    newSummary.setMember(member);
                    newSummary.setMonth(monthStart);
                    newSummary.setMonthlyTemperature(roundedAvg);
                    monthlySummaryRepository.save(newSummary);
                });

    }
}
