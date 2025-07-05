package com.humanoid.emobin.infrastructure.openai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

//프론트에서 받는 것 제외 자바로 파이썬 코드 실행 테스트
public class PythonExecutorTest {

    public static void main(String[] args) {
        try {
            // 텍스트 입력
            String inputText = "요즘 너무 지치고 슬퍼";

            // analyze.py 위치 (절대경로 또는 상대경로 명확히)
            String pythonScriptPath = "src/main/java/com/humanoid/emobin/infrastructure/python/analyze.py";

            // 파이썬 명령 구성
            ProcessBuilder pb = new ProcessBuilder(
                    "venv/Scripts/python",  // Windows 기준. macOS/Linux: venv/bin/python
                    pythonScriptPath,
                    inputText
            );

            pb.environment().put("PYTHONIOENCODING", "utf-8");

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8") );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ Python 분석 결과: " + output);
            } else {
                System.out.println("❌ 실행 실패, 종료 코드: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
