package pfe.mandomati.rhms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RhmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RhmsApplication.class, args);
	}

}
