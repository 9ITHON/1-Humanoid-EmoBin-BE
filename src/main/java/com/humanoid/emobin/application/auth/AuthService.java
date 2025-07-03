package com.humanoid.emobin.application.auth;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.MemberService;
import com.humanoid.emobin.application.auth.dto.UserInfo;
import com.humanoid.emobin.application.auth.dto.LoginResponse;
import com.humanoid.emobin.infrastructure.oauth.KakaoOAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;


    public LoginResponse verifyUser(String accessToken) {
        UserInfo userInfo = kakaoOAuthClient.getUserInfo(accessToken);

        Long id = userInfo.getOauthId();
        OAuthProvider provider = userInfo.getOAuthProvider();


        return null;
    }
}
