import re
import os
import json
from dotenv import load_dotenv
from openai import OpenAI


load_dotenv()
client = OpenAI()


emotion_labels = ['행복(Happiness)', '슬픔(Sadness)', '분노(Anger)', '공포(Fear)', '놀람(Surprise)', '혐오(Disgust)']
cause_labels = [
    "대인관계 요인", "자기개념 및 자존감", "트라우마 및 생애사적 사건",
    "스트레스 및 환경적 요인", "생물학적 및 신체적 요인", "도덕적 판단 및 가치 갈등",
    "실존적 요인", "사회문화적 요인", "정체성 관련 요인"
]


emotion_weights = {
    "행복": 2.0,
    "슬픔": -1.5,
    "분노": -2.0,
    "공포": -1.8,
    "놀람": 0.0,
    "혐오": -1.7
}

def preprocess(text):
    text = text.strip()
    text = re.sub(r'\s+', ' ', text)
    text = re.sub(r'(.)\1{4,}', r'\1\1\1', text)
    text = re.sub(r'(https?://|www\.)\S+', '', text)
    text = re.sub(r'\S+@\S+\.\S+', '', text)
    return text

def gpt_analyze_emotion_and_causes(text):
    prompt = f"""다음 문장에서 감정과 감정의 원인을 분석해 주세요.

[감정 라벨] 중에서 가장 적절한 하나를 선택하세요:
{', '.join(emotion_labels)}

[감정 원인 라벨] 중에서 해당 문장에 가장 관련 있는 2가지를 골라주세요:
{', '.join(cause_labels)}

또한 감정의 깊이를 1.0에서 10.0 사이의 소수점 첫째 자리까지로 정수/소수를 포함해 추정해 주세요.

문장: "{text}"

출력 예시:
감정: 슬픔(Sadness)
감정 원인: 자기개념 및 자존감, 대인관계 요인
감정 깊이: 7.3
사용자에게 전하는 한마디: "그런일이 있었구나 이런 부분에서 너의 감정이 많이 안 좋을 것 같네"
"""
    try:
        response = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "당신은 감정과 감정 원인을 정확히 분류하는 한국어 상담사입니다."},
                {"role": "user", "content": prompt}
            ]
        )
        return response.choices[0].message.content.strip()
    except Exception as e:
        print("분석 실패")
        return f"GPT 호출 실패: {e}"


def parse_result(result_text):
    try:
        lines = result_text.split('\n')
        emotion = ""
        causes = []
        depth = None
        temperature = 0.0
        message = ""

        for line in lines:
            if line.startswith("감정:"):
                emotion = line.replace("감정:", "").strip()
            elif line.startswith("감정 원인:"):
                causes = [c.strip() for c in line.replace("감정 원인:", "").split(',')]
            elif line.startswith("감정 깊이:"):
                depth_str = line.replace("감정 깊이:", "").strip()
                try:
                    depth = round(float(depth_str), 1)
                except:
                    depth = None
            elif line.startswith("사용자에게 전하는 한마디:"):
                message = line.replace("사용자에게 전하는 한마디:", "").strip().strip('"')

        if emotion:
            base_emotion = emotion.split("(")[0]
            if base_emotion == "놀람":
                if depth is not None:
                    if any("긍정" in cause for cause in causes):
                        temperature = round(1.0 * depth, 1)
                    elif any("부정" in cause for cause in causes):
                        temperature = round(-1.0 * depth, 1)
                    else:
                        temperature = 0.0
            else:
                weight = emotion_weights.get(base_emotion, 0.0)
                if depth is not None:
                    temperature = round(weight * depth, 1)

        return {
            "emotion": emotion,
            "causes": causes,
            "emotionDepth": depth,
            "temperature": temperature,
            "message": message
        }
    except Exception as e:
        return {"error": f"결과 파싱 실패: {e}"}