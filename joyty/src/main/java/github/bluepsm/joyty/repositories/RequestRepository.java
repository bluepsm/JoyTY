package github.bluepsm.joyty.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.Post;
import github.bluepsm.joyty.models.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
	Optional<List<Request>> findByOwnerId(Long userId);
	Optional<List<Request>> findByJoinId(Long postId);
	
	Window<Request> findFirst10ByJoinIdOrderByCreatedAtAsc(Long postId, OffsetScrollPosition position);
}
