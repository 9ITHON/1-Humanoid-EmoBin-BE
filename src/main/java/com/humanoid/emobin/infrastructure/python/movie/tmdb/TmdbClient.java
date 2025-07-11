package com.humanoid.emobin.infrastructure.python.movie.tmdb;

import com.humanoid.emobin.domain.recommendation.movie.dto.MovieResponseDto;
import com.humanoid.emobin.domain.recommendation.movie.dto.TmdbMovieData;
import com.humanoid.emobin.domain.recommendation.movie.dto.TmdbSearchResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmdbClient {


    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .build();
    private String tmdbApiKey;

    @PostConstruct
    public void init() {
        this.tmdbApiKey = System.getProperty("MOVIE_API_KEY");
        log.info("✅ TMDB Key loaded in @PostConstruct: {}", tmdbApiKey);
    }

    public MovieResponseDto getMovieByTitle(String title) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", title)
                        .queryParam("language", "en-US")
                        .queryParam("api_key", tmdbApiKey)
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(TmdbSearchResponse.class)
                                .map(result -> {
                                    TmdbMovieData movie = result.getResults().get(0);
                                    return new MovieResponseDto(
                                            movie.getTitle(),
                                            "https://image.tmdb.org/t/p/w500" + movie.getPosterPath(),
                                            movie.getOverview(),
                                            movie.getReleaseDate(),
                                            movie.getVoteAverage()
                                    );
                                });
                    } else {
                        return response.bodyToMono(String.class)
                                .doOnNext(errorBody -> log.error("❌ TMDB 응답 오류: status={}, body={}", response.statusCode(), errorBody))
                                .thenReturn(null);
                    }
                })
                .onErrorResume(e -> {
                    log.error("❌ TMDB 요청 예외 발생", e);
                    return Mono.empty();
                })
                .block();

    }
}