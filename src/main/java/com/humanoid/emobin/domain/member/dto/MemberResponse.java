package com.humanoid.emobin.domain.member.dto;

import com.humanoid.emobin.commnon.Gender;
import com.humanoid.emobin.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class MemberResponse {
    public String nickname;
    public String profile;
    public LocalDate birthdate;
    private Gender gender;

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getNickname(), member.getProfile(), member.getBirthdate(), member.getGender());
    }
}