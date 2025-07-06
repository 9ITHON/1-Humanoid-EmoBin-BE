package com.humanoid.emobin.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private Object data;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message,Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
