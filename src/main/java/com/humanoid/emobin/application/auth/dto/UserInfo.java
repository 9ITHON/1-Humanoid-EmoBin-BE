package com.humanoid.emobin.application.auth.dto;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
    private Long oauthId;
    private OAuthProvider oAuthProvider;
    private String nickname;
    private String profile;
}