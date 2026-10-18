package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VolunteerCampaignService campaignService;
    private final VolunteerResponseService responseService;

    public UserService(UserRepository userRepository,
                       VolunteerCampaignService campaignService,
                       VolunteerResponseService responseService) {
        this.userRepository = userRepository;
        this.campaignService = campaignService;
        this.responseService = responseService;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new BadRequestException("Имя пользователя обязательно");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Email пользователя обязателен");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("VOLUNTEER");
        }

        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User update(Long id, User patch) {
        User existing = getById(id);

        if (patch.getName() != null) {
            if (patch.getName().isBlank()) {
                throw new BadRequestException("Имя пользователя не может быть пустым");
            }
            existing.setName(patch.getName());
        }
        if (patch.getEmail() != null) {
            if (patch.getEmail().isBlank()) {
                throw new BadRequestException("Email пользователя не может быть пустым");
            }
            existing.setEmail(patch.getEmail());
        }
        if (patch.getRole() != null) {
            existing.setRole(patch.getRole());
        }

        return userRepository.save(existing);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь с id " + id + " не найден");
        }
        campaignService.deleteByCoordinatorId(id);
        responseService.deleteByVolunteerId(id);
        userRepository.deleteById(id);
    }

    public void deactivateUser(Long userId) {
        var user = getById(userId);
        user.setRole("DEACTIVATED");
        
        // Отменяем все активные отклики волонтера
        var volunteerResponses = responseService.getByVolunteer(user);
        for (var resp : volunteerResponses) {
            if (!"COMPLETED".equals(resp.getStatus())) {
                resp.setStatus("CANCELLED");
                responseService.updateStatus(resp.getId(), "CANCELLED");
            }
        }
        
        userRepository.save(user);
    }
}
