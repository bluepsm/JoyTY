package github.bluepsm.joyty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
