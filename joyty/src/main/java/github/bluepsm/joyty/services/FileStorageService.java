package github.bluepsm.joyty.services;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import github.bluepsm.joyty.models.File;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.FileRepository;
import github.bluepsm.joyty.repositories.UserRepository;

@Service
public class FileStorageService {
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public Optional<File> store(Long userId, MultipartFile file) throws IOException {
		Optional<User> userOpt = userRepository.findById(userId);
		
		if (userOpt.isEmpty()) {
			return Optional.empty();
		}
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		File fileDB = new File(fileName, file.getContentType(), file.getBytes());
		fileDB.setFileOwner(userOpt.get());
		
		return Optional.of(fileRepository.save(fileDB));
	}
	
	public Optional<File> getFile(String id) {
		return fileRepository.findById(id);
	}
	
	public Stream<File> getAllFiles() {
		return fileRepository.findAll().stream();
	}
}
