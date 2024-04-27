package github.bluepsm.joyty.controllers;

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

import github.bluepsm.joyty.models.Request;
import github.bluepsm.joyty.services.RequestService;

@RestController
@RequestMapping("/request")
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

    @PostMapping("/create")
    public ResponseEntity<Request> createRequest(@RequestParam Long postId, @RequestParam Long userId, @RequestBody Request newRequest) {        
        Request createdRequest = requestService.createRequest(postId, userId, newRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
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

    @PutMapping("/{requestId}/respond")
    public ResponseEntity<Request> requestRespond(@PathVariable Long requestId, @RequestParam String respond) {
        Request request = requestService.requestRespond(requestId, respond);

        return ResponseEntity.ok(request);
    }
}
