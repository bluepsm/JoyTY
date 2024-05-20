package github.bluepsm.joyty.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.File;
import github.bluepsm.joyty.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    
//    @Query(value = "SELECT u.profile_Img FROM users u WHERE u.id = :id")
//    Optional<File> getProfileImgById(@Param("id") Long userId);
  
    //Optional<User> updateUsername(Long userId, String username);
    //Optional<User> updateName(Long userId, String firstName, String lastName);
    //Optional<User> updateGender(Long userId, String gender);
    //Optional<User> updateDateOfBirth(Long userId, Date dateOfBirth);
    //Optional<User> updateEmail(Long userId, String email);
    //Optional<User> updatePassword(Long userId, String password);
    //Optional<User> updatePhoneNumber(Long userId, String phoneNumber);
    //Optional<User> updateLocation(Long userId, String country, String state, String city);
}
