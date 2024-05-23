package github.bluepsm.joyty.security.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import github.bluepsm.joyty.exception.TokenRefreshException;
import github.bluepsm.joyty.models.RefreshToken;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.RefreshTokenRepository;
import github.bluepsm.joyty.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RefreshTokenService {
	@Value("${joyty.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}
	
	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setUser(userRepository.findById(userId).get());
		
		ZoneId z = ZoneId.systemDefault();
		ZonedDateTime zdt = ZonedDateTime.now(z);
		ZonedDateTime zdtAdded = zdt.plus(refreshTokenDurationMs, ChronoUnit.MILLIS);
		refreshToken.setExpiryDate(zdtAdded);
		
		refreshToken.setToken(UUID.randomUUID().toString());
		
		return refreshTokenRepository.save(refreshToken);
	}
	
	public RefreshToken verifyExpiration(RefreshToken token) {
		ZoneId z = ZoneId.systemDefault();
		if (token.getExpiryDate().compareTo(ZonedDateTime.now(z)) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin");
		}
		
		return token;
	}
	
	@Transactional
	public int deleteByUserId(Long userId) {
		log.info("deletedByUserId is executed.");
		log.info("userId: {}", userId);
		Optional<User> userOptional = userRepository.findById(userId);
		
		if (!userOptional.isPresent()) {
			log.info("User not found!");
		}
		
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}
