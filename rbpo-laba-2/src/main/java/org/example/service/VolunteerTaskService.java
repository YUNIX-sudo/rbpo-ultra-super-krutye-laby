package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.VolunteerTask;
import org.example.repository.VolunteerCampaignRepository;
import org.example.repository.VolunteerTaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VolunteerTaskService {

    private final VolunteerTaskRepository taskRepository;
    private final VolunteerCampaignRepository campaignRepository;
    private final VolunteerResponseService responseService;

    public VolunteerTaskService(VolunteerTaskRepository taskRepository,
                                VolunteerCampaignRepository campaignRepository,
                                VolunteerResponseService responseService) {
        this.taskRepository = taskRepository;
        this.campaignRepository = campaignRepository;
        this.responseService = responseService;
    }

    public List<VolunteerTask> getAll() {
        return taskRepository.findAll();
    }

    public VolunteerTask getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача акции с id " + id + " не найдена"));
    }

    public VolunteerTask create(VolunteerTask task) {
        if (task.getCampaignId() == null) {
            throw new BadRequestException("Необходимо указать id акции");
        }
        if (!campaignRepository.existsById(task.getCampaignId())) {
            throw new BadRequestException("Акция с id " + task.getCampaignId() + " не найдена");
        }
        if (task.getRequiredParticipants() != null && task.getRequiredParticipants() <= 0) {
            throw new BadRequestException("Число участников должно быть больше 0");
        }
        if (task.getDurationHours() != null && task.getDurationHours() <= 0) {
            throw new BadRequestException("Длительность в часах должна быть положительным числом");
        }

        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public VolunteerTask update(Long id, VolunteerTask patch) {
        VolunteerTask existing = getById(id);

        if (patch.getCampaignId() != null) {
            if (!campaignRepository.existsById(patch.getCampaignId())) {
                throw new BadRequestException("Акция с id " + patch.getCampaignId() + " не найдена");
            }
            existing.setCampaignId(patch.getCampaignId());
        }
        if (patch.getTitle() != null) {
            existing.setTitle(patch.getTitle());
        }
        if (patch.getRequiredParticipants() != null) {
            if (patch.getRequiredParticipants() <= 0) {
                throw new BadRequestException("Число участников должно быть больше 0");
            }
            existing.setRequiredParticipants(patch.getRequiredParticipants());
        }
        if (patch.getStartDate() != null) {
            existing.setStartDate(patch.getStartDate());
        }
        if (patch.getDurationHours() != null) {
            if (patch.getDurationHours() <= 0) {
                throw new BadRequestException("Длительность в часах должна быть положительным числом");
            }
            existing.setDurationHours(patch.getDurationHours());
        }

        return taskRepository.save(existing);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Задача акции с id " + id + " не найдена");
        }
        responseService.deleteByTaskId(id);
        taskRepository.deleteById(id);
    }

    public void deleteByCampaignId(Long campaignId) {
        List<VolunteerTask> tasks = taskRepository.findByCampaignId(campaignId);
        for (VolunteerTask task : tasks) {
            responseService.deleteByTaskId(task.getId());
            taskRepository.deleteById(task.getId());
        }
    }
}
