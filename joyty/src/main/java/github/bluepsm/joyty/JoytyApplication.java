package github.bluepsm.joyty;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.Scheduled;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
public class JoytyApplication {
	
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+07:00"));
		
		SpringApplication.run(JoytyApplication.class, args);
		
		//System.out.println(new Date());
	}

}
