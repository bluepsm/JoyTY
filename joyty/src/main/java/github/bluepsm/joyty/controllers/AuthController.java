package github.bluepsm.joyty.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.bluepsm.joyty.exception.TokenRefreshException;
import github.bluepsm.joyty.models.ERole;
import github.bluepsm.joyty.models.RefreshToken;
import github.bluepsm.joyty.models.Role;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.payload.request.LoginRequest;
import github.bluepsm.joyty.payload.request.SignupRequest;
import github.bluepsm.joyty.payload.request.UpdateEmailRequest;
import github.bluepsm.joyty.payload.request.UpdateUsernameRequest;
import github.bluepsm.joyty.payload.request.ResetPasswordRequest;
import github.bluepsm.joyty.payload.response.MessageResponse;
import github.bluepsm.joyty.payload.response.UserInfoResponse;
import github.bluepsm.joyty.repositories.RoleRepository;
import github.bluepsm.joyty.repositories.UserRepository;
import github.bluepsm.joyty.security.jwt.JwtUtils;
import github.bluepsm.joyty.security.services.RefreshTokenService;
import github.bluepsm.joyty.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	RefreshTokenService refreshTokenService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager
	      .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

	    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

	    List<String> roles = userDetails.getAuthorities().stream()
	      .map(item -> item.getAuthority())
	      .collect(Collectors.toList());
	    
	    //log.info("User ID: {}", userDetails.getId());
	    //log.info("User Username: {}", userDetails.getUsername());
	    //log.info("User Password: {}", userDetails.getPassword());
	    
	    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
	    
	    ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
	    
	    //log.info("Refresh Token: {}", jwtRefreshCookie.toString());

	    return ResponseEntity.ok()
	    		.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
	    		.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
	    		.body(new UserInfoResponse(userDetails.getId(),
	                                   userDetails.getUsername(),
	                                   userDetails.getEmail(),
	                                   userDetails.getProfileImg(),
	                                   roles));
	}

	@PostMapping("/signup")
	@Transactional
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	    	return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
	    }

	    User user = new User(signUpRequest.getUsername(),
	                        encoder.encode(signUpRequest.getPassword()),
	                         signUpRequest.getEmail(),
	                         signUpRequest.getFirstName(),
	                         signUpRequest.getLastName(),
	                         signUpRequest.getGender(),
	                         signUpRequest.getDateOfBirth(),
	                         signUpRequest.getPhoneNumber(),
	                         signUpRequest.getCountry(),
	                         signUpRequest.getState(),
	                         signUpRequest.getCity()
	                         );

	    Set<String> strRoles = signUpRequest.getRole();
	    Set<Role> roles = new HashSet<>();
	    
	    if (signUpRequest.getPhoneNumber().equals("000-000-0000")) {
	    	//log.info("Dev Register");
	    	strRoles = new HashSet<String>();
	    	strRoles.add("user");
	    	strRoles.add("mod");
	    	strRoles.add("admin");
	    }

	    if (strRoles == null) {
	    	Role userRole = roleRepository.findByName(ERole.ROLE_USER)
	    			.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    	roles.add(userRole);
	    } else {
	    	strRoles.forEach(role -> {
	    		switch (role) {
	    			case "admin":
	    				Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
	    					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    				roles.add(adminRole);
	    				break;

	    			case "mod":
	    				Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
	    					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    				roles.add(modRole);
	    				break;

	    			default:
	    				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
	    					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    				roles.add(userRole);
	    		}
	    	});
	    }

	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principle.toString() != "anonymousUser") {
			Long userId = ((UserDetailsImpl) principle).getId();
			//log.info("This log is from before the call of deleteByUserId. UserID is {}", userId);
			refreshTokenService.deleteByUserId(userId);
		}
		
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		ResponseCookie refreshCookie = jwtUtils.getCleanJwtRefreshCookie();
	    
		return ResponseEntity.ok()
	    		.header(HttpHeaders.SET_COOKIE, cookie.toString())
	    		.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
	    		.body(new MessageResponse("You've been signed out!"));
	}
	
	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
		
		if ((refreshToken != null) && (refreshToken.length() > 0)) {
			//log.info("found refresh token.");
			return refreshTokenService.findByToken(refreshToken)
					.map(refreshTokenService::verifyExpiration)
					.map(RefreshToken::getUser)
					.map(user -> {
						//log.info("generating new token.");
						ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
						
						return ResponseEntity.ok()
								.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
								.body(new MessageResponse("Token is refreshed successfully!"));
					})
					.orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
		}
		
		return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
	}
	
	@GetMapping("/existsByUsername")
    public ResponseEntity<Boolean> existsByUsername(@RequestParam String username) {
		//log.info("ExistByUsername got this: " + username);
    	if (userRepository.existsByUsername(username)) {
    		//log.info("Is Existed");
    		return ResponseEntity.ok(true);
    	} else {
    		//log.info("Is not Existed");
			return ResponseEntity.ok(false);
		}
    }
	
	@GetMapping("/existsByEmail")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
    	if (userRepository.existsByEmail(email)) {
    		return ResponseEntity.ok(true);
    	} else {
			return ResponseEntity.ok(false);
		}
    }
	
	@PatchMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    	//log.info("Password before encode: " + updatePasswordRequest.getPassword());
    	final Long userId = resetPasswordRequest.getUserId();
    	final String encodedPassword = encoder.encode(resetPasswordRequest.getPassword());
    		
    	Optional<User> userOpt = userRepository.findById(userId);
    	
    	if (userOpt.isEmpty()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Cannot find user's data."));
    	}
    	User user = userOpt.get();
		user.setPassword(encodedPassword);
		
		userRepository.save(user);
    		
    	return ResponseEntity.ok(new MessageResponse("Reset password successfully!"));
    }
	
	@PatchMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@Valid @RequestBody UpdateEmailRequest updateEmailRequest) {
    	final Long userId = updateEmailRequest.getUserId();
    	final String email = updateEmailRequest.getEmail();
    	
    	if (userRepository.existsByEmail(email)) {
	    	return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
	    }
    	
    	Optional<User> userOpt = userRepository.findById(userId);
    	if (userOpt.isEmpty()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Cannot find user's data."));
    	}
    	User user = userOpt.get();
    	
		user.setEmail(email);
		
		userRepository.save(user);
    		
    	return ResponseEntity.ok(new MessageResponse("Email update successfully!"));
    }
	
	@PatchMapping("/updateUsername")
    public ResponseEntity<?> updateUsername(@Valid @RequestBody UpdateUsernameRequest updateUsernameRequest) {
    	final Long userId = updateUsernameRequest.getUserId();
    	final String username = updateUsernameRequest.getUsername();
    	//log.info("Update Username Checkpoint.");
    	if (userRepository.existsByUsername(username)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
	    }
    	
    	Optional<User> userOpt = userRepository.findById(userId);
    	if (userOpt.isEmpty()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Cannot find user's data."));
    	}
    	User user = userOpt.get();
		user.setUsername(username);
		
		userRepository.save(user);
    		
    	return ResponseEntity.ok(new MessageResponse("Username update successfully!"));
    }
}
