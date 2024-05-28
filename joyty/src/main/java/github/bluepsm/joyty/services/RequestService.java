package github.bluepsm.joyty.services;

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
import org.springframework.transaction.annotation.Transactional;

import github.bluepsm.joyty.models.ERequest;
import github.bluepsm.joyty.models.Post;
import github.bluepsm.joyty.models.Request;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.PostRepository;
import github.bluepsm.joyty.repositories.RequestRepository;
import github.bluepsm.joyty.repositories.UserRepository;

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
        return requestRepository.findById(id);
    }

    @CachePut(value = "requests", key = "#id")
    public Optional<Request> updateRequestById(Long id, Request request) {
        Optional<Request> requestOpt = requestRepository.findById(id);

        if(requestOpt.isEmpty()) {
            return Optional.empty();
        }

        request.setId(id);
        
        // Keep the existing created_at timestamp
        Long createdAt = requestOpt.get().getCreatedAt();
        request.setCreatedAt(createdAt);

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
    
    @Transactional
    public Optional<Request> createRequest(Long postId, Long userId, String body) {
        Optional<Post> postOpt = postRepository.findById(postId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (postOpt.isEmpty() || userOpt.isEmpty()) {
        	return Optional.empty();
        }
        
        Request request = new Request(body);
        
        request.setJoin(postOpt.get());
        request.setOwner(userOpt.get());
        
        return Optional.of(requestRepository.save(request));
    }
    
    public Optional<List<Request>> getRequestByUserId(Long userId) {
        return requestRepository.findByOwnerId(userId);
    }
    
    public Optional<List<Request>> getRequestByPostId(Long postId) {
        return requestRepository.findByJoinId(postId);
    }
    
    @Transactional
    public Optional<Request> respondToRequest(Long requestId, String response) {
        Optional<Request> requestOpt = requestRepository.findById(requestId);
        
        if (requestOpt.isEmpty()) {
        	return Optional.empty();
        }
        
        Request request = requestOpt.get();
        Post post = request.getJoin();
        User user = request.getOwner();
        Long createdAt = request.getCreatedAt();
        Integer numberOfMember = post.getMembers().size();
        Integer partySize = post.getPartySize();

        switch (response) {
            case "ACCEPT":
                if (partySize == 0 || numberOfMember < partySize) {
                    request.setStatus(ERequest.ACCEPT);
                    post.getMembers().add(user);
                    post.setJoinner(numberOfMember + 1);
                    postRepository.save(post);
                } else {
                    //log.info("Party is full.");
                    return Optional.empty();
                };
                break;
            case "REJECT":
                request.setStatus(ERequest.REJECT);
                break;
            default:
            	//log.info("response is not matching.");
            	return Optional.empty();
        };
        
        request.setCreatedAt(createdAt);
        requestRepository.save(request);
        
        return Optional.of(request);
    }
    
    public Optional<Window<Request>> getScrollRequests(Long postId, Long latestRequest) {
    	OffsetScrollPosition offset = ScrollPosition.offset(latestRequest);
    	Optional<Window<Request>> requests = requestRepository.findFirst10ByJoinIdOrderByCreatedAtAsc(postId, offset);
    	
    	return requests;
    }
}
