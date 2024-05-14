package github.bluepsm.joyty.services.notification;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.Tag;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.models.notification.EEntity;
import github.bluepsm.joyty.models.notification.EType;
import github.bluepsm.joyty.models.notification.Notification;
import github.bluepsm.joyty.repositories.UserRepository;
import github.bluepsm.joyty.repositories.notification.NotificationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Optional<Notification> createNotification(Long fromUserId, Long toUserId, EType type, EEntity entity, Long entityId) {
		Notification notification = new Notification(type, entity, entityId);
		
		//log.info(notification.toString());
		
		User fromUser = userRepository.findById(fromUserId).get();
		notification.setFromUser(fromUser);
		
		User toUser = userRepository.findById(toUserId).get();
		
		Set<User> users = new HashSet<User>();
		users.add(toUser);
		
		notification.setToUsers(users);

		//log.info(notification.toString());
		
		return Optional.of(notificationRepository.save(notification));
	}
	
	public Optional<List<Notification>> getNotificationByUserId(Long userId) {
		return Optional.of(notificationRepository.findByToUsersId(userId));
	}
}