package com.humanoid.emobin.domain.emotionhistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionHistoryRepository extends JpaRepository<EmotionHistoryEntity, Long> {
    // 추후 일별/월별 조회, 유저별 분석 내역 확인 등 확장 가능
}
