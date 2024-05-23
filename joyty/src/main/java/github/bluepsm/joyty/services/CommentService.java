package github.bluepsm.joyty.services;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.Comment;
import github.bluepsm.joyty.models.Post;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.payload.request.CreateCommentRequest;
import github.bluepsm.joyty.repositories.CommentRepository;
import github.bluepsm.joyty.repositories.PostRepository;
import github.bluepsm.joyty.repositories.UserRepository;

@Slf4j
@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<List<Comment>> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return Optional.of(comments);
    }

    @Cacheable(value = "comments", key = "#id", unless = "#result == null")
    public Optional<Comment> getCommentById(Long id) {
        log.info("Redis is Retrieve Comment ID: {}", id);
        return commentRepository.findById(id);
    }

    @CachePut(value = "comments", key = "#id")
    public Optional<Comment> updateCommentById(Long commentId, CreateCommentRequest commentRequest) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);

        if(!commentOpt.isPresent()) {
            return Optional.empty();
        }
        
        Comment comment = commentOpt.get();

        comment.setId(commentId);
        comment.setBody(commentRequest.getBody());
        
        // Keep the existing created_at timestamp
        Long createdAt = commentOpt.get().getCreatedAt();
        comment.setCreatedAt(createdAt);

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
    
    public Optional<Comment> createComment(Long postId, Long userId, String body) {
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findById(userId).get();
        
        Comment comment = new Comment(body);
        comment.setPost(post);
        comment.setUser(user);
        
        return Optional.of(commentRepository.save(comment));
    }
    
    public Optional<List<Comment>> getCommentsByPostId(Long postId) {
        //log.info("Redis is Retrieve Comment ID: {}", id);
        return commentRepository.findByPostId(postId);
    }
    
    public Window<Comment> getScrollComments(Long postId, Long latestComment) {
    	OffsetScrollPosition offset = ScrollPosition.offset(latestComment);
    	Window<Comment> comments = commentRepository.findFirst10ByPostIdOrderByCreatedAtAsc(postId, offset);
    	
    	return comments;
    }
}
