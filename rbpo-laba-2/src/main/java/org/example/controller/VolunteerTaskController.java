package org.example.controller;

import org.example.dto.VolunteerTaskDto;
import org.example.dto.VolunteerTaskRequest;
import org.example.model.VolunteerTask;
import org.example.service.VolunteerTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/volunteer-tasks")
public class VolunteerTaskController {

    private final VolunteerTaskService taskService;

    public VolunteerTaskController(VolunteerTaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<VolunteerTaskDto> getAll() {
        return taskService.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VolunteerTaskDto getById(@PathVariable Long id) {
        return toDto(taskService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VolunteerTaskDto create(@RequestBody VolunteerTaskRequest request) {
        VolunteerTask task = new VolunteerTask();
        task.setCampaignId(request.getCampaignId());
        task.setTitle(request.getTitle());
        task.setRequiredParticipants(request.getRequiredParticipants());
        task.setStartDate(request.getStartDate());
        task.setDurationHours(request.getDurationHours());

        VolunteerTask created = taskService.create(task);
        return toDto(created);
    }

    @PatchMapping("/{id}")
    public VolunteerTaskDto update(@PathVariable Long id, @RequestBody VolunteerTaskRequest request) {
        VolunteerTask patch = new VolunteerTask();
        patch.setCampaignId(request.getCampaignId());
        patch.setTitle(request.getTitle());
        patch.setRequiredParticipants(request.getRequiredParticipants());
        patch.setStartDate(request.getStartDate());
        patch.setDurationHours(request.getDurationHours());

        VolunteerTask updated = taskService.update(id, patch);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    private VolunteerTaskDto toDto(VolunteerTask task) {
        return new VolunteerTaskDto(
                task.getId(),
                task.getCampaignId(),
                task.getTitle(),
                task.getRequiredParticipants(),
                task.getStartDate(),
                task.getDurationHours(),
                task.getCreatedAt()
        );
    }
}
