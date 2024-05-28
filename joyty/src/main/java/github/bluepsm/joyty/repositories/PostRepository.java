package github.bluepsm.joyty.repositories;

import java.util.Optional;

import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Window<Post>> findFirst5AllByOrderByCreatedAtDesc(OffsetScrollPosition position);
}