package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import github.bluepsm.joyty.models.Tag;
import github.bluepsm.joyty.services.TagService;

@RestController
@RequestMapping("/tag")
public class TagController {
	@Autowired
	private TagService tagService;

    @GetMapping("/all")
    public ResponseEntity<List<Tag>> getAllTags() {
        Optional<List<Tag>> tags = tagService.getAllTags();
        
        if(!tags.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tags.get());
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long tagId) {
        Optional<Tag> tag = tagService.getTagById(tagId);

        if(!tag.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tag.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Tag> createTag(@RequestBody Tag newTag) {
        Tag createdTag = tagService.createTag(newTag);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @PutMapping("/{tagId}/update")
    public ResponseEntity<Tag> updateTagById(@PathVariable Long tagId, @RequestBody Tag newTag) {
        Optional<Tag> updatedTag = tagService.updateTagById(tagId, newTag);

        if(!updatedTag.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTag.get());
    }

    @DeleteMapping("/{tagId}/delete")
    public ResponseEntity<?> deleteTagById(@PathVariable Long tagId) {
        if(!tagService.deleteTagById(tagId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
