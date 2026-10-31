package org.example.controller;

import org.example.dto.VolunteerResponseDto;
import org.example.dto.VolunteerResponseRequest;
import org.example.model.User;
import org.example.model.VolunteerResponse;
import org.example.model.VolunteerTask;
import org.example.service.VolunteerResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/volunteer-responses")
public class VolunteerResponseController {

    private final VolunteerResponseService responseService;

    public VolunteerResponseController(VolunteerResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COORDINATOR', 'VOLUNTEER')")
    public List<VolunteerResponseDto> getAll() {
        return responseService.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COORDINATOR', 'VOLUNTEER')")
    public VolunteerResponseDto getById(@PathVariable Long id) {
        return toDto(responseService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('COORDINATOR')")
    public VolunteerResponseDto create(@RequestBody VolunteerResponseRequest request) {
        VolunteerResponse response = new VolunteerResponse();

        VolunteerTask task = new VolunteerTask();
        task.setId(request.getTaskId());
        response.setTask(task);

        User volunteer = new User();
        volunteer.setId(request.getVolunteerId());
        response.setVolunteer(volunteer);

        response.setStatus(request.getStatus());

        VolunteerResponse created = responseService.create(response);
        return toDto(created);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public VolunteerResponseDto update(@PathVariable Long id, @RequestBody VolunteerResponseRequest request) {
        VolunteerResponse patch = new VolunteerResponse();

        if (request.getTaskId() != null) {
            VolunteerTask task = new VolunteerTask();
            task.setId(request.getTaskId());
            patch.setTask(task);
        }

        if (request.getVolunteerId() != null) {
            User volunteer = new User();
            volunteer.setId(request.getVolunteerId());
            patch.setVolunteer(volunteer);
        }

        patch.setStatus(request.getStatus());

        VolunteerResponse updated = responseService.update(id, patch);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('COORDINATOR') or (hasRole('VOLUNTEER') and @volunteerResponseService.getById(#id).volunteer.email == authentication.name)")
    public void delete(@PathVariable Long id) {
        responseService.delete(id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('COORDINATOR') or (hasRole('VOLUNTEER') and @userRepository.findById(#volunteerId).orElse(new org.example.model.User()).email == authentication.name)")
    public VolunteerResponseDto register(@RequestParam Long taskId, @RequestParam Long volunteerId) {
        return toDto(responseService.registerVolunteerForTask(taskId, volunteerId));
    }

    private VolunteerResponseDto toDto(VolunteerResponse response) {
        return new VolunteerResponseDto(
                response.getId(),
                response.getTask() != null ? response.getTask().getId() : null,
                response.getVolunteer() != null ? response.getVolunteer().getId() : null,
                response.getStatus(),
                response.getCreatedAt()
        );
    }
}
