package github.bluepsm.joyty.services;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.ERequest;
import github.bluepsm.joyty.models.Post;
import github.bluepsm.joyty.models.Request;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.PostRepository;
import github.bluepsm.joyty.repositories.RequestRepository;
import github.bluepsm.joyty.repositories.UserRepository;

@Slf4j
@Service
public class RequestService {
	@Autowired
	private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public Optional<List<Request>> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return Optional.of(requests);
    }

    @Cacheable(value = "requests", key = "#id", unless = "#result == null")
    public Optional<Request> getRequestById(Long id) {
        log.info("Redis is Retrieve Request ID: {}", id);
        return requestRepository.findById(id);
    }

    @CachePut(value = "requests", key = "#id")
    public Optional<Request> updateRequestById(Long id, Request request) {
        Optional<Request> requestOpt = requestRepository.findById(id);

        if(!requestOpt.isPresent()) {
            return Optional.empty();
        }

        request.setId(id);
        
        // Keep the existing created_at timestamp
        Long created_at = requestOpt.get().getCreated_at();
        request.setCreated_at(created_at);

        return Optional.of(requestRepository.save(request));
    }

    @CacheEvict(value = "requests", key = "#id")
    public boolean deleteRequestById(Long id) {
        try {
            requestRepository.deleteById(id);
            return true;
        } catch(EmptyResultDataAccessException err) {
            return false;
        }
    }

    public Request requestRespond(Long requestId, String respond) {
        Request request = requestRepository.findById(requestId).get();
        Post post = request.getJoin();
        User user = request.getOwner();
        Integer numberOfMember = post.getParty_member().size();
        Integer partySize = post.getParty_size();

        switch (respond) {
            case "accept":
                if (partySize == 0 || numberOfMember < partySize) {
                    request.setStatus(ERequest.ACCEPT);
                    post.getParty_member().add(user);
                    postRepository.save(post);
                } else {
                    log.info("Party is full.");
                };
                break;
            case "reject":
                request.setStatus(ERequest.REJECT);
                break;
            default:
                log.info("response is not matching.");
        };

        return request;
    }
    
    public Request createRequest(Long postId, Long userId, String body) {
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findById(userId).get();
        
        Request request = new Request(body);
        
        request.setJoin(post);
        request.setOwner(user);
        
        return requestRepository.save(request);
    }
    
    public Optional<List<Request>> getRequestByUserId(Long userId) {
        //log.info("Redis is Retrieve Request ID: {}", id);
        return requestRepository.findByOwnerId(userId);
    }
}
