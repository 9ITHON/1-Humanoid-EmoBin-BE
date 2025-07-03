package com.humanoid.emobin.presentation.auth;

import com.humanoid.emobin.application.auth.AuthService;
import com.humanoid.emobin.application.auth.dto.KakaoLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
public class KakaoAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody KakaoLoginRequest request) {
        authService.verifyUser(request.getAccessToken());
        return ResponseEntity.status(200).build();
    }
}
