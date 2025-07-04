package com.humanoid.emobin.application.auth;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.Member;
import com.humanoid.emobin.domain.member.MemberService;
import com.humanoid.emobin.application.auth.dto.UserInfo;
import com.humanoid.emobin.application.auth.dto.LoginResponse;
import com.humanoid.emobin.global.exception.MemberNotFoundException;
import com.humanoid.emobin.infrastructure.jwt.JwtProvider;
import com.humanoid.emobin.infrastructure.oauth.KakaoOAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;



    public LoginResponse verifyUser(String accessToken) {
        UserInfo userInfo = kakaoOAuthClient.getUserInfo(accessToken);

        Long id = userInfo.getOauthId();
        OAuthProvider provider = userInfo.getOAuthProvider();



        Optional<Member> optionalMember = memberService.findByIdAndProvider(id, provider);
        if (optionalMember.isPresent()) {
            Long memberId = optionalMember.get().getId();
            return new LoginResponse(
                    jwtProvider.createAccessToken(memberId),
                    jwtProvider.createRefreshToken(memberId));
        } else {
            //redisService.cacheUserInfo(userInfo);
            throw new MemberNotFoundException("해당 회원이 존재하지 않습니다. (회원 가입 필요)");
        }
    }
}