package com.humanoid.emobin.auth;

import com.humanoid.emobin.auth.dto.*;
import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.service.MemberService;
import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.exception.AuthErrorCode;
import com.humanoid.emobin.global.exception.MemberNotFoundException;
import com.humanoid.emobin.auth.infrastructure.jwt.JwtProvider;
import com.humanoid.emobin.auth.infrastructure.oauth.KakaoOAuthClient;
import com.humanoid.emobin.auth.infrastructure.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Value("${jwt.refresh-token-validity}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${jwt.access-token-validity}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;



    public LoginResponse authenticateKakaoUser(String accessTokenFromKakao) {
        TemporaryMemberInfo memberInfo = kakaoOAuthClient.getUserInfo(accessTokenFromKakao);

        Long id = memberInfo.getOauthId();
        OAuthProvider provider = memberInfo.getOAuthProvider();

        Optional<Member> optionalMember = memberService.findByIdAndProvider(id, provider);
        if (optionalMember.isPresent()) { //기존 회원
            Member member = optionalMember.get();
            if (member.isDeleted()) { //탈퇴회원이라면
                throw new CustomException(AuthErrorCode.MEMBER_ALREADY_DELETED);
            }
            Long memberId = member.getId();

            String accessToken = jwtProvider.createAccessToken(memberId);
            String refreshToken = jwtProvider.createRefreshToken(memberId);

            redisService.setData("refresh:" + memberId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
            return new LoginResponse(accessToken, refreshToken);
        } else { //신규 회원
            redisService.setData("temp-oauth:" + id + ":" + provider, memberInfo, ACCESS_TOKEN_EXPIRE_TIME);
            throw new MemberNotFoundException(AuthErrorCode.MEMBER_NOT_FOUND,
                    new OAuthLoginFailureInfo(id, provider, memberInfo.getNickname()));
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

    public LoginResponse signup(SignupRequest request) {
        Long oauthId = request.getOauthId();
        OAuthProvider oAuthProvider = request.getOAuthProvider();
        String name = "temp-oauth:" + oauthId + ":" + oAuthProvider;



        TemporaryMemberInfo data = (TemporaryMemberInfo) redisService.getData(name);


        if (data == null) {
            throw new CustomException(AuthErrorCode.TEMP_MEMBER_NOT_FOUND);
        }

        Member member = new Member();
        member.setGender(request.getGender());
        member.setBirthdate(request.getBirthdate());
        member.setNickname(request.getNickname());
        member.setOauthId(data.getOauthId());
        member.setOauthProvider(data.getOAuthProvider());
        member.setProfile(data.getProfile());
        memberService.save(member);

        Long memberId = member.getId();

        String accessToken = jwtProvider.createAccessToken(memberId);
        String refreshToken = jwtProvider.createRefreshToken(memberId);

        redisService.setData("refresh:" + memberId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
        return new LoginResponse(accessToken, refreshToken);
    }
}