package com.humanoid.emobin.domain.recommendation.controller;

import com.humanoid.emobin.domain.recommendation.movie.dto.MovieResponseDto;
import com.humanoid.emobin.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.humanoid.emobin.domain.recommendation.dto.RecommendationRequest;
import com.humanoid.emobin.domain.recommendation.movie.service.RecommendationMovieService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationMovieService recommendationMovieService;

    @PostMapping("/movie")
    public ResponseEntity<ApiResponse<List<MovieResponseDto>>> recommendMovie(@RequestBody RecommendationRequest request) {
        List<MovieResponseDto> recommendations = recommendationMovieService.recommendMovies(request);
        return ResponseEntity.ok(ApiResponse.success(recommendations));
    }
}