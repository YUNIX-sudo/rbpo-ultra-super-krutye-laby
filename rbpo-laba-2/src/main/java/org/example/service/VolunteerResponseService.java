package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.VolunteerResponse;
import org.example.repository.UserRepository;
import org.example.repository.VolunteerResponseRepository;
import org.example.repository.VolunteerTaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VolunteerResponseService {

    private final VolunteerResponseRepository responseRepository;
    private final VolunteerTaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ParticipationReportService reportService;

    public VolunteerResponseService(VolunteerResponseRepository responseRepository,
                                    VolunteerTaskRepository taskRepository,
                                    UserRepository userRepository,
                                    ParticipationReportService reportService) {
        this.responseRepository = responseRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.reportService = reportService;
    }

    public List<VolunteerResponse> getAll() {
        return responseRepository.findAll();
    }

    public VolunteerResponse getById(Long id) {
        return responseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отклик с id " + id + " не найден"));
    }

    public VolunteerResponse create(VolunteerResponse response) {
        if (response.getTaskId() == null) {
            throw new BadRequestException("Необходимо указать id задачи");
        }
        if (!taskRepository.existsById(response.getTaskId())) {
            throw new BadRequestException("Задача с id " + response.getTaskId() + " не найдена");
        }
        if (response.getVolunteerId() == null) {
            throw new BadRequestException("Необходимо указать id волонтёра");
        }
        if (!userRepository.existsById(response.getVolunteerId())) {
            throw new BadRequestException("Волонтёр с id " + response.getVolunteerId() + " не найден");
        }
        if (response.getStatus() == null || response.getStatus().isBlank()) {
            response.setStatus("PENDING");
        }

        response.setCreatedAt(LocalDateTime.now());
        return responseRepository.save(response);
    }

    public VolunteerResponse update(Long id, VolunteerResponse patch) {
        VolunteerResponse existing = getById(id);

        if (patch.getTaskId() != null) {
            if (!taskRepository.existsById(patch.getTaskId())) {
                throw new BadRequestException("Задача с id " + patch.getTaskId() + " не найдена");
            }
            existing.setTaskId(patch.getTaskId());
        }
        if (patch.getVolunteerId() != null) {
            if (!userRepository.existsById(patch.getVolunteerId())) {
                throw new BadRequestException("Волонтёр с id " + patch.getVolunteerId() + " не найден");
            }
            existing.setVolunteerId(patch.getVolunteerId());
        }
        if (patch.getStatus() != null) {
            existing.setStatus(patch.getStatus());
        }

        return responseRepository.save(existing);
    }

    public void delete(Long id) {
        if (!responseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Отклик с id " + id + " не найден");
        }
        reportService.deleteByResponseId(id);
        responseRepository.deleteById(id);
    }

    public void deleteByTaskId(Long taskId) {
        List<VolunteerResponse> responses = responseRepository.findByTaskId(taskId);
        for (VolunteerResponse response : responses) {
            reportService.deleteByResponseId(response.getId());
            responseRepository.deleteById(response.getId());
        }
    }

    public void deleteByVolunteerId(Long volunteerId) {
        List<VolunteerResponse> responses = responseRepository.findByVolunteerId(volunteerId);
        for (VolunteerResponse response : responses) {
            reportService.deleteByResponseId(response.getId());
            responseRepository.deleteById(response.getId());
        }
    }
}
