package com.humanoid.emobin.presentation.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/singup")
    public ResponseEntity<?> signup() {
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> reissueAccessToken() {
        return null;
    }
}
