package com.humanoid.emobin.global.exception;

import com.humanoid.emobin.auth.dto.OAuthLoginFailureInfo;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    private final OAuthLoginFailureInfo oAuthLoginFailureInfo;

    public MemberNotFoundException(ErrorCode errorCode, OAuthLoginFailureInfo oAuthLoginFailureInfo) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.oAuthLoginFailureInfo = oAuthLoginFailureInfo;
    }
}
