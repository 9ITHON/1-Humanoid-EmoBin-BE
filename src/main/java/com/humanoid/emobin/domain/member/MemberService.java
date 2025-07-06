package com.humanoid.emobin.domain.member;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import com.humanoid.emobin.global.exception.AuthErrorCode;
import com.humanoid.emobin.global.exception.CustomException;
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

    public void save(Member member) {
        memberRepository.save(member);
    }

    public String getNickname(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.MEMBER_NOT_FOUND));
        return member.getNickname();
    }
}