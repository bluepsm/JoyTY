package github.bluepsm.joytyapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import github.bluepsm.joytyapp.models.PostModel;
import github.bluepsm.joytyapp.services.PostService;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/all")
    public ResponseEntity<List<PostModel>> getAllPosts() {
        Optional<List<PostModel>> posts = postService.getAllPosts();
        
        if(!posts.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(posts.get());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostModel> getPostById(@PathVariable Long postId) {
        Optional<PostModel> post = postService.getPostById(postId);

        if(!post.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(post.get());
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<PostModel> createPost(@PathVariable Long userId, @RequestBody PostModel newPost, @RequestParam Long... tags) {
        
        PostModel createdPost = postService.createPost(userId, newPost, tags);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{postId}/update")
    public ResponseEntity<PostModel> updatePostById(@PathVariable Long postId, @RequestBody PostModel newPost) {
        Optional<PostModel> updatedPost = postService.updatePostById(postId, newPost);

        if(!updatedPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPost.get());
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<?> deletePostById(@PathVariable Long postId) {
        if(!postService.deletePostById(postId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

}
