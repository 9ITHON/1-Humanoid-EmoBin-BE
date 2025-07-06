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

        // 5. UTF-8로 출력 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"))) {
            return reader.lines().collect(Collectors.joining());
        }
    }
}
