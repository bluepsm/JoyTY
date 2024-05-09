package github.bluepsm.joyty.payload.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {
	@NotBlank
	private String body;
	
	@NotBlank
	private String meeting_location;
	
	@NotBlank
	private String meeting_city;
	
	@NotBlank
	private String meeting_state;
	
	@NotBlank
	private String meeting_country;
	
	@NotNull 
	private Date meeting_datetime;
	 
	@NotNull
	private Integer party_size;
	
	@NotNull 
	private BigDecimal cost_estimate;
	  
	@NotNull 
	private Boolean cost_share;
	  
	private Set<Long> tags;
}
