package com.humanoid.emobin.domain.recommendation.movie.service;


import com.humanoid.emobin.domain.recommendation.dto.RecommendationRequest;
import com.humanoid.emobin.domain.recommendation.movie.dto.MovieResponseDto;
import com.humanoid.emobin.infrastructure.python.movie.PythonExecutor;
import com.humanoid.emobin.infrastructure.python.movie.tmdb.TmdbClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecommendationMovieService {

    private final PythonExecutor pythonExecutor;
    private final TmdbClient tmdbClient;

    public List<MovieResponseDto> recommendMovies(RecommendationRequest request) {
        String emotion = request.getSimplifiedEmotion(); // "슬픔(Sadness)" → "슬픔"
        String message = request.getMessage() != null ? request.getMessage() : "";
        String genre = request.getGenre() != null ? request.getGenre() : "Any";
        int movieCount = request.getMoviecount();

        // 1. 감정 기반 추천 Python 스크립트 실행
        List<String> recommendedTitles = pythonExecutor.runRecommendScript(
                emotion, message, genre, movieCount
        );

        if (recommendedTitles == null || recommendedTitles.isEmpty()) {
            return List.of();
        }

        // 2. TMDB API를 통해 제목 → 영화 정보 조회
        return recommendedTitles.stream()
                .map(tmdbClient::getMovieByTitle)
                .filter(Objects::nonNull)
                .toList();
    }
}