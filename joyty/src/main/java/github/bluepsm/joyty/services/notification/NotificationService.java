package github.bluepsm.joyty.services.notification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.models.notification.EEntity;
import github.bluepsm.joyty.models.notification.EType;
import github.bluepsm.joyty.models.notification.Notification;
import github.bluepsm.joyty.repositories.UserRepository;
import github.bluepsm.joyty.repositories.notification.NotificationRepository;

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public Optional<Notification> createNotification(
													Long fromUserId, 
													Long toUserId, 
													EType type, 
													EEntity entity, 
													Long entityId,
													EEntity relatedEntity, 
													Long relatedEntityId
													) {
		Notification notification = new Notification(type, entity, entityId, relatedEntity, relatedEntityId);
		
		Optional<User> fromUserOpt = userRepository.findById(fromUserId);
		Optional<User> toUserOpt = userRepository.findById(toUserId);
		
		if (fromUserOpt.isEmpty() || toUserOpt.isEmpty()) {
			return Optional.empty();
		}
		
		notification.setFromUser(fromUserOpt.get());
		
		Set<User> users = new HashSet<User>();
		users.add(toUserOpt.get());
		
		notification.setToUsers(users);

		return Optional.of(notificationRepository.save(notification));
	}
	
	public Optional<List<Notification>> getNotificationByUserId(Long userId, String[] sort) {
		List<Order> orders = new ArrayList<Order>();

        if (sort[0].contains(",")) {
          // will sort more than 2 columns
          for (String sortOrder : sort) {
            // sortOrder="column, direction"
            String[] _sort = sortOrder.split(",");
            orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
          }
        } else {
          // sort=[column, direction]
          orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }
        
		return Optional.of(notificationRepository.findByToUsersId(userId, Sort.by(orders)));
	}
	
	private Sort.Direction getSortDirection(String direction) {
    	if (direction.equals("desc")) {
    		return Sort.Direction.DESC;
    	} else if (direction.equals("asc")) {
    		return Sort.Direction.ASC;
    	}
    	
    	return Sort.Direction.DESC;
    }
    
    public Window<Notification> getScrollNotifications(Long userId, Long latestNotification) {
    	OffsetScrollPosition offset = ScrollPosition.offset(latestNotification);
    	Window<Notification> notifications = notificationRepository.findFirst10ByToUsersIdOrderByCreatedAtDesc(userId, offset);
    	
    	return notifications;
    }
}