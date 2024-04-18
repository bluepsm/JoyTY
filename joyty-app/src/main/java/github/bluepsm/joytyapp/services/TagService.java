package github.bluepsm.joytyapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joytyapp.models.TagModel;
import github.bluepsm.joytyapp.repositories.TagRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TagService {
    
    @Autowired
    private TagRepository tagRepository;

    public Optional<List<TagModel>> getAllTags() {
        List<TagModel> tags = tagRepository.findAll();
        return Optional.of(tags);
    }

    @Cacheable(value = "tags", key = "#id", unless = "#result == null")
    public Optional<TagModel> getTagById(Long id) {
        log.info("Redis is Retrieve Tag ID: {}", id);
        return tagRepository.findById(id);
    }

    public TagModel createTag(TagModel tag) {
        return tagRepository.save(tag);
    }

    @CachePut(value = "tags", key = "#id")
    public Optional<TagModel> updateTagById(Long id, TagModel tag) {
        Optional<TagModel> tagOpt = tagRepository.findById(id);

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
