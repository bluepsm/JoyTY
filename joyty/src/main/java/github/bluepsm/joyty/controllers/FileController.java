package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import github.bluepsm.joyty.models.File;
import github.bluepsm.joyty.payload.response.FileResponse;
import github.bluepsm.joyty.payload.response.MessageResponse;
import github.bluepsm.joyty.services.FileStorageService;

@Controller
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/file")
public class FileController {
	@Autowired
	private FileStorageService fileStorageService;
	
	@PostMapping("/upload/{userId}")
	public ResponseEntity<MessageResponse> uploadFile(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			fileStorageService.store(userId, file);
			
			message = "Uploaded file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload file: " + file.getOriginalFilename() + "!";
		    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}
	
	@GetMapping("/allFiles")
	public ResponseEntity<List<FileResponse>> getListFiles() {
		List<FileResponse> files = fileStorageService.getAllFiles().map(File -> {
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/files")
					.path(File.getId())
					.toUriString();
			return new FileResponse(
					File.getName(),
					fileDownloadUri,
					File.getType(),
					File.getData().length
					);
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}
	
	@GetMapping("/getFile/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
	    Optional<File> fileOpt = fileStorageService.getFile(id);
	    
	    if (fileOpt.isEmpty()) {
	    	return ResponseEntity.badRequest().build();
	    }

	    File file = fileOpt.get();
	    
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
	        .body(file.getData());
	  }
}
