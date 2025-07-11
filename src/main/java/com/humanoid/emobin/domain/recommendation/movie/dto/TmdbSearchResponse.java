package com.humanoid.emobin.domain.recommendation.movie.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TmdbSearchResponse {
    private List<TmdbMovieData> results;
}