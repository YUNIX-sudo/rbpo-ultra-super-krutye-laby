package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CsrfResponse;
import org.example.dto.RegisterRequest;
import org.example.dto.UserDto;
import org.example.exception.BadRequestException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/csrf")
    public CsrfResponse getCsrfToken(CsrfToken csrfToken) {
        if (csrfToken == null) {
            return new CsrfResponse("", "X-XSRF-TOKEN", "_csrf");
        }
        return new CsrfResponse(csrfToken.getToken(), csrfToken.getHeaderName(), csrfToken.getParameterName());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("VOLUNTEER");
        user.setIsActive(1);
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return new UserDto(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole(), saved.getCreatedAt());
    }
}
