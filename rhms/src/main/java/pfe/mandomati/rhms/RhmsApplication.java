package pfe.mandomati.rhms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pfe.mandomati.rhms")
public class RhmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RhmsApplication.class, args);
	}

}
