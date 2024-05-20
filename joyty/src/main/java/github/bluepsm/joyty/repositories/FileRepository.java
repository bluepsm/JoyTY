package github.bluepsm.joyty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

}
