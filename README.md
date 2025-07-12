# EmoBin 프로젝트의 서버입니다.

> **구름톤 유니트 경인지부 해커톤 출전작**

---

## 💡 서비스 한 줄 요약

**프론트로부터 텍스트를 입력받아 분석 결과를 저장 및 전달하는 감정 분석 시스템**

---

# 📘 Git 협업 규칙

## ✅ 이슈 타입 분류

| 타입 | 설명 |
|------|------|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 코드 리팩토링 (기능 변경 없이 구조 개선) |
| `docs` | 문서 변경 (README 등) |
| `que` | 질문 또는 논의 목적 이슈 |

---

## 📝 커밋 메시지 컨벤션

```bash
<타입>: <변경 사항에 대한 간단한 설명>
```

- 커밋 메시지는 한글로 작성합니다.
- 커밋은 하나의 작업 단위로 구분되며, 논리적으로 나눠 작성합니다.

```bash
예)
feat: 로그인 API 구현
fix: 이미지 업로드 버그 수정
refactor: UserService 로직 분리
docs: 배포 가이드 추가
que: API 응답 포맷 관련 질문
```
  
# 🌿 브랜치 전략

## 기본 브랜치
- `main`: 실제 운영 배포 브랜치
- `develop`: 개발 통합 브랜치

## 작업 브랜치 네이밍 규칙 

```bash
<이슈번호>/<타입>/<기능명 또는 수정 내용>
```
브랜치 예시
- 3/feature/emotion-temperature-system: 기능 개발
- 7/fix/login-redirect-bug: 버그 수정
- 12/refactor/user-service-refine: 리팩토링

> 이슈 번호는 GitHub Issues 번호를 따릅니다.

```bash
예)
- `이슈 번호/feature/<기능명>`: 기능 개발용
- `이슈 번호/fix/<버그명>`: 버그 수정
- `이슈 번호/refactor/<리팩토링내용>`: 리팩토링
```
## PR 및 이슈 연동 규칙
- PR 생성 시 반드시 관련 이슈를 연결합니다.
```bash
예)
  관련 이슈: #3
```

---
# 기술 스택

| 항목             | 사용 기술                   |
| -------------- | ----------------------- |
| Backend        | Spring Boot             |
| Database       | MySQL, Redis            |
| Movie       | TMDB            |
| AI Integration | OpenAI (GPT API 사용)     |
| DevOps         | GitHub Actions, AWS EC2 |

  
---
# ERD
![EmoBin_BE](https://github.com/user-attachments/assets/693aa782-1ef4-4a04-a88b-f8306ea43514)
 
|  테이블 명 | 설명                |
|---------------------|-------------|
| `members`     | 사용자 정보 (닉네임, 생년월일, 성별, 프로) 저장 |
| `emotion_history`  | 사용자의 결과를 저장     |
| `emotion_causes`    | 검사결과에서 사용되는 감정 원인이 저장      |
| `daily_summary`   | 감정 달력에서 사용하는 해당 당일의 온도 관련 정보 저장         |
| `monthly_summary`   | 감정 달력에서 사용하는 해당 달의 온도 관련 정보 저장         |
