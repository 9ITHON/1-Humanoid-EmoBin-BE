package com.humanoid.emobin.application.auth.dto;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthLoginFailureInfo {
    private Long oauthId;
    private OAuthProvider oauthProvider;
    private String nickname;
}
