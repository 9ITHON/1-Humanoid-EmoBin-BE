package com.humanoid.emobin.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @NotNull(message = "엑세스토큰을 포함해야합니다")
    private String refreshToken;
}