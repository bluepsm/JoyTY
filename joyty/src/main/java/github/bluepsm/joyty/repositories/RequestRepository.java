package github.bluepsm.joyty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

}
