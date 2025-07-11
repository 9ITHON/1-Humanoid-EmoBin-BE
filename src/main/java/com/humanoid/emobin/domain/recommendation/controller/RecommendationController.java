package com.humanoid.emobin.domain.recommendation.controller;

import com.humanoid.emobin.domain.recommendation.movie.dto.MovieResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.humanoid.emobin.domain.recommendation.dto.RecommendationRequest;
import com.humanoid.emobin.domain.recommendation.movie.service.RecommendationMovieService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationMovieService recommendationMovieService;

    @PostMapping("/movie")
    public ResponseEntity<List<MovieResponseDto>> recommendMovie(@RequestBody RecommendationRequest request) {
        List<MovieResponseDto> recommendations = recommendationMovieService.recommendMovies(request);
        return ResponseEntity.ok(recommendations);
    }
}