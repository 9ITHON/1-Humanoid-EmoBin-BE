package com.humanoid.emobin.infrastructure.python.movie.tmdb;

import com.humanoid.emobin.domain.recommendation.movie.dto.MovieResponseDto;
import com.humanoid.emobin.domain.recommendation.movie.dto.TmdbMovieData;
import com.humanoid.emobin.domain.recommendation.movie.dto.TmdbSearchResponse;
import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.response.EmotionErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmdbClient {


    private final WebClient webClient;

    @Autowired
    public TmdbClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    private String tmdbApiKey;

    @PostConstruct
    public void init() {
        this.tmdbApiKey = System.getProperty("MOVIE_API_KEY");
    }

    public MovieResponseDto getMovieByTitle(String title) {
        try {
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
                                        if (result.getResults() == null || result.getResults().isEmpty()) {
                                            throw new CustomException(EmotionErrorCode.TMDB_RESULT_EMPTY);
                                        }
                                        TmdbMovieData movie = result.getResults().stream()
                                                .filter(m -> m.getPosterPath() != null && !m.getPosterPath().isBlank())
                                                .findFirst()
                                                .orElseThrow(() -> new CustomException(EmotionErrorCode.TMDB_RESULT_EMPTY));

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
                                    .then(Mono.error(new CustomException(EmotionErrorCode.TMDB_API_ERROR)));
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("❌ TMDB 요청 예외 발생", e);
                        return Mono.error(new CustomException(EmotionErrorCode.TMDB_API_ERROR));
                    })
                    .block();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ TMDB 예외 처리 도중 알 수 없는 오류 발생", e);
            throw new CustomException(EmotionErrorCode.TMDB_API_ERROR);
        }
    }

}