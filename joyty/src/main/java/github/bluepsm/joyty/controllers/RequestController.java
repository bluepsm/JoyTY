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
import github.bluepsm.joyty.models.Request;
import github.bluepsm.joyty.payload.request.JoinRequest;
import github.bluepsm.joyty.security.services.UserDetailsImpl;
import github.bluepsm.joyty.services.RequestService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/request")
public class RequestController {
	@Autowired
	private RequestService requestService;

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAllRequests() {
        Optional<List<Request>> requests = requestService.getAllRequests();
        
        if(!requests.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requests.get());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long requestId) {
        Optional<Request> request = requestService.getRequestById(requestId);

        if(!request.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request.get());
    }

    @PutMapping("/{requestId}/update")
    public ResponseEntity<Request> updateRequestById(@PathVariable Long requestId, @RequestBody Request newRequest) {
        Optional<Request> updatedRequest = requestService.updateRequestById(requestId, newRequest);

        if(!updatedRequest.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedRequest.get());
    }

    @DeleteMapping("/{requestId}/delete")
    public ResponseEntity<?> deleteRequestById(@PathVariable Long requestId) {
        if(!requestService.deleteRequestById(requestId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

//    @PutMapping("/{requestId}/respond")
//    public ResponseEntity<Request> requestRespond(@PathVariable Long requestId, @RequestParam String respond) {
//        Request request = requestService.requestRespond(requestId, respond);
//
//        return ResponseEntity.ok(request);
//    }
    
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
    public ResponseEntity<Request> createRequest(@RequestBody JoinRequest joinRequest) {
    	Optional<Long> userId = getUserId();
    	
    	if (!userId.isPresent()) {
    		return ResponseEntity.internalServerError().build();
    	}
    	
        Request createdRequest = requestService.createRequest(joinRequest.getPostId(), userId.get(), joinRequest.getBody());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }
    
    @GetMapping("/getJoinRequestByUserId/{userId}")
    public ResponseEntity<List<Request>> getJoinRequestByUserId(@PathVariable Long userId) {
    	Optional<List<Request>> requests = requestService.getRequestByUserId(userId);

        if(!requests.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requests.get());
    }
    
    @GetMapping("/getJoinRequestByPostId/{postId}")
    public ResponseEntity<List<Request>> getJoinRequestByPostId(@PathVariable Long postId) {
    	Optional<List<Request>> requests = requestService.getRequestByPostId(postId);

        if(!requests.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requests.get());
    }
    
    @GetMapping("/respondToRequest/{requestId}")
    public ResponseEntity<Request> respondToRequest(@PathVariable Long requestId, @RequestParam String response) {
    	return ResponseEntity.ok(requestService.respondToRequest(requestId, response));
    }
}
