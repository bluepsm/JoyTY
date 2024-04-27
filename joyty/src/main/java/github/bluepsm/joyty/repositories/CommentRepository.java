package github.bluepsm.joyty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
