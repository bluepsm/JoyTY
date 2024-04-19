package github.bluepsm.joytyapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import github.bluepsm.joytyapp.models.CommentModel;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {

}
