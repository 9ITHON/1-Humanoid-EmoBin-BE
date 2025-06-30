package com.humanoid.emobin;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmobinApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();

		System.setProperty("H2_DB_PATH", dotenv.get("H2_DB_PATH"));

		SpringApplication.run(EmobinApplication.class, args);
	}

}
