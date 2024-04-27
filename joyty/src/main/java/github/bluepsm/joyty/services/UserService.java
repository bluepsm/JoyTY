package github.bluepsm.joyty.services;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.UserRepository;

@Slf4j
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

    public Optional<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return Optional.of(users);
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public Optional<User> getUserById(Long id) {
        log.info("Redis is Retrieve User ID: {}", id);
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
        Long created_at = userOpt.get().getCreated_at();
        user.setCreated_at(created_at);

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
}
