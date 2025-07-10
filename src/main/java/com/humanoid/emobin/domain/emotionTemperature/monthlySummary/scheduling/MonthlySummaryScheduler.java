package com.humanoid.emobin.domain.emotionTemperature.monthlySummary.scheduling;

import com.humanoid.emobin.domain.emotionTemperature.monthlySummary.MonthlySummaryService;
import com.humanoid.emobin.domain.member.entity.Member;
import com.humanoid.emobin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlySummaryScheduler {

    private final MonthlySummaryService monthlySummaryService;
    private final MemberRepository memberRepository;

    // 매일 새벽 2시에 실행
    @Scheduled(cron = "0 0 2 * * *")
    public void runMonthlySummaryTask() {
        LocalDate today = LocalDate.now();
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        if (!today.equals(endOfMonth)) {
            log.info("오늘은 말일이 아닙니다. 실행하지 않습니다: {}", today);
            return;
        }

        log.info("말일입니다. 월간 요약을 실행합니다: {}", today);

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            monthlySummaryService.updateOrCreateMonthlySummary(member, today);
        }

        log.info("모든 회원에 대한 월간 요약 작업 완료");
    }
}