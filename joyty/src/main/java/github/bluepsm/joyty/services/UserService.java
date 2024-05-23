package github.bluepsm.joyty.services;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import github.bluepsm.joyty.models.File;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.FileRepository;
import github.bluepsm.joyty.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FileRepository fileRepository;

    public Optional<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return Optional.of(users);
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    @CachePut(value = "users", key = "#id")
    public Optional<User> updateUserById(Long id, User user) {
        Optional<User> userOpt = userRepository.findById(id);

        if(!userOpt.isPresent()) {
            return Optional.empty();
        }

        user.setId(id);
        
        // Keep the existing created_at timestamp
        Long createdAt = userOpt.get().getCreatedAt();
        user.setCreatedAt(createdAt);

        return Optional.of(userRepository.save(user));
    }

    @CacheEvict(value = "users", key = "#id")
    public boolean deleteUserById(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch(EmptyResultDataAccessException err) {
            return false;
        }
    }
    
    public Optional<User> updateName(Long userId, String firstName, String lastName) {
    	try {
    		User user = userRepository.findById(userId).get();
    		user.setFirstName(firstName);
    		user.setLastName(lastName);
    		
    		return Optional.of(userRepository.save(user));
    	} catch (Exception e) {
			return Optional.empty();
		}
    }
    
    public Optional<User> updateDateOfBirth(Long userId, Date dob) {
    	try {
    		User user = userRepository.findById(userId).get();
    		
    		user.setDateOfBirth(dob);
    		
    		return Optional.of(userRepository.save(user));
    	} catch (Exception e) {
			return Optional.empty();
		}
    }
    
    public Optional<User> updateGender(Long userId, String gender) {
    	try {
    		User user = userRepository.findById(userId).get();
    		user.setGender(gender);
    		
    		return Optional.of(userRepository.save(user));
    	} catch (Exception e) {
			return Optional.empty();
		}
    }
    
    public Optional<User> updatePhoneNumber(Long userId, String phoneNumber) {
    	try {
    		User user = userRepository.findById(userId).get();
    		user.setPhoneNumber(phoneNumber);
    		
    		return Optional.of(userRepository.save(user));
    	} catch (Exception e) {
			return Optional.empty();
		}
    }
    
    public Optional<User> updateLocation(Long userId, String country, String state, String city) {
    	try {
    		User user = userRepository.findById(userId).get();
    		user.setCountry(country);
    		user.setState(state);
    		user.setCity(city);
    		
    		return Optional.of(userRepository.save(user));
    	} catch (Exception e) {
			return Optional.empty();
		}
    }
    
    @Transactional
    public Optional<User> updateProfileImg(Long userId, MultipartFile image) throws IOException {
    	try {
    		User user = userRepository.findById(userId).get();
    		
    		String fileName = StringUtils.cleanPath(image.getOriginalFilename());
    		File fileDB = new File(fileName, image.getContentType(), image.getBytes());
    		fileDB.setFileOwner(user);
    		
    		try {
    			File file = fileRepository.save(fileDB);
    			user.setProfileImg(file);
    		} catch (Exception e) {
    			return Optional.empty();
    		}
    		
    		return Optional.of(userRepository.save(user));
    	} catch (Exception e) {
			return Optional.empty();
		}
    }
}
