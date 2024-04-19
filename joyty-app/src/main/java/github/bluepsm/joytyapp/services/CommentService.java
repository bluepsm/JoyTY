package github.bluepsm.joytyapp.services;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joytyapp.models.CommentModel;
import github.bluepsm.joytyapp.models.PostModel;
import github.bluepsm.joytyapp.models.UserModel;
import github.bluepsm.joytyapp.repositories.CommentRepository;
import github.bluepsm.joytyapp.repositories.PostRepository;
import github.bluepsm.joytyapp.repositories.UserRepository;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<List<CommentModel>> getAllComments() {
        List<CommentModel> comments = commentRepository.findAll();
        return Optional.of(comments);
    }

    @Cacheable(value = "comments", key = "#id", unless = "#result == null")
    public Optional<CommentModel> getCommentById(Long id) {
        log.info("Redis is Retrieve Comment ID: {}", id);
        return commentRepository.findById(id);
    }

    public CommentModel createComment(Long postId, Long userId, CommentModel comment) {
        PostModel post = postRepository.findById(postId).get();
        UserModel user = userRepository.findById(userId).get();

        comment.setPost(post);
        comment.setUser(user);
        
        return commentRepository.save(comment);
    }

    @CachePut(value = "comments", key = "#id")
    public Optional<CommentModel> updateCommentById(Long id, CommentModel comment) {
        Optional<CommentModel> commentOpt = commentRepository.findById(id);

        if(!commentOpt.isPresent()) {
            return Optional.empty();
        }

        comment.setId(id);
        
        // Keep the existing created_at timestamp
        Long created_at = commentOpt.get().getCreated_at();
        comment.setCreated_at(created_at);

        return Optional.of(commentRepository.save(comment));
    }

    @CacheEvict(value = "comments", key = "#id")
    public boolean deleteCommentById(Long id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch(EmptyResultDataAccessException err) {
            return false;
        }
    }

}
