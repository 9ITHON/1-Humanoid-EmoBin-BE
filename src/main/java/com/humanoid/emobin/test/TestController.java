package com.humanoid.emobin.test;

import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.service.MemberService;
import com.humanoid.emobin.auth.infrastructure.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;


    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/auth")
    public String getAuth() {
        memberService.save(new Member());
        return jwtProvider.createRefreshToken(1L);
    }
}
