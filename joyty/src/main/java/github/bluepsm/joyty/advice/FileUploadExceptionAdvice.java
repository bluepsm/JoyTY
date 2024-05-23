package github.bluepsm.joyty.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import github.bluepsm.joyty.payload.response.MessageResponse;

@ControllerAdvice
public class FileUploadExceptionAdvice {
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<MessageResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File too large!"));
	}
}
