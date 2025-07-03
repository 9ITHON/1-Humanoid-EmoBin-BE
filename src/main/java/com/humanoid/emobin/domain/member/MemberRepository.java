package com.humanoid.emobin.domain.member;

import com.humanoid.emobin.domain.commnon.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByOauthIdAndOauthProvider(String oauthId, OAuthProvider provider);
}
