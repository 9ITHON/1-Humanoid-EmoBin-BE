package com.humanoid.emobin;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmobinApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();

		System.setProperty("H2_DB_PATH", dotenv.get("H2_DB_PATH"));
		System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
		System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("MYSQL_SECRET", dotenv.get("MYSQL_SECRET"));
		System.setProperty("PYTHON_SCRIPT_PATH", dotenv.get("PYTHON_SCRIPT_PATH"));
		System.setProperty("PYTHON_EXEC_PATH", dotenv.get("PYTHON_EXEC_PATH"));
		System.setProperty("KAKAO_CLIENT_ID", dotenv.get("KAKAO_CLIENT_ID"));
		SpringApplication.run(EmobinApplication.class, args);
	}

}
