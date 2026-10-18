package org.example.controller;

import org.example.dto.UserDto;
import org.example.dto.UserRequest;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return toDto(userService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        User created = userService.create(user);
        return toDto(created);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserRequest request) {
        User patch = new User();
        patch.setName(request.getName());
        patch.setEmail(request.getEmail());
        patch.setRole(request.getRole());

        User updated = userService.update(id, patch);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
