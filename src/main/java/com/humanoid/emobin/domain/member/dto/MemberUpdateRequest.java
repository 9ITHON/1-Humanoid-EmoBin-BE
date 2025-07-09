package com.humanoid.emobin.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {
    @NotBlank(message = "이름은 필수입니다.")
    public String nickname;
}
