package com.humanoid.emobin.infrastructure.python.movie;

import com.humanoid.emobin.global.exception.CustomException;
import com.humanoid.emobin.global.response.EmotionErrorCode;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PythonExecutor {

    @Value("${env.PYTHON_RECOMMEND_MOVIE_SCRIPT_PATH}")
    private String recommendScriptPath;

    @Value("${env.PYTHON_EXEC_PATH}")
    private String pythonExecPath;

    public List<String> runRecommendScript(String emotion, String message, String genre, int count) {
        List<String> result = new ArrayList<>();

        log.info("📍 Python 실행기 경로: {}", pythonExecPath);
        log.info("📍 Python 추천 스크립트 경로: {}", recommendScriptPath);

        try {
            // 1. 인자 구성
            List<String> command = new ArrayList<>();
            command.add(pythonExecPath);
            command.add(recommendScriptPath);
            command.add(emotion != null ? emotion : "");
            command.add(message != null ? message : "");
            command.add(genre != null ? genre : "");
            command.add(String.valueOf(count));

            // 2. 프로세스 실행
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.environment().put("PYTHONIOENCODING", "utf-8");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 3. 결과 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.add(line.trim());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Python recommend script exited with code {}", exitCode);
                throw new CustomException(EmotionErrorCode.PYTHON_EXECUTION_ERROR);
            }
        } catch (Exception e) {
            log.error("Error executing Python script", e);
            throw new CustomException(EmotionErrorCode.PYTHON_EXECUTION_ERROR);
        }

        log.info("✅ 추천 영화 결과: {}", result);
        return result;
    }
}