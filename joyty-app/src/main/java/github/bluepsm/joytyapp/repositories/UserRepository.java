package github.bluepsm.joytyapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import github.bluepsm.joytyapp.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

}
