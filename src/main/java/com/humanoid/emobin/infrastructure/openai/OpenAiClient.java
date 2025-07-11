package com.humanoid.emobin.infrastructure.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class OpenAiClient {

    @Value("${env.PYTHON_ANALYZE_SCRIPT_PATH}")
    private String scriptPath;

    @Value("${env.PYTHON_EXEC_PATH}")
    private String pythonExecutable;

    public String analyzeEmotion(String inputText) throws IOException {

        System.out.println("[INFO] Python 실행 경로: " + pythonExecutable);
        System.out.println("[INFO] Python 스크립트 경로: " + scriptPath);
        System.out.println("[INFO] 입력된 텍스트: " + inputText);

        ProcessBuilder pb = new ProcessBuilder(pythonExecutable, scriptPath, inputText);
        pb.environment().put("PYTHONIOENCODING", "utf-8");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"))) {
            output = reader.lines().collect(Collectors.joining("\n"));
            System.out.println("[PYTHON OUTPUT]");
            System.out.println(output);
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("[ERROR] Python script exited with code: " + exitCode);
                throw new IOException("Python script exited with non-zero code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Python script interrupted during execution", e);
        }

        if (output.isBlank()) {
            throw new IOException("Python script returned empty output");
        }

        return output;
    }
}
