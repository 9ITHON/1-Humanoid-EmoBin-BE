# EmoBin프로젝트의 서버입니다.   
---
## ERD(초기 버전-수정 가능) 
![EmoBin_BE](https://github.com/user-attachments/assets/32ebf410-7079-4b74-8bd7-4b77bd9bcbbd)
 
|  테이블 명 | 설명                |
|---------------------|-------------|
| `members`     | 사용자 정보 (닉네임, 생년월일, 성별, 프로) 저장 |
| `emotion_history`  | 사용자의 결과를 저장     |
| `emotion_causes`    | 검사결과에서 사용되는 감정 원인이 저장      |
| `daily_summary`   | 감정 달력에서 사용하는 해당 당일의 온도 관련 정보 저장         |
| `monthly_summary`   | 감정 달력에서 사용하는 해당 달의 온도 관련 정보 저장         |
