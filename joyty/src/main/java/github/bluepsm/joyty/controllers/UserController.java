package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.UserRepository;
import github.bluepsm.joyty.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;
    
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
    
    @PatchMapping("/{userId}/updateUsername")
    public ResponseEntity<User> updateUsername(@PathVariable Long userId, @RequestParam String username) {
    	Optional<User> user = userService.updateUsername(userId, username);
    		
    	if (!user.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    		
    	return new ResponseEntity<User>(HttpStatus.OK);
    }
    
    @PatchMapping("/{userId}/updateName")
    public ResponseEntity<User> updateName(@PathVariable Long userId, @RequestParam String firstName, @RequestParam String lastName) {
    	Optional<User> user = userService.updateName(userId, firstName, lastName);
    		
    	if (!user.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    		
    	return new ResponseEntity<User>(HttpStatus.OK);
    }
    
    @PatchMapping("/{userId}/updateGender")
    public ResponseEntity<User> updateGender(@PathVariable Long userId, @RequestParam String gender) {
    	Optional<User> user = userService.updateGender(userId, gender);
    		
    	if (!user.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    		
    	return new ResponseEntity<User>(HttpStatus.OK);
    }
    
    @PatchMapping("/{userId}/updateEmail")
    public ResponseEntity<User> updateEmail(@PathVariable Long userId, @RequestParam String email) {
    	Optional<User> user = userService.updateEmail(userId, email);
    		
    	if (!user.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    		
    	return new ResponseEntity<User>(HttpStatus.OK);
    }
    
    @PatchMapping("/{userId}/updatePhoneNumber")
    public ResponseEntity<User> updatePhoneNumber(@PathVariable Long userId, @RequestParam String phoneNumber) {
    	Optional<User> user = userService.updatePhoneNumber(userId, phoneNumber);
    		
    	if (!user.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    		
    	return new ResponseEntity<User>(HttpStatus.OK);
    }
    
    @PatchMapping("/{userId}/updateLocation")
    public ResponseEntity<User> updateLocation(@PathVariable Long userId, @RequestParam String country, @RequestParam String state,@RequestParam String city) {
    	Optional<User> user = userService.updateLocation(userId, country, state, city);
    		
    	if (!user.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    		
    	return new ResponseEntity<User>(HttpStatus.OK);
    }
    
}
