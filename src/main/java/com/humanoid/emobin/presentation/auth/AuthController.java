package com.humanoid.emobin.presentation.auth;

import com.humanoid.emobin.application.auth.AuthService;
import com.humanoid.emobin.application.auth.dto.AccessTokenResponse;
import com.humanoid.emobin.application.auth.dto.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/singup")
    public ResponseEntity<?> signup() {
        return null;
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
