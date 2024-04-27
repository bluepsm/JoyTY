package github.bluepsm.joyty.payload.request;

import java.sql.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	@Size(min = 3, max = 30)
    @NotBlank
    private String username;

    //@JsonIgnore
    @Size(min = 8, max = 30)
    @NotBlank
    private String password;

    @Size(max = 30)
    @Email
    @NotBlank
    private String email;

    @Size(min = 2, max = 30)
    @NotBlank
    private String first_name;

    @Size(min = 2, max = 30)
    @NotBlank
    private String last_name;

    @NotBlank
    private String gender;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date_of_birth;

    @Size(min = 12, max = 12)
    @NotBlank
    private String phone_number;

    @NotBlank
    private String country;

    @NotBlank
    private String state;

    @NotBlank
    private String city;

    private Set<String> role;
}
