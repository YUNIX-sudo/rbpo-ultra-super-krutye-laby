package org.example.controller;

import org.example.dto.VolunteerResponseDto;
import org.example.dto.VolunteerResponseRequest;
import org.example.model.VolunteerResponse;
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
        response.setTaskId(request.getTaskId());
        response.setVolunteerId(request.getVolunteerId());
        response.setStatus(request.getStatus());

        VolunteerResponse created = responseService.create(response);
        return toDto(created);
    }

    @PatchMapping("/{id}")
    public VolunteerResponseDto update(@PathVariable Long id, @RequestBody VolunteerResponseRequest request) {
        VolunteerResponse patch = new VolunteerResponse();
        patch.setTaskId(request.getTaskId());
        patch.setVolunteerId(request.getVolunteerId());
        patch.setStatus(request.getStatus());

        VolunteerResponse updated = responseService.update(id, patch);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        responseService.delete(id);
    }

    private VolunteerResponseDto toDto(VolunteerResponse response) {
        return new VolunteerResponseDto(
                response.getId(),
                response.getTaskId(),
                response.getVolunteerId(),
                response.getStatus(),
                response.getCreatedAt()
        );
    }
}
