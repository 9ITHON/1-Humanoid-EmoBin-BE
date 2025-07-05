package com.humanoid.emobin.application.auth;

import com.humanoid.emobin.application.auth.dto.AccessTokenResponse;
import com.humanoid.emobin.application.auth.dto.UserInfo;
import com.humanoid.emobin.domain.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.Member;
import com.humanoid.emobin.domain.member.MemberService;
import com.humanoid.emobin.application.auth.dto.LoginResponse;
import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.exception.AuthErrorCode;
import com.humanoid.emobin.infrastructure.jwt.JwtProvider;
import com.humanoid.emobin.infrastructure.oauth.KakaoOAuthClient;
import com.humanoid.emobin.infrastructure.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Value("${jwt.refresh-token-validity}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;



    public LoginResponse authenticateKakaoUser(String accessTokenFromKakao) {
        UserInfo userInfo = kakaoOAuthClient.getUserInfo(accessTokenFromKakao);

        Long id = userInfo.getOauthId();
        OAuthProvider provider = userInfo.getOAuthProvider();

        Optional<Member> optionalMember = memberService.findByIdAndProvider(id, provider);
        if (optionalMember.isPresent()) {
            Long memberId = optionalMember.get().getId();

            String accessToken = jwtProvider.createAccessToken(memberId);
            String refreshToken = jwtProvider.createRefreshToken(memberId);

            redisService.setData("refresh:" + memberId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
            return new LoginResponse(accessToken, refreshToken);
        } else {
            //redisService.cacheUserInfo(userInfo);
            throw new CustomException(AuthErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public void logout(String accessToken, String refreshToken) {
        Long memberId;

        accessToken = jwtProvider.resolveToken(accessToken);
        if (accessToken == null) {
            throw new CustomException(AuthErrorCode.TOKEN_NOT_FOUND);
        }

        try {
            Claims claims = jwtProvider.parseClaims(accessToken);
            String tokenId = claims.getId();
            long ttl = jwtProvider.getRemainingMillis(claims);

            //블랙리스트 등록
            redisService.setData("blacklist:access:" + tokenId, "logout", ttl);
            memberId = claims.get("memberId", Long.class);
        } catch (ExpiredJwtException e) { //토큰 만료
            Claims claims = e.getClaims();
            memberId = claims.get("memberId", Long.class);
        }

        //리프레시 토큰 삭제
        validateRefreshToken(memberId, refreshToken);
        redisService.deleteData("refresh:" + memberId);
    }

    public AccessTokenResponse reissueAccessToken(String refreshToken) {
        jwtProvider.validateToken(refreshToken);

        Claims claims = jwtProvider.parseClaims(refreshToken);
        Long memberId = claims.get("memberId", Long.class);

        validateRefreshToken(memberId, refreshToken);

        String newAccessToken = jwtProvider.createAccessToken(memberId);

        return new AccessTokenResponse(newAccessToken);
    }

    private void validateRefreshToken(Long memberId, String refreshToken) {
        String savedRefresh = String.valueOf(redisService.getData("refresh:" + memberId));
        if (savedRefresh == null) {
            throw new CustomException(AuthErrorCode.TOKEN_EXPIRED);
        } else if (!refreshToken.equals(savedRefresh)) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}