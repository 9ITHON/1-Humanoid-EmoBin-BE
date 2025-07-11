package com.humanoid.emobin.auth.controller;

import com.humanoid.emobin.auth.AuthService;
import com.humanoid.emobin.auth.dto.KakaoLoginRequest;
import com.humanoid.emobin.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
public class KakaoAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody KakaoLoginRequest request) {
        LoginResponse loginResponse = authService.authenticateKakaoUser(request.getCode());
        return ResponseEntity.status(200).body(loginResponse);
    }
}
