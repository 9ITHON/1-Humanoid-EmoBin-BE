package com.humanoid.emobin.domain.member;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    public Optional<Member> findByIdAndProvider(Long id, OAuthProvider provider) {
        return memberRepository.findByOauthIdAndOauthProvider(id, provider);
    }
}