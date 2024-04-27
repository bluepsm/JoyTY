package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.services.UserService;

@RestController
@RequestMapping("/user")
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
}
