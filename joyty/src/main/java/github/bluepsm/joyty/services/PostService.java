package github.bluepsm.joyty.services;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.Post;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.payload.request.CreatePostRequest;
import github.bluepsm.joyty.models.Tag;
import github.bluepsm.joyty.repositories.PostRepository;
import github.bluepsm.joyty.repositories.TagRepository;
import github.bluepsm.joyty.repositories.UserRepository;

@Slf4j
@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

//    public Optional<List<Post>> getAllPosts() {
//        List<Post> posts = postRepository.findAll();
//        return Optional.of(posts);
//    }

    @Cacheable(value = "posts", key = "#id", unless = "#result == null")
    public Optional<Post> getPostById(Long id) {
        log.info("Redis is Retrieve Post ID: {}", id);
        return postRepository.findById(id);
    }

//    public Post createPost(Long userId, Post post, Long[] tagsId) {
//        Optional<User> user = userRepository.findById(userId);
//        Set<Tag> tags = new HashSet<Tag>();
//
//        for (Long tagId : tagsId) {
//            Tag tag = tagRepository.findById(tagId).get();
//            tags.add(tag);
//        }
//
//        post.setAuthor(user.get());
//        post.setTags(tags);
//
//        return postRepository.save(post);
//    }

    @CachePut(value = "posts", key = "#postId")
    public Optional<Post> updatePostById(Long postId, Post post) {
        Optional<Post> postOpt = postRepository.findById(postId);

        if(!postOpt.isPresent()) {
            return Optional.empty();
        }

        post.setId(postId);
        
        // Keep the existing created_at timestamp
        Long created_at = postOpt.get().getCreated_at();
        post.setCreated_at(created_at);

        // Keep the post owner data
        User user = postOpt.get().getAuthor();
        post.setAuthor(user);

        // Keep tags
        Set<Tag> tags = postOpt.get().getTags();
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
    
    public Post createPost(Long userId, CreatePostRequest createPostRequest) {
    	//log.info("PostService CreatePost");
    	//log.info("UserId: " + userId);
        Optional<User> user = userRepository.findById(userId);
        
        //log.info(user.toString());
        
        Set<Tag> tags = new HashSet<Tag>();

        for (Long tagId : createPostRequest.getTags()) {
            Tag tag = tagRepository.findById(tagId).get();
            tags.add(tag);
        }
        
        //log.info(tags.toString());
        
        String body = createPostRequest.getBody();
        String meeting_location = createPostRequest.getMeeting_location();
        String meeting_city = createPostRequest.getMeeting_city();
        String meeting_state = createPostRequest.getMeeting_state();
        String meeting_country = createPostRequest.getMeeting_country();
        Date meeting_datetime = createPostRequest.getMeeting_datetime();
        Integer party_size = createPostRequest.getParty_size();
        BigDecimal cost_estimate = createPostRequest.getCost_estimate();
        Boolean cost_share = createPostRequest.getCost_share();
        
        Post post = new Post(body, party_size, meeting_location, meeting_city, meeting_state, meeting_country, meeting_datetime, cost_estimate, cost_share);
        
        post.setAuthor(user.get());
        post.setTags(tags);
        post.setMeeting_done(false);
        
        return postRepository.save(post);
    }
    
    public Optional<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return Optional.of(posts);
    }
}
