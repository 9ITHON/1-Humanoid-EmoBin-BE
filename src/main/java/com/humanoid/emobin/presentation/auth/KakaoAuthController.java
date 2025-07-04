package com.humanoid.emobin.presentation.auth;

import com.humanoid.emobin.application.auth.AuthService;
import com.humanoid.emobin.application.auth.dto.KakaoLoginRequest;
import com.humanoid.emobin.application.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
public class KakaoAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody KakaoLoginRequest request) {
        LoginResponse loginResponse = authService.verifyUser(request.getAccessToken());
        return ResponseEntity.status(200).body(loginResponse);
    }
}
