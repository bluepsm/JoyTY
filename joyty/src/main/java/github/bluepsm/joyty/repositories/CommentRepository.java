package github.bluepsm.joyty.repositories;

import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import github.bluepsm.joyty.models.Comment;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	Optional<List<Comment>> findByPostId(Long postId);
	Optional<Window<Comment>> findFirst10ByPostIdOrderByCreatedAtAsc(Long postId, OffsetScrollPosition position);
}
