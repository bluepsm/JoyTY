package github.bluepsm.joyty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
