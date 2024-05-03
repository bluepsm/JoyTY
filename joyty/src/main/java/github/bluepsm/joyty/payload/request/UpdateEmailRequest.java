package github.bluepsm.joyty.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmailRequest {
	@NotNull
	private Long userId;
	
	@Size(max = 30)
    @Email
    @NotBlank
    private String email;
}
