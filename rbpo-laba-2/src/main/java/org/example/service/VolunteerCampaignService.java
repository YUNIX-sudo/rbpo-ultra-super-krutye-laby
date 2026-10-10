package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.VolunteerCampaign;
import org.example.repository.UserRepository;
import org.example.repository.VolunteerCampaignRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VolunteerCampaignService {

    private final VolunteerCampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final VolunteerTaskService taskService;

    public VolunteerCampaignService(VolunteerCampaignRepository campaignRepository,
                                    UserRepository userRepository,
                                    VolunteerTaskService taskService) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    public List<VolunteerCampaign> getAll() {
        return campaignRepository.findAll();
    }

    public VolunteerCampaign getById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Акция с id " + id + " не найдена"));
    }

    public VolunteerCampaign create(VolunteerCampaign campaign) {
        if (campaign.getTitle() == null || campaign.getTitle().isBlank()) {
            throw new BadRequestException("Название акции не может быть пустым");
        }
        if (campaign.getCoordinatorId() == null) {
            throw new BadRequestException("Необходимо указать id координатора");
        }
        if (!userRepository.existsById(campaign.getCoordinatorId())) {
            throw new BadRequestException("Пользователь-координатор с id " + campaign.getCoordinatorId() + " не найден");
        }
        if (campaign.getStartDate() != null && campaign.getEndDate() != null && campaign.getEndDate().isBefore(campaign.getStartDate())) {
            throw new BadRequestException("Дата окончания не может быть раньше даты начала");
        }
        if (campaign.getStatus() == null || campaign.getStatus().isBlank()) {
            campaign.setStatus("PLANNED");
        }

        campaign.setCreatedAt(LocalDateTime.now());
        return campaignRepository.save(campaign);
    }

    public VolunteerCampaign update(Long id, VolunteerCampaign patch) {
        VolunteerCampaign existing = getById(id);

        if (patch.getTitle() != null) {
            if (patch.getTitle().isBlank()) {
                throw new BadRequestException("Название акции не может быть пустым");
            }
            existing.setTitle(patch.getTitle());
        }
        if (patch.getCoordinatorId() != null) {
            if (!userRepository.existsById(patch.getCoordinatorId())) {
                throw new BadRequestException("Пользователь-координатор с id " + patch.getCoordinatorId() + " не найден");
            }
            existing.setCoordinatorId(patch.getCoordinatorId());
        }
        if (patch.getStartDate() != null) {
            existing.setStartDate(patch.getStartDate());
        }
        if (patch.getEndDate() != null) {
            existing.setEndDate(patch.getEndDate());
        }
        if (existing.getStartDate() != null && existing.getEndDate() != null && existing.getEndDate().isBefore(existing.getStartDate())) {
            throw new BadRequestException("Дата окончания не может быть раньше даты начала");
        }
        if (patch.getStatus() != null) {
            existing.setStatus(patch.getStatus());
        }

        return campaignRepository.save(existing);
    }

    public void delete(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new ResourceNotFoundException("Акция с id " + id + " не найдена");
        }
        taskService.deleteByCampaignId(id);
        campaignRepository.deleteById(id);
    }

    public void deleteByCoordinatorId(Long coordinatorId) {
        List<VolunteerCampaign> campaigns = campaignRepository.findByCoordinatorId(coordinatorId);
        for (VolunteerCampaign campaign : campaigns) {
            taskService.deleteByCampaignId(campaign.getId());
            campaignRepository.deleteById(campaign.getId());
        }
    }
}
