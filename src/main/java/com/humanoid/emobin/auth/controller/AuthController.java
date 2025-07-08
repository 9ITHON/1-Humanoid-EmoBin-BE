package com.humanoid.emobin.auth.controller;

import com.humanoid.emobin.auth.AuthService;
import com.humanoid.emobin.auth.dto.AccessTokenResponse;
import com.humanoid.emobin.auth.dto.LoginResponse;
import com.humanoid.emobin.auth.dto.RefreshTokenRequest;
import com.humanoid.emobin.auth.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@RequestBody SignupRequest request) {
        LoginResponse response = authService.signup(request);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken,
                                       @RequestBody RefreshTokenRequest request) {
        authService.logout(accessToken, request.getRefreshToken());
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> reissueAccessToken(@RequestBody RefreshTokenRequest request) {
        AccessTokenResponse response = authService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.status(200).body(response);
    }
}
