package github.bluepsm.joyty.payload.response;

import java.util.List;

import github.bluepsm.joyty.models.File;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
	private Long id;
    private String username;
    private String email;
    private File profileImg;
    private List<String> roles;

    public UserInfoResponse(Long id, String username, String email, File profileImg, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImg = profileImg;
        this.roles = roles;
    }
}
