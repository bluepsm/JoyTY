package github.bluepsm.joytyapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import github.bluepsm.joytyapp.models.PostModel;

public interface PostRepository extends JpaRepository<PostModel, Long> {

}
