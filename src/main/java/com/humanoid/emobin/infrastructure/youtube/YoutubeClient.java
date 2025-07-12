package com.humanoid.emobin.infrastructure.youtube;

import com.humanoid.emobin.domain.recommendation.music.dto.MusicResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class YoutubeClient {

    public List<MusicResponseDto> getMusicByEmotion(String emotion) {


        return null;
    }
}
