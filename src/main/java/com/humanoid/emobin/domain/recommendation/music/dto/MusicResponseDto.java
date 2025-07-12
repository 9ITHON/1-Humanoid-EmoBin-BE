package com.humanoid.emobin.domain.recommendation.music.dto;

import com.humanoid.emobin.domain.recommendation.music.Music;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MusicResponseDto {
    private String title;
    private String youtubeUrl;

    public static MusicResponseDto from(Music music) {
        return new MusicResponseDto(music.getTitle(), music.getUrl());
    }
}
