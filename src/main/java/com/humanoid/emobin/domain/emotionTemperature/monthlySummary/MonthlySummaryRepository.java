package com.humanoid.emobin.domain.emotionTemperature.monthlySummary;

import com.humanoid.emobin.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MonthlySummaryRepository extends JpaRepository<MonthlySummaryEntity, Long> {
    Optional<MonthlySummaryEntity> findByMemberAndMonth(Member member, LocalDate month);
}