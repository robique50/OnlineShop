package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.security.JwtUtil;
import ro.msg.learning.shop.service.UserService;
import ro.msg.learning.shop.dto.LoginRequest;
import ro.msg.learning.shop.dto.RegisterRequestDTO;
import ro.msg.learning.shop.dto.UserProfileDTO;
import ro.msg.learning.shop.model.User;
import ro.msg.learning.shop.security.CustomUserDetails;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Attempting authentication for user: {}", loginRequest.username());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            log.info("Authentication successful for user: {}", loginRequest.username());
            String jwt = jwtUtil.generateToken(authentication);
            return ResponseEntity.ok(jwt);

        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", loginRequest.username());
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            log.error("Error during authentication", e);
            return ResponseEntity.status(500).body("Authentication error occurred");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            log.info("Attempting registration for user: {}", registerRequest.username());
            Optional<User> registeredUserOptional = userService.registerNewUser(registerRequest);

            if (registeredUserOptional.isPresent()) {
                User registeredUser = registeredUserOptional.get();
                log.info("Registration successful for user: {}", registeredUser.getUsername());
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                log.warn("Registration failed for user: {}. Username or email already exists.", registerRequest.username());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exists.");
            }
        } catch (Exception e) { 
            log.error("Unexpected error during registration for user: {}", registerRequest.username(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during registration.");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("CUSTOMER");

        UserProfileDTO profile = new UserProfileDTO(
                userDetails.getId(),
                userDetails.getUsername(),
                role
        );

        return ResponseEntity.ok(profile);
    }
}