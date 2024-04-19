package github.bluepsm.joytyapp.services;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joytyapp.models.PostModel;
import github.bluepsm.joytyapp.models.RequestModel;
import github.bluepsm.joytyapp.models.UserModel;
import github.bluepsm.joytyapp.repositories.PostRepository;
import github.bluepsm.joytyapp.repositories.RequestRepository;
import github.bluepsm.joytyapp.repositories.UserRepository;

@Slf4j
@Service
public class RequestService {

    enum Status {
        PENDING,
        ACCEPT,
        REJECT
    }

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public Optional<List<RequestModel>> getAllRequests() {
        List<RequestModel> requests = requestRepository.findAll();
        return Optional.of(requests);
    }

    @Cacheable(value = "requests", key = "#id", unless = "#result == null")
    public Optional<RequestModel> getRequestById(Long id) {
        log.info("Redis is Retrieve Request ID: {}", id);
        return requestRepository.findById(id);
    }

    public RequestModel createRequest(Long postId, Long userId, RequestModel request) {
        PostModel post = postRepository.findById(postId).get();
        UserModel user = userRepository.findById(userId).get();

        request.setRequest_to(post);
        request.setRequest_by(user);
        
        return requestRepository.save(request);
    }

    @CachePut(value = "requests", key = "#id")
    public Optional<RequestModel> updateRequestById(Long id, RequestModel request) {
        Optional<RequestModel> requestOpt = requestRepository.findById(id);

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

    public RequestModel requestRespond(Long requestId, String respond) {
        RequestModel request = requestRepository.findById(requestId).get();
        PostModel post = request.getRequest_to();
        UserModel user = request.getRequest_by();
        Integer numberOfMember = post.getParty_member().size();
        Integer partySize = post.getParty_size();

        switch (respond) {
            case "accept":
                if (partySize == 0 || numberOfMember < partySize) {
                    request.setStatus(Status.ACCEPT.toString());
                    post.getParty_member().add(user);
                    postRepository.save(post);
                } else {
                    log.info("Party is full.");
                };
                break;
            case "reject":
                request.setStatus(Status.REJECT.toString());
                break;
            default:
                log.info("response is not matching.");
        };

        return request;
    }

}
