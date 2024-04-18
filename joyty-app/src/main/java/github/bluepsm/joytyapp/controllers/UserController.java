package github.bluepsm.joytyapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.bluepsm.joytyapp.models.UserModel;
import github.bluepsm.joytyapp.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        Optional<List<UserModel>> users = userService.getAllUsers();

        if(!users.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(users.get());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long userId) {
        Optional<UserModel> user = userService.getUserById(userId);

        if(!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/create")
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel newUser) {
        UserModel createdUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<UserModel> updateUserById(@PathVariable Long userId, @RequestBody UserModel newUser) {
        Optional<UserModel> updatedUser = userService.updateUserById(userId, newUser);

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
