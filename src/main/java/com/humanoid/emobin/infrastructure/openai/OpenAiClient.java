package com.humanoid.emobin.infrastructure.openai;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class OpenAiClient {

    public String analyzeEmotion(String inputText) throws IOException {

        // 1. 실행할 Python 경로와 스크립트 경로 명확히 지정
        String pythonExecutable = "venv/Scripts/python";  // macOS/Linux: venv/bin/python
        String scriptPath = "src/main/java/com/humanoid/emobin/infrastructure/python/analyze.py";

        // 2. ProcessBuilder에 정확한 경로 전달
        ProcessBuilder pb = new ProcessBuilder(pythonExecutable, scriptPath, inputText);

        // 3. 한글 출력 깨짐 방지
        pb.environment().put("PYTHONIOENCODING", "utf-8");

        // 4. stderr 출력도 같이 redirect
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Python script exited with non-zero code: " + exitCode);
            }
        } catch (InterruptedException e) {
            // 인터럽트 상태 복원
            Thread.currentThread().interrupt();
            // 내부에서 로그 찍거나 처리하고, IOException으로 변환하여 던지기
            throw new IOException("Python script interrupted during execution", e);
        }
        // 5. UTF-8로 출력 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"))) {
            String output = reader.lines().collect(Collectors.joining());
            if (output.isBlank()) {
                throw new IOException("Python script returned empty output");
            }
            return output;
        }
    }
}
