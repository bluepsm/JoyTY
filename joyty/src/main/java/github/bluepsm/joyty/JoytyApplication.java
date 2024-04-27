package github.bluepsm.joyty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JoytyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoytyApplication.class, args);
	}

}
