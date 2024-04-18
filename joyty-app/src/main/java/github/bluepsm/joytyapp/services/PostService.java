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
import github.bluepsm.joytyapp.models.TagModel;
import github.bluepsm.joytyapp.repositories.PostRepository;
import github.bluepsm.joytyapp.repositories.UserRepository;
import github.bluepsm.joytyapp.repositories.TagRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    public Optional<List<PostModel>> getAllPosts() {
        List<PostModel> posts = postRepository.findAll();
        return Optional.of(posts);
    }

    @Cacheable(value = "posts", key = "#id", unless = "#result == null")
    public Optional<PostModel> getPostById(Long id) {
        log.info("Redis is Retrieve Post ID: {}", id);
        return postRepository.findById(id);
    }

    public PostModel createPost(Long userId, PostModel post, Long[] tagsId) {
        Optional<UserModel> user = userRepository.findById(userId);
        Set<TagModel> tags = new HashSet<TagModel>();

        for (Long tagId : tagsId) {
            TagModel tag = tagRepository.findById(tagId).get();
            tags.add(tag);
        }

        post.setUser(user.get());
        post.setTags(tags);

        return postRepository.save(post);
    }

    @CachePut(value = "posts", key = "#postId")
    public Optional<PostModel> updatePostById(Long postId, PostModel post) {
        Optional<PostModel> postOpt = postRepository.findById(postId);

        if(!postOpt.isPresent()) {
            return Optional.empty();
        }

        post.setId(postId);
        
        // Keep the existing created_at timestamp
        Long created_at = postOpt.get().getCreated_at();
        post.setCreated_at(created_at);

        // Keep the post owner data
        UserModel user = postOpt.get().getUser();
        post.setUser(user);

        // Keep tags
        Set<TagModel> tags = postOpt.get().getTags();
        post.setTags(tags);

        return Optional.of(postRepository.save(post));
    }

    @CacheEvict(value = "posts", key = "#id")
    public boolean deletePostById(Long id) {
        
        try {
            postRepository.deleteById(id);
            return true;
        } catch(EmptyResultDataAccessException err) {
            return false;
        }
    }

}
