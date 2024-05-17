package github.bluepsm.joyty.repositories.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.notification.Notification;
import github.bluepsm.joyty.models.User;
import java.util.Set;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByToUsersId(Long userId, Sort sort);
}
