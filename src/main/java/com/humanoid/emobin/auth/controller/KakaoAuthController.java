package com.humanoid.emobin.auth.controller;

import com.humanoid.emobin.auth.AuthService;
import com.humanoid.emobin.auth.dto.KakaoLoginRequest;
import com.humanoid.emobin.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
@Slf4j
public class KakaoAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody KakaoLoginRequest request) {
        log.info("===={}====",request.getCode());
        LoginResponse loginResponse = authService.authenticateKakaoUser(request.getCode());
        return ResponseEntity.status(200).body(loginResponse);
    }
}
