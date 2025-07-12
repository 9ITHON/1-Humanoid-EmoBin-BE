package com.humanoid.emobin.global.response;

import com.humanoid.emobin.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EmotionErrorCode implements ErrorCode {

    // 기존 예외 코드
    EMOTION_ANALYSIS_FAILED("EMOTION_ANALYSIS_FAILED", HttpStatus.INTERNAL_SERVER_ERROR.value(), "감정 분석에 실패했습니다."),
    INVALID_REQUEST("INVALID_REQUEST", HttpStatus.BAD_REQUEST.value(), "요청 값이 올바르지 않습니다."),
    NULL_ANALYSIS_RESULT("NULL_ANALYSIS_RESULT", HttpStatus.INTERNAL_SERVER_ERROR.value(), "분석 결과가 비어 있습니다."),
    INVALID_RESPONSE_FROM_AI("INVALID_RESPONSE_FROM_AI", HttpStatus.BAD_GATEWAY.value(), "AI 응답 형식이 올바르지 않습니다."),
    INVALID_ANALYSIS_TEMPERATURE("INVALID_ANALYSIS_TEMPERATURE", 400, "분석된 감정 온도가 유효하지 않습니다."),
    MEMBER_DATA_NOT_FOUND("MEMBER_DATA_NOT_FOUND", HttpStatus.NOT_FOUND.value(), "해당 회원의 감정 데이터를 찾을 수 없습니다."),
    TMDB_RESULT_EMPTY("TMDB_RESULT_EMPTY", HttpStatus.NOT_FOUND.value(), "TMDB에서 해당 영화 정보를 찾을 수 없습니다."),
    INVALID_MOVIE_COUNT("INVALID_MOVIE_COUNT", HttpStatus.BAD_REQUEST.value(), "요청한 영화 개수는 1~10 사이여야 합니다."),
    MOVIELIST_NOT_FOUND("MOVIELIST_NOT_FOUND", HttpStatus.NOT_FOUND.value(), "추천 영화 목록을 찾을 수 없습니다."),
    TMDB_API_ERROR("TMDB_API_ERROR", 502, "TMDB API 호출 중 오류가 발생했습니다."),
    PYTHON_EXECUTION_ERROR("PYTHON_EXECUTION_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Python 스크립트 실행 중 오류가 발생했습니다.");


    private final String code;
    private final int status;
    private final String message;

    EmotionErrorCode(String code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}