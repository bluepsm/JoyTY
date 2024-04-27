package github.bluepsm.joyty.models;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "refreshtoken")
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@Column(nullable = false)
	private Instant expiryDate;
	
	public RefreshToken() {}
}
