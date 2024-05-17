package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.bluepsm.joyty.models.Comment;
import github.bluepsm.joyty.models.notification.EEntity;
import github.bluepsm.joyty.models.notification.EType;
import github.bluepsm.joyty.models.notification.Notification;
import github.bluepsm.joyty.services.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/notification")
public class NotificationController {
	@Autowired
	NotificationService notificationService;
	
//	@GetMapping("/test")
//	public ResponseEntity<Notification> testNotification() {
//		Optional<Notification> notification = notificationService.createNotification(1L, 2L, EType.TYPE_SEND_REQUEST, EEntity.ENTITY_REQUEST, 1L);
//		
//		if (!notification.isPresent()) {
//			return ResponseEntity.internalServerError().build();
//		}
//		
//        return ResponseEntity.ok(notification.get());
//    }

	@GetMapping("/getNotificationByUserId/{userId}")
	public ResponseEntity<List<Notification>> getNotificationByUserId(@PathVariable Long userId, @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		Optional<List<Notification>> notifications = notificationService.getNotificationByUserId(userId, sort);
		
		if (!notifications.isPresent()) {
			return ResponseEntity.internalServerError().build();
		}
		
        return ResponseEntity.ok(notifications.get());
    }
}
