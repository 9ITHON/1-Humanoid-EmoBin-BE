package com.humanoid.emobin.domain.member.controller;

import com.humanoid.emobin.auth.AuthService;
import com.humanoid.emobin.auth.dto.RefreshTokenRequest;
import com.humanoid.emobin.domain.member.dto.MemberResponse;
import com.humanoid.emobin.domain.member.dto.MemberUpdateRequest;
import com.humanoid.emobin.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/me")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal Long memberId) {
        MemberResponse response = memberService.findMemberById(memberId);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping
    public ResponseEntity<MemberUpdateRequest> updateMember(@AuthenticationPrincipal Long memberId,
                                                            @RequestBody @Valid MemberUpdateRequest request) {
        memberService.updateMember(memberId,request);
        return ResponseEntity.status(200).body(request);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal Long memberId,
                                             @RequestHeader("Authorization") String accessToken,
                                             @Valid @RequestBody RefreshTokenRequest request) {
        memberService.delete(memberId);
        authService.logout(accessToken, request.getRefreshToken());
        return ResponseEntity.status(204).build();
    }
}