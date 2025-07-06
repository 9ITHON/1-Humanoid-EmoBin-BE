package com.humanoid.emobin.domain.emotioncauses;

import com.humanoid.emobin.domain.emotionhistory.EmotionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmotionCauseRepository extends JpaRepository<EmotionCauseEntity, Long> {
    List<EmotionCauseEntity> findByEmotionHistory(EmotionHistoryEntity history);
}
