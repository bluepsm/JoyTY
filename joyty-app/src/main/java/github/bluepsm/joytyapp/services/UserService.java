package github.bluepsm.joytyapp.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joytyapp.models.PostModel;
import github.bluepsm.joytyapp.models.UserModel;
import github.bluepsm.joytyapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<List<UserModel>> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return Optional.of(users);
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public Optional<UserModel> getUserById(Long id) {
        log.info("Redis is Retrieve User ID: {}", id);
        return userRepository.findById(id);
    }

    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }

    @CachePut(value = "users", key = "#id")
    public Optional<UserModel> updateUserById(Long id, UserModel user) {
        Optional<UserModel> userOpt = userRepository.findById(id);

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

    public void addPost(Long user_id, PostModel post) {
        UserModel user = userRepository.findById(user_id).get();

        Set<PostModel> posts = new HashSet<PostModel>();
        posts.add(post);
        
        //user.setPosts(posts);
        userRepository.save(user);
    }

}
