package com.humanoid.emobin.domain.member.repository;

import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByOauthIdAndOauthProvider(Long oauthId, OAuthProvider provider);
}
