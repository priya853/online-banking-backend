package com.onlinebanking.controller;
import jakarta.validation.Valid;
import com.onlinebanking.dto.LoginRequest;
import com.onlinebanking.entity.User;
import com.onlinebanking.repository.UserRepository;
import com.onlinebanking.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // User login
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest) {

        // Find user by email
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElse(null);

        // Invalid email
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid email or password");
        }

        // Check whether user is blocked
        if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
            return ResponseEntity
                    .status(403)
                    .body(
                            "Your account is blocked. " +
                                    "Please contact the administrator."
                    );
        }

        // Check password
        boolean passwordMatch =
                passwordEncoder.matches(
                        loginRequest.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatch) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid email or password");
        }

        // Generate JWT token
        String token =
                jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(token);
    }
}