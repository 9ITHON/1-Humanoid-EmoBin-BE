package com.humanoid.emobin.infrastructure.oauth;


import com.humanoid.emobin.application.auth.dto.UserInfo;
import com.humanoid.emobin.application.auth.dto.KakaoUserInfoResponse;
import com.humanoid.emobin.application.auth.dto.KakaoUserInfoResponse.KakaoAccount.Profile;
import com.humanoid.emobin.domain.commnon.OAuthProvider;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private static final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public UserInfo getUserInfo(String accessToken) {
        KakaoUserInfoResponse userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();

        Profile profile = userInfo.getKakaoAccount().getProfile();

        return new UserInfo(userInfo.getId(),
                OAuthProvider.KAKAO,
                profile.getNickName(),
                profile.getProfileImageUrl());
    }
}
