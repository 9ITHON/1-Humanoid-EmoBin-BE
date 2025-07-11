package com.humanoid.emobin.auth.infrastructure.oauth;


import com.humanoid.emobin.auth.dto.KakaoTokenResponse;
import com.humanoid.emobin.auth.dto.TemporaryMemberInfo;
import com.humanoid.emobin.auth.dto.KakaoUserInfoResponse;
import com.humanoid.emobin.auth.dto.KakaoUserInfoResponse.KakaoAccount.Profile;
import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.global.exception.AuthErrorCode;
import com.humanoid.emobin.global.exception.CustomException;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoOAuthClient {

    @Value("${kakao.client_id}")
    private String clientId;

    private static final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private static final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponse kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CustomException(AuthErrorCode.KAKAO_INVALID_TOKEN)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new CustomException(AuthErrorCode.KAKAO_INTERNAL_ERROR)))
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        return kakaoTokenResponseDto.getAccessToken();
    }

    public TemporaryMemberInfo getUserInfo(String accessToken) {
        KakaoUserInfoResponse userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CustomException(AuthErrorCode.KAKAO_INVALID_TOKEN)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new CustomException(AuthErrorCode.KAKAO_INTERNAL_ERROR)))
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();

        Profile profile = userInfo.getKakaoAccount().getProfile();

        return new TemporaryMemberInfo(userInfo.getId(),
                OAuthProvider.KAKAO,
                profile.getNickName(),
                profile.getProfileImageUrl());
    }
}
