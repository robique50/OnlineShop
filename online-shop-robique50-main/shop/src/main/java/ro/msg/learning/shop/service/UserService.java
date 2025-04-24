// filepath: shop/src/main/java/ro/msg/learning/shop/service/UserService.java
package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dto.RegisterRequestDTO;
// import ro.msg.learning.shop.exception.UserAlreadyExistsException; // Remove this import
import ro.msg.learning.shop.model.User;
import ro.msg.learning.shop.repository.UserRepository;

import java.util.Optional; // Import Optional

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> registerNewUser(RegisterRequestDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            return Optional.empty();
        }
        if (userRepository.existsByEmailAddress(registerRequest.emailAddress())) {
             return Optional.empty(); 
        }

        User newUser = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .emailAddress(registerRequest.emailAddress())
                .userRole(registerRequest.role())
                .build();

        User savedUser = userRepository.save(newUser);
        return Optional.of(savedUser); 
    }
}