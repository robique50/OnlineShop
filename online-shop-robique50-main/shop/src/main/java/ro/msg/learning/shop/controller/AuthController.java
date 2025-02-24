package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.security.JwtUtil;
import ro.msg.learning.shop.dto.LoginRequest;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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
}