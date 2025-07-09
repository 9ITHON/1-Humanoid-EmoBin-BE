package com.humanoid.emobin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    //공통 인증 관련
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND",202,"사용자가 존재하지 않습니다. (회원 가입 필요)"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", 401, "토큰이 만료되었습니다."),
    TOKEN_BLACKLISTED("TOKEN_BLACKLISTED", 401, "사용할 수 없는 토큰입니다."),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", 401, "토큰이 존재하지 않습니다."),
    MALFORMED_TOKEN("MALFORMED_TOKEN", 401, "손상되었거나 잘못 구성된 JWT 토큰입니다."),
    INVALID_TOKEN("INVALID_TOKEN",401,"유효하지 않은 토큰입니다."),
    TEMP_MEMBER_NOT_FOUND("TEMP_MEMBER_NOT_FOUND", 410, "임시 회원 정보를 찾을 수 없습니다. (만료 또는 유효하지 않음)"),
    MEMBER_ALREADY_DELETED("USER_ALREADY_DELETED", 409, "이미 탈퇴한 계정입니다. 복구를 원하시면 고객센터로 문의해주세요."),

    //카카오 인증 관련
    KAKAO_INVALID_TOKEN("KAKAO_OAUTH_401", 401, "카카오 액세스 토큰이 유효하지 않습니다."),
    KAKAO_INTERNAL_ERROR("KAKAO_OAUTH_500", 500, "카카오 서버 오류입니다.");

    private final String code;
    private final int status;
    private final String message;
}
