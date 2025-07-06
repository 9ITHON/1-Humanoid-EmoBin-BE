package com.humanoid.emobin.presentation.analysis;

import com.humanoid.emobin.infrastructure.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test-token")
public class TokenTestController {

    private final JwtProvider jwtProvider;

    @GetMapping("/generate")
    public String generateToken(@RequestParam Long memberId) {
        return jwtProvider.createAccessToken(memberId);
    }
}