package github.bluepsm.joyty.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import github.bluepsm.joyty.models.File;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.payload.request.ResetPasswordRequest;
import github.bluepsm.joyty.payload.request.UpdateDobRequest;
import github.bluepsm.joyty.payload.request.UpdateGenderRequest;
import github.bluepsm.joyty.payload.request.UpdateLocationRequest;
import github.bluepsm.joyty.payload.request.UpdateNameRequest;
import github.bluepsm.joyty.payload.request.UpdatePhoneNumberRequest;
import github.bluepsm.joyty.payload.response.MessageResponse;
import github.bluepsm.joyty.repositories.UserRepository;
import github.bluepsm.joyty.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	PasswordEncoder encoder;
    
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        Optional<List<User>> users = userService.getAllUsers();

        if(!users.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(users.get());
    }
    
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);

        if(!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        User createdUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId, @RequestBody User newUser) {
        Optional<User> updatedUser = userService.updateUserById(userId, newUser);

        if(!updatedUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUser.get());
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        if(!userService.deleteUserById(userId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/updateName")
    public ResponseEntity<?> updateName(@Valid @RequestBody UpdateNameRequest updateNameRequest) {
    	final Long userId = updateNameRequest.getUserId();
    	final String firstName = updateNameRequest.getFirstName();
    	final String lastName = updateNameRequest.getLastName();
    	
    	Optional<User> user = userService.updateName(userId, firstName, lastName);
    	
    	if (!user.isPresent()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot update name!"));
    	}
    		
    	return ResponseEntity.ok(new MessageResponse("Name update successfully!"));
    }
    
    @PatchMapping("/updateDateOfBirth")
    public ResponseEntity<?> updateDateOfBirth(@Valid @RequestBody UpdateDobRequest updateDobRequest) {
    	final Long userId = updateDobRequest.getUserId();
    	final Date dob = updateDobRequest.getDateOfBirth();
    	
    	Optional<User> user = userService.updateDateOfBirth(userId, dob);
    	
    	if (!user.isPresent()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot update date of birth!"));
    	}
    		
    	return ResponseEntity.ok(new MessageResponse("Date of birth update successfully!"));
    }
    
    @PatchMapping("/updateGender")
    public ResponseEntity<?> updateGender(@Valid @RequestBody UpdateGenderRequest updateGenderRequest) {
    	final Long userId = updateGenderRequest.getUserId();
    	final String gender = updateGenderRequest.getGender();
    	
    	Optional<User> user = userService.updateGender(userId, gender);
    		
    	if (!user.isPresent()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot update gender!"));
    	}
    		
    	return ResponseEntity.ok(new MessageResponse("Gender update successfully!"));
    }
    
    @PatchMapping("/updatePhoneNumber")
    public ResponseEntity<?> updatePhoneNumber(@Valid @RequestBody UpdatePhoneNumberRequest updatePhoneNumberRequest) {
    	final Long userId = updatePhoneNumberRequest.getUserId();
    	final String phoneNumber = updatePhoneNumberRequest.getPhoneNumber();
    	
    	Optional<User> user = userService.updatePhoneNumber(userId, phoneNumber);
    		
    	if (!user.isPresent()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot update phone number!"));
    	}
    		
    	return ResponseEntity.ok(new MessageResponse("Phone number update successfully!"));
    }
    
    @PatchMapping("/updateLocation")
    public ResponseEntity<?> updateLocation(@Valid @RequestBody UpdateLocationRequest updateLocationRequest) {
    	final Long userId = updateLocationRequest.getUserId();
    	final String country = updateLocationRequest.getCountry();
    	final String state = updateLocationRequest.getState();
    	final String city = updateLocationRequest.getCity();
    	
    	Optional<User> user = userService.updateLocation(userId, country, state, city);
    		
    	if (!user.isPresent()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot update location!"));
    	}
    		
    	return ResponseEntity.ok(new MessageResponse("Location update successfully!"));
    }
    
    @PatchMapping("/updateProfileImg/{userId}")
    public ResponseEntity<?> updateProfileImg(@PathVariable Long userId, @RequestParam("image") MultipartFile image) {
    	try {
    		Optional<User> user = userService.updateProfileImg(userId, image);
    		
    		if (!user.isPresent()) {
        		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot update profile image!"));
        	}
        		
        	return ResponseEntity.ok(user.get().getProfileImg());
    	} catch(Exception e) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot update profile image!"));
    	}
    }
    
//    @GetMapping("/getProfileImgById/{userId}")
//    public ResponseEntity<?> getProfileImgById(@PathVariable Long userId) {
//    	Optional<File> img = this.userService.getProfileImgById(userId);
//    	
//    	if (!img.isPresent()) {
//    		return ResponseEntity.notFound().build();
//    	}
//    	
//    	return ResponseEntity.ok(img.get());
//    }
}
