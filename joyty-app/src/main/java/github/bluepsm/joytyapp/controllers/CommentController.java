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

import github.bluepsm.joytyapp.models.CommentModel;
import github.bluepsm.joytyapp.services.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/all")
    public ResponseEntity<List<CommentModel>> getAllPosts() {
        Optional<List<CommentModel>> comments = commentService.getAllComments();
        
        if(!comments.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comments.get());
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentModel> getPostById(@PathVariable Long commentId) {
        Optional<CommentModel> comment = commentService.getCommentById(commentId);

        if(!comment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comment.get());
    }

    @PostMapping("/create")
    public ResponseEntity<CommentModel> createPost(@RequestParam Long postId, @RequestParam Long userId, @RequestBody CommentModel newComment) {        
        CommentModel createdComment = commentService.createComment(postId, userId, newComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{commentId}/update")
    public ResponseEntity<CommentModel> updatePostById(@PathVariable Long commentId, @RequestBody CommentModel newComment) {
        Optional<CommentModel> updatedComment = commentService.updateCommentById(commentId, newComment);

        if(!updatedComment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedComment.get());
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<?> deletePostById(@PathVariable Long commentId) {
        if(!commentService.deleteCommentById(commentId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

}
