package com.humanoid.emobin.application.auth.dto;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class TemporaryMemberInfo {
    private Long oauthId;
    private OAuthProvider oAuthProvider;
    private String nickname;
    private String profile;
}