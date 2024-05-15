package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import github.bluepsm.joyty.models.Comment;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.models.notification.EEntity;
import github.bluepsm.joyty.models.notification.EType;
import github.bluepsm.joyty.payload.request.CreateCommentRequest;
import github.bluepsm.joyty.security.services.UserDetailsImpl;
import github.bluepsm.joyty.services.CommentService;
import github.bluepsm.joyty.services.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/comment")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private NotificationService notificationService;

    @GetMapping("/all")
    public ResponseEntity<List<Comment>> getAllComments() {
        Optional<List<Comment>> comments = commentService.getAllComments();
        
        if(!comments.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comments.get());
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
        Optional<Comment> comment = commentService.getCommentById(commentId);

        if(!comment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comment.get());
    }

//    @PostMapping("/create")
//    public ResponseEntity<Comment> createComment(@RequestParam Long postId, @RequestParam Long userId, @RequestBody Comment newComment) {        
//        Comment createdComment = commentService.createComment(postId, userId, newComment);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
//    }

    @PutMapping("/{commentId}/update")
    public ResponseEntity<Comment> updateCommentById(@PathVariable Long commentId, @RequestBody Comment newComment) {
        Optional<Comment> updatedComment = commentService.updateCommentById(commentId, newComment);

        if(!updatedComment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedComment.get());
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long commentId) {
        if(!commentService.deleteCommentById(commentId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
    
    private Optional<Long> getUserId() {
    	Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principle.toString() != "anonymousUser") {
			Long userId = ((UserDetailsImpl) principle).getId();
			return Optional.of(userId);
		} else {
			return Optional.empty();
		}
    }
    
    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody CreateCommentRequest commentRequest) {
    	Optional<Long> userId = getUserId();
    	
    	if (!userId.isPresent()) {
    		return ResponseEntity.internalServerError().build();
    	}
    		
        Optional<Comment> createdComment = commentService.createComment(commentRequest.getPostId(), userId.get(), commentRequest.getBody());

        if (!createdComment.isPresent()) {
    		return ResponseEntity.internalServerError().build();
    	}
        
        Comment comment = createdComment.get();
        
        Long toUserId = comment.getPost().getAuthor().getId();
        
        notificationService.createNotification(
								        		userId.get(), 
								        		toUserId, 
								        		EType.TYPE_COMMENT, 
								        		EEntity.ENTITY_COMMENT, 
								        		comment.getId(),
								        		EEntity.ENTITY_POST,
								        		comment.getPost().getId()
								        	);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
    
    @GetMapping("/getCommentByPost/{postId}")
    public ResponseEntity<List<Comment>> getCommentByPostId(@PathVariable Long postId) {
    	Optional<List<Comment>> comments = commentService.getCommentsByPostId(postId);

        if(!comments.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comments.get());
    }
}
