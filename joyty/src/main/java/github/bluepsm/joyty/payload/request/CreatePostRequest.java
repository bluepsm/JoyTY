package github.bluepsm.joyty.payload.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
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
    private String placeName;
    
	@NotBlank
    private String placeAddress;
    
    @NotNull
    private Double placeLatitude;
    
    @NotNull
    private Double placeLongtitude;
	
	@NotNull 
	private Date meetingDatetime;
	 
	@NotNull
	private Integer partySize;
	
	@NotNull 
	private BigDecimal costEstimate;
	  
	@NotNull 
	private Boolean costShare;
	  
	private Set<Long> tags;
}
