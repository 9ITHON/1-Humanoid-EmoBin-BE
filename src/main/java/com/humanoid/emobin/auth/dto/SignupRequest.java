package com.humanoid.emobin.auth.dto;

import com.humanoid.emobin.commnon.Gender;
import com.humanoid.emobin.commnon.OAuthProvider;
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
