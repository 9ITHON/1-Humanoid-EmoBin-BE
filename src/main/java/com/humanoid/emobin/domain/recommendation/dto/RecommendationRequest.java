package com.humanoid.emobin.domain.recommendation.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class RecommendationRequest {
    // 감정을 전달 받아야 함. e.g. "슬픔" or "슬픔(Sadness)"
    private String emotion;
    // 추천에 영향을 줄 수 있는 검사 메시지 (심리적 조언 등)
    private String message;
    //추천 받을 영화 수
    private int moviecount;
    // 특정 장르를 선호할 경우 (예: "드라마", "코미디") – 선택 사항
    private String genre;

    public String getSimplifiedEmotion() {
        if (emotion == null) return null;
        int idx = emotion.indexOf("(");
        return (idx != -1) ? emotion.substring(0, idx).trim() : emotion.trim();
    }
}
