package github.bluepsm.joyty.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGenderRequest {
	@NotNull
	private Long userId;
	
	@NotBlank
    private String gender;
}
