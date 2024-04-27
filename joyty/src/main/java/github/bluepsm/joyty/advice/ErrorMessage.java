package github.bluepsm.joyty.advice;

import java.util.Date;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {
	private Integer statusCode;
	private Date timestamp;
	private String message;
	private String description;

	public ErrorMessage(Integer statusCode, Date timestamp, String message, String description) {
	    this.statusCode = statusCode;
	    this.timestamp = timestamp;
	    this.message = message;
	    this.description = description;
	}
}
