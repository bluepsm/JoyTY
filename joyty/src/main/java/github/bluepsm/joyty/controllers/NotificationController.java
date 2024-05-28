package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import github.bluepsm.joyty.models.notification.Notification;
import github.bluepsm.joyty.services.notification.NotificationService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/notification")
public class NotificationController {
	@Autowired
	NotificationService notificationService;

	@GetMapping("/getNotificationByUserId/{userId}")
	public ResponseEntity<List<Notification>> getNotificationByUserId(@PathVariable Long userId, @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		Optional<List<Notification>> notifications = notificationService.getNotificationByUserId(userId, sort);
		
		if (notifications.isEmpty()) {
			return ResponseEntity.internalServerError().build();
		}
		
        return ResponseEntity.ok(notifications.get());
    }
    
    @GetMapping("/getScrollNotifications/{userId}")
    public ResponseEntity<Window<Notification>> getScrollNotifications(@PathVariable Long userId, @RequestParam(defaultValue = "0") Long latestNotification) {
    	Window<Notification> notifications = notificationService.getScrollNotifications(userId, latestNotification);

        return ResponseEntity.ok(notifications);
    }
}
