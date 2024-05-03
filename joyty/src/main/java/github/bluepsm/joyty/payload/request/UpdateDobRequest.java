package github.bluepsm.joyty.payload.request;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDobRequest {
	@NotNull
	private Long userId;
	
	@NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_of_birth;
}
