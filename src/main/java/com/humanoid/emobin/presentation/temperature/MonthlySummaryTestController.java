package com.humanoid.emobin.presentation.temperature;

import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.MonthlySummaryService;
import com.humanoid.emobin.domain.member.Member;
import com.humanoid.emobin.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/test/monthly")
@RequiredArgsConstructor
public class MonthlySummaryTestController {

    private final MonthlySummaryService monthlySummaryService;
    private final MemberRepository memberRepository;

    @GetMapping("/update")
    public ResponseEntity<String> updateMonthlySummary(
            @RequestParam("memberId") Long memberId,  // 🔁 토큰 없이 직접 받기
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        monthlySummaryService.updateOrCreateMonthlySummary(member, date);
        return ResponseEntity.ok("월간 요약 생성 또는 업데이트 완료 (월말만 반영됨)");
    }
}
