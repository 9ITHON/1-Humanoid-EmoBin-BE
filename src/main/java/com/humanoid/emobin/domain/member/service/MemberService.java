package com.humanoid.emobin.domain.member.service;

import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.dto.MemberResponse;
import com.humanoid.emobin.domain.member.dto.MemberUpdateRequest;
import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.repository.MemberRepository;
import com.humanoid.emobin.global.exception.AuthErrorCode;
import com.humanoid.emobin.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Optional<Member> findByIdAndProvider(Long id, OAuthProvider provider) {
        return memberRepository.findByOauthIdAndOauthProvider(id, provider);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public String getNickname(Long memberId) {
        return getMember(memberId).getNickname();
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberById(Long memberId) {
        return MemberResponse.from(getMember(memberId));
    }

    public void updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = getMember(memberId);
        member.update(request);
    }

    public void delete(Long memberId) {
        Member member = getMember(memberId);
        member.delete();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.MEMBER_NOT_FOUND));
    }
}