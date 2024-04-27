package github.bluepsm.joyty.services;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.Tag;
import github.bluepsm.joyty.repositories.TagRepository;

@Slf4j
@Service
public class TagService {
	@Autowired
	private TagRepository tagRepository;

    public Optional<List<Tag>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return Optional.of(tags);
    }

    @Cacheable(value = "tags", key = "#id", unless = "#result == null")
    public Optional<Tag> getTagById(Long id) {
        log.info("Redis is Retrieve Tag ID: {}", id);
        return tagRepository.findById(id);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @CachePut(value = "tags", key = "#id")
    public Optional<Tag> updateTagById(Long id, Tag tag) {
        Optional<Tag> tagOpt = tagRepository.findById(id);

        if(!tagOpt.isPresent()) {
            return Optional.empty();
        }

        tag.setId(id);

        return Optional.of(tagRepository.save(tag));
    }

    @CacheEvict(value = "tags", key = "#id")
    public boolean deleteTagById(Long id) {
        
        try {
            tagRepository.deleteById(id);
            return true;
        } catch(EmptyResultDataAccessException err) {
            return false;
        }
    }
}
