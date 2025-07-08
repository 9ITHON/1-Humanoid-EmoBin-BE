package com.humanoid.emobin.auth.dto;

import com.humanoid.emobin.commnon.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthLoginFailureInfo {
    private Long oauthId;
    private OAuthProvider oauthProvider;
    private String nickname;
}
