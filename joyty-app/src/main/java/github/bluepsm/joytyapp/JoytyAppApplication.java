package github.bluepsm.joytyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JoytyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoytyAppApplication.class, args);
	}

}
