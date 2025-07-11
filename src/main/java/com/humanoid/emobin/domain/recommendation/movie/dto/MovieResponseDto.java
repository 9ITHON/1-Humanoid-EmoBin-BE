package com.humanoid.emobin.domain.recommendation.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieResponseDto {
    private String title;
    private String posterUrl;
    private String overview;
    private String releaseDate;
    private double rating;
}