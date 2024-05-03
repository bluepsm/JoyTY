package github.bluepsm.joyty.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNameRequest {
	@NotNull
	private Long userId;
	
	@Size(min = 2, max = 30)
    @NotBlank
    private String first_name;

    @Size(min = 2, max = 30)
    @NotBlank
    private String last_name;
}
