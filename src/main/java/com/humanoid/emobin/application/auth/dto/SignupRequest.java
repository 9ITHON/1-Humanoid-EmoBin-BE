package com.humanoid.emobin.application.auth.dto;

import com.humanoid.emobin.domain.commnon.Gender;
import com.humanoid.emobin.domain.commnon.OAuthProvider;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SignupRequest {
    private Long oauthId;
    private OAuthProvider oAuthProvider;
    private String nickname;
    private LocalDate birthdate;
    private Gender gender;
}
