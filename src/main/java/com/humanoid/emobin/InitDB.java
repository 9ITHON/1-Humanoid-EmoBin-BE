package com.humanoid.emobin;

import com.humanoid.emobin.domain.member.Member;
import com.humanoid.emobin.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService service;

    @PostConstruct
    public void init() {
        //service.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final MemberRepository memberRepository;
        public void dbInit1() {
            Member member = new Member();
            em.persist(member);
        }
    }
}
