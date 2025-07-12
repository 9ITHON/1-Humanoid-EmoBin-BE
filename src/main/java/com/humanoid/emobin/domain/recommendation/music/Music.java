package com.humanoid.emobin.domain.recommendation.music;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Music {
    @Id @GeneratedValue
    @Column(name = "music_id")
    private Long id;
    private String title;
    private String url;
    private String emotion;
}
