package com.humanoid.emobin;

import com.humanoid.emobin.auth.infrastructure.jwt.JwtProvider;
import com.humanoid.emobin.auth.infrastructure.redis.RedisService;
import com.humanoid.emobin.commnon.Gender;
import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.entity.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
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

        public void dbInit1() {

        }
    }
}
