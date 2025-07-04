package com.humanoid.emobin.domain.member;

import com.humanoid.emobin.domain.commnon.Gender;
import com.humanoid.emobin.domain.commnon.OAuthProvider;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;


@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String profile;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Long oauthId;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oauthProvider;
}
