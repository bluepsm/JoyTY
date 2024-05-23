package github.bluepsm.joyty;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JoytyApplication {
	
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+07:00"));
		SpringApplication.run(JoytyApplication.class, args);
	}
}
