package com.humanoid.emobin.domain.recommendation.music.service;

import com.humanoid.emobin.domain.recommendation.dto.RecommendationRequest;
import com.humanoid.emobin.domain.recommendation.music.Music;
import com.humanoid.emobin.domain.recommendation.music.dto.MusicResponseDto;
import com.humanoid.emobin.domain.recommendation.music.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;

    public void save(String emotion, String title, String url) {
        Music music = new Music();
        music.setEmotion(emotion);
        music.setTitle(title);
        music.setUrl(url);
        musicRepository.save(music);
    }

    public List<MusicResponseDto> getRecommendation(RecommendationRequest request) {
        List<Music> randomByEmotion = musicRepository.findRandomByEmotion(request.getSimplifiedEmotion(), 3);
        return randomByEmotion.stream()
                .map(MusicResponseDto::from)
                .toList();
    }
}
