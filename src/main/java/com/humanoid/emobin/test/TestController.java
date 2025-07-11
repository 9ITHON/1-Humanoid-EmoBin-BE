package com.humanoid.emobin.test;

import com.humanoid.emobin.auth.infrastructure.redis.RedisService;
import com.humanoid.emobin.commnon.Gender;
import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.service.MemberService;
import com.humanoid.emobin.auth.infrastructure.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;


    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/init")
    public void init() {
        Member member = new Member();
        member.setBirthdate(LocalDate.of(2025, 7, 10));
        member.setGender(Gender.MALE);
        member.setDeleted(true);
        member.setNickname("테스트계정");
        member.setOauthId(12345L);
        member.setOauthProvider(OAuthProvider.KAKAO);
        member.setProfile("test.example");
        memberService.save(member);

        Long id = member.getId();
        String refreshToken = jwtProvider.createRefreshToken(id);
        redisService.setData("refresh:" + id, refreshToken, 1209600000);
    }

    @PostMapping("/auth")
    public Map<String, String> getTokens() {

        Optional<Member> member = memberService.findByIdAndProvider(12345L, OAuthProvider.KAKAO);
        Long id = member.get().getId();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtProvider.createAccessToken(id));
        tokens.put("refreshToken", String.valueOf(redisService.getData("refresh:" + id)));
        return tokens;
    }
}
