package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Window;
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

import github.bluepsm.joyty.models.Post;
import github.bluepsm.joyty.payload.request.CreatePostRequest;
import github.bluepsm.joyty.payload.response.MessageResponse;
import github.bluepsm.joyty.security.services.UserDetailsImpl;
import github.bluepsm.joyty.services.PostService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/post")
public class PostController {
	@Autowired
	private PostService postService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
    	//log.info("In PostController CreatePost");
    	Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principle.toString() != "anonymousUser") {
			Long userId = ((UserDetailsImpl) principle).getId();
			postService.createPost(userId, createPostRequest);
			//log.info("In PostController CreatePost: Checkpoint");
		} else {
			//log.info("In PostController CreatePost: Error occured");
			return ResponseEntity.badRequest().body(new MessageResponse("No Authorized"));
		}
    	
    	return ResponseEntity.ok().build();
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
    	Optional<List<Post>> posts = postService.getAllPosts(sort);
        
        if(posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(posts.get());
    }
    
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Optional<Post> post = postService.getPostById(postId);

        if(post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(post.get());
    }
    
    @DeleteMapping("/deleteByPostId/{postId}")
    public ResponseEntity<?> deletePostById(@PathVariable Long postId) {
        if(!postService.deletePostById(postId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/update/{postId}")
    public ResponseEntity<Post> updatePostById(@PathVariable Long postId, @Valid @RequestBody CreatePostRequest updatePostRequest) {
        Optional<Post> updatedPost = postService.updatePost(postId, updatePostRequest);

        if(updatedPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPost.get());
    }
    
    @GetMapping("/getScrollPosts")
    public ResponseEntity<Window<Post>> getScrollPosts(@RequestParam(defaultValue = "0") Long latestPost) {
    	Optional<Window<Post>> postsOpt = postService.getScrollPosts(latestPost);
    	
    	if(postsOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    	
    	return ResponseEntity.ok(postsOpt.get());
    }
}
