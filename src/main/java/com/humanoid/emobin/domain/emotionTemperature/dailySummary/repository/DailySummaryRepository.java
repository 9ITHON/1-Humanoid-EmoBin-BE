package com.humanoid.emobin.domain.emotionTemperature.dailySummary.repository;


import com.humanoid.emobin.domain.emotionTemperature.dailySummary.DailySummaryEntity;
import com.humanoid.emobin.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailySummaryRepository extends JpaRepository<DailySummaryEntity, Long> {

    Optional<DailySummaryEntity> findByMemberAndLocalDate(Member member, LocalDate localDate);
    List<DailySummaryEntity> findByMemberAndLocalDateBetween(Member member, LocalDate start, LocalDate end);

}

