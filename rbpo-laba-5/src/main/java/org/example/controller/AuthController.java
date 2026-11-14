package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CsrfResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RefreshRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.TokenResponse;
import org.example.exception.BadRequestException;
import org.example.exception.ConflictException;
import org.example.exception.UnauthorizedException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserSessionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping({"/auth", "/api/auth", ""})
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSessionService userSessionService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          UserSessionService userSessionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSessionService = userSessionService;
    }

    @GetMapping({"/csrf", "/auth/csrf", "/api/auth/csrf"})
    public CsrfResponse getCsrfToken(CsrfToken csrfToken) {
        if (csrfToken == null) {
            return new CsrfResponse("", "X-XSRF-TOKEN", "_csrf");
        }
        return new CsrfResponse(csrfToken.getToken(), csrfToken.getHeaderName(), csrfToken.getParameterName());
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody(required = false) LoginRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body is required");
        }
        String email = request.getEmail();
        if (email == null || email.isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Email and password are required");
        }

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (user.getIsActive() == null || user.getIsActive() != 1 || "DEACTIVATED".equalsIgnoreCase(user.getRole())) {
            throw new UnauthorizedException("User is deactivated");
        }

        TokenResponse tokens = userSessionService.issueTokenPair(user, request.getDeviceId());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
                .body(tokens);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody(required = false) RegisterRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body is required");
        }
        String email = request.getEmail();
        if (email == null || email.isBlank() ||
                request.getName() == null || request.getName().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("All fields are required");
        }

        email = email.trim();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("User with email " + email + " already exists");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("VOLUNTEER");
        user.setIsActive(1);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        TokenResponse tokens = userSessionService.issueTokenPair(savedUser, request.getDeviceId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
                .body(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody(required = false) RefreshRequest request) {
        if (request == null || request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
            throw new BadRequestException("refreshToken is required");
        }

        TokenResponse tokens = userSessionService.refreshTokenPair(request.getRefreshToken().trim());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
                .body(tokens);
    }
}
