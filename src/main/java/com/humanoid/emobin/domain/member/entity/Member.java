package com.humanoid.emobin.domain.member.entity;

import com.humanoid.emobin.commnon.Gender;
import com.humanoid.emobin.commnon.OAuthProvider;
import com.humanoid.emobin.domain.member.dto.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
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

    private boolean isDeleted = false;

    public void update(MemberUpdateRequest request) {
        this.nickname = request.getNickname();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
