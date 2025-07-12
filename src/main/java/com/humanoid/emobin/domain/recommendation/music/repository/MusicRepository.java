package com.humanoid.emobin.domain.recommendation.music.repository;

import com.humanoid.emobin.domain.recommendation.music.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music,Long> {
    @Query(value = "SELECT * FROM music WHERE emotion = :emotion ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Music> findRandomByEmotion(@Param("emotion") String emotion, @Param("count") int count);
}
