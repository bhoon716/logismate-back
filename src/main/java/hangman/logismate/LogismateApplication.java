package hangman.logismate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hangman.logismate")
public class LogismateApplication {
	public static void main(String[] args) {
		SpringApplication.run(LogismateApplication.class, args);
	}
}
