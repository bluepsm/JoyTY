package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Window;
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

import github.bluepsm.joyty.models.Request;
import github.bluepsm.joyty.models.notification.EEntity;
import github.bluepsm.joyty.models.notification.EType;
import github.bluepsm.joyty.payload.request.JoinRequest;
import github.bluepsm.joyty.security.services.UserDetailsImpl;
import github.bluepsm.joyty.services.RequestService;
import github.bluepsm.joyty.services.notification.NotificationService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/request")
public class RequestController {
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private NotificationService notificationService;

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAllRequests() {
        Optional<List<Request>> requests = requestService.getAllRequests();
        
        if(requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requests.get());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long requestId) {
        Optional<Request> request = requestService.getRequestById(requestId);

        if(request.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request.get());
    }

    @PutMapping("/{requestId}/update")
    public ResponseEntity<Request> updateRequestById(@PathVariable Long requestId, @RequestBody Request newRequest) {
        Optional<Request> updatedRequest = requestService.updateRequestById(requestId, newRequest);

        if(updatedRequest.isEmpty()) {
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
    	
    	if (userId.isEmpty()) {
    		return ResponseEntity.internalServerError().build();
    	}
    	
        Optional<Request> createdRequest = requestService.createRequest(joinRequest.getPostId(), userId.get(), joinRequest.getBody());

        if (createdRequest.isEmpty()) {
    		return ResponseEntity.internalServerError().build();
    	}
        
        Request request = createdRequest.get();
        
        Long toUserId = request.getJoin().getAuthor().getId();
        
        notificationService.createNotification(
								        		userId.get(), 
								        		toUserId, 
								        		EType.TYPE_SEND_REQUEST, 
								        		EEntity.ENTITY_REQUEST, 
								        		request.getId(),
								        		EEntity.ENTITY_POST,
								        		request.getJoin().getId()
								        		);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }
    
    @GetMapping("/getJoinRequestByUserId/{userId}")
    public ResponseEntity<List<Request>> getJoinRequestByUserId(@PathVariable Long userId) {
    	Optional<List<Request>> requests = requestService.getRequestByUserId(userId);

        if(requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requests.get());
    }
    
    @GetMapping("/getJoinRequestByPostId/{postId}")
    public ResponseEntity<List<Request>> getJoinRequestByPostId(@PathVariable Long postId) {
    	Optional<List<Request>> requests = requestService.getRequestByPostId(postId);

        if(requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requests.get());
    }
    
    @GetMapping("/respondToRequest/{requestId}")
    public ResponseEntity<Request> respondToRequest(@PathVariable Long requestId, @RequestParam String response) {
    	Optional<Request> requestResponded = requestService.respondToRequest(requestId, response);
    	
    	if (requestResponded.isEmpty()) {
    		return ResponseEntity.internalServerError().build();
    	}
        
        Request request = requestResponded.get();
        
        Long toUserId = request.getOwner().getId();
        
        switch (response) {
	        case "ACCEPT":
	        	notificationService.createNotification(
									        			request.getJoin().getAuthor().getId(), 
									        			toUserId, 
									        			EType.TYPE_ACCEPT_REQUEST, 
									        			EEntity.ENTITY_REQUEST, 
									        			request.getId(),
									        			EEntity.ENTITY_POST,
									        			request.getJoin().getId()
									        			);
	            break;
	        case "REJECT":
	        	notificationService.createNotification(
									        			request.getJoin().getAuthor().getId(), 
									        			toUserId, 
									        			EType.TYPE_REJECT_REQUEST, 
									        			EEntity.ENTITY_REQUEST, 
									        			request.getId(),
									        			EEntity.ENTITY_POST,
									        			request.getJoin().getId()
									        			);
	            break;
        }
        
    	return ResponseEntity.ok(request);
    }
    
    @GetMapping("/getScrollRequests/{postId}")
    public ResponseEntity<Window<Request>> getScrollRequests(@PathVariable Long postId, @RequestParam(defaultValue = "0") Long latestRequest) {
    	Optional<Window<Request>> requestsOpt = requestService.getScrollRequests(postId, latestRequest);

    	if (requestsOpt.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	}
    	
        return ResponseEntity.ok(requestsOpt.get());
    }
}
