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
        if (response.getTask() == null || response.getTask().getId() == null) {
            throw new BadRequestException("Необходимо указать задачу");
        }
        var task = taskRepository.findById(response.getTask().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));
        
        if (response.getVolunteer() == null || response.getVolunteer().getId() == null) {
            throw new BadRequestException("Необходимо указать волонтёра");
        }
        var volunteer = userRepository.findById(response.getVolunteer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Волонтёр не найден"));

        response.setTask(task);
        response.setVolunteer(volunteer);
        
        if (response.getStatus() == null || response.getStatus().isBlank()) {
            response.setStatus("PENDING");
        }

        response.setCreatedAt(LocalDateTime.now());
        return responseRepository.save(response);
    }

    public VolunteerResponse registerVolunteerForTask(Long taskId, Long volunteerId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));
        
        if ("FINISHED".equals(task.getCampaign().getStatus())) {
            throw new BadRequestException("Нельзя записаться на задачу завершенной кампании");
        }
        
        var volunteer = userRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Волонтёр не найден"));
        
        if (!"VOLUNTEER".equals(volunteer.getRole())) {
            throw new BadRequestException("Только пользователи с ролью VOLUNTEER могут записываться на задачи");
        }

        VolunteerResponse response = new VolunteerResponse();
        response.setTask(task);
        response.setVolunteer(volunteer);
        response.setStatus("REGISTERED");
        response.setCreatedAt(LocalDateTime.now());
        
        return responseRepository.save(response);
    }

    public VolunteerResponse update(Long id, VolunteerResponse patch) {
        VolunteerResponse existing = getById(id);

        if (patch.getTask() != null && patch.getTask().getId() != null) {
            var task = taskRepository.findById(patch.getTask().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));
            existing.setTask(task);
        }
        if (patch.getVolunteer() != null && patch.getVolunteer().getId() != null) {
            var volunteer = userRepository.findById(patch.getVolunteer().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Волонтёр не найден"));
            existing.setVolunteer(volunteer);
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
        var task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return;
        List<VolunteerResponse> responses = responseRepository.findByTask(task);
        for (VolunteerResponse response : responses) {
            reportService.deleteByResponseId(response.getId());
            responseRepository.deleteById(response.getId());
        }
    }

    public void deleteByVolunteerId(Long volunteerId) {
        var volunteer = userRepository.findById(volunteerId).orElse(null);
        if (volunteer == null) return;
        List<VolunteerResponse> responses = responseRepository.findByVolunteer(volunteer);
        for (VolunteerResponse response : responses) {
            reportService.deleteByResponseId(response.getId());
            responseRepository.deleteById(response.getId());
        }
    }

    public List<VolunteerResponse> getByVolunteer(org.example.model.User volunteer) {
        return responseRepository.findByVolunteer(volunteer);
    }

    public void updateStatus(Long id, String status) {
        var response = getById(id);
        response.setStatus(status);
        responseRepository.save(response);
    }
}
