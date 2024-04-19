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

import github.bluepsm.joytyapp.models.RequestModel;
import github.bluepsm.joytyapp.services.RequestService;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/all")
    public ResponseEntity<List<RequestModel>> getAllRequests() {
        Optional<List<RequestModel>> requests = requestService.getAllRequests();
        
        if(!requests.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(requests.get());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestModel> getRequestById(@PathVariable Long requestId) {
        Optional<RequestModel> request = requestService.getRequestById(requestId);

        if(!request.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request.get());
    }

    @PostMapping("/create")
    public ResponseEntity<RequestModel> createRequest(@RequestParam Long postId, @RequestParam Long userId, @RequestBody RequestModel newRequest) {        
        RequestModel createdRequest = requestService.createRequest(postId, userId, newRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @PutMapping("/{requestId}/update")
    public ResponseEntity<RequestModel> updateRequestById(@PathVariable Long requestId, @RequestBody RequestModel newRequest) {
        Optional<RequestModel> updatedRequest = requestService.updateRequestById(requestId, newRequest);

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
    public ResponseEntity<RequestModel> requestRespond(@PathVariable Long requestId, @RequestParam String respond) {
        RequestModel request = requestService.requestRespond(requestId, respond);

        return ResponseEntity.ok(request);
    }
    
}
