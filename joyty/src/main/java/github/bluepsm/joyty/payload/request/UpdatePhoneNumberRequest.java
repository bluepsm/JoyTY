package github.bluepsm.joyty.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePhoneNumberRequest {
	@NotNull
	private Long userId;
	
	@Size(min = 12, max = 12)
    @NotBlank
    private String phoneNumber;
}
