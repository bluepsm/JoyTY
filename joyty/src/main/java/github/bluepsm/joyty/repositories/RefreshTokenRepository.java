package github.bluepsm.joyty.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import github.bluepsm.joyty.models.RefreshToken;
import github.bluepsm.joyty.models.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);
	int deleteByUser(User user);
}
