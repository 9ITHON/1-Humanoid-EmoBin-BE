package com.humanoid.emobin;

import com.humanoid.emobin.auth.infrastructure.jwt.JwtProvider;
import com.humanoid.emobin.auth.infrastructure.redis.RedisService;
import com.humanoid.emobin.commnon.Gender;
import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.entity.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Profile("local")
public class InitDB {

    private final InitService service;

    @PostConstruct
    public void init() {
        service.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final JwtProvider jwtProvider;
        private final RedisService redisService;
        public void dbInit1() {
            Member member = new Member();
            member.setBirthdate(LocalDate.of(2025, 7, 10));
            member.setGender(Gender.MALE);
            member.setDeleted(true);
            member.setNickname("테스트계정");
            member.setOauthId(12345L);
            member.setOauthProvider(OAuthProvider.KAKAO);
            member.setProfile("test.example");
            em.persist(member);

            Long id = member.getId();
            String refreshToken = jwtProvider.createRefreshToken(id);
            redisService.setData("refresh:" + id, refreshToken, 1209600000);
        }
    }
}
