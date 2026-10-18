package org.example.controller;

import org.example.dto.VolunteerResponseDto;
import org.example.dto.VolunteerResponseRequest;
import org.example.model.User;
import org.example.model.VolunteerResponse;
import org.example.model.VolunteerTask;
import org.example.service.VolunteerResponseService;
import org.springframework.http.HttpStatus;
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
    public List<VolunteerResponseDto> getAll() {
        return responseService.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VolunteerResponseDto getById(@PathVariable Long id) {
        return toDto(responseService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    public void delete(@PathVariable Long id) {
        responseService.delete(id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
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
