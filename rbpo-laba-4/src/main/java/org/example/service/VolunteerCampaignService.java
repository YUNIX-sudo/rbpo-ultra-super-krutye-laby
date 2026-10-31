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
        if (campaign.getCoordinator() == null || campaign.getCoordinator().getId() == null) {
            throw new BadRequestException("Необходимо указать координатора");
        }
        var coordinator = userRepository.findById(campaign.getCoordinator().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Координатор не найден"));
        
        if (!"COORDINATOR".equals(coordinator.getRole())) {
            throw new BadRequestException("Только пользователи с ролью COORDINATOR могут быть координаторами кампаний");
        }

        campaign.setCoordinator(coordinator);
        
        if (campaign.getStartDate() != null && campaign.getEndDate() != null && campaign.getEndDate().isBefore(campaign.getStartDate())) {
            throw new BadRequestException("Дата окончания не может быть раньше даты начала");
        }
        if (campaign.getStatus() == null || campaign.getStatus().isBlank()) {
            campaign.setStatus("PLANNED");
        }

        campaign.setCreatedAt(LocalDateTime.now());
        return campaignRepository.save(campaign);
    }

    public VolunteerCampaign finishCampaign(Long campaignId) {
        var campaign = getById(campaignId);
        campaign.setStatus("FINISHED");
        // Логика: при завершении кампании можно также менять статус задач, но JPA связи это покроют при необходимости
        return campaignRepository.save(campaign);
    }

    public VolunteerCampaign assignCoordinatorToCampaign(Long campaignId, Long coordinatorId) {
        var campaign = getById(campaignId);
        var coordinator = userRepository.findById(coordinatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Координатор не найден"));
        
        if (!"COORDINATOR".equals(coordinator.getRole())) {
            throw new BadRequestException("Назначить можно только пользователя с ролью COORDINATOR");
        }
        
        campaign.setCoordinator(coordinator);
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
        if (patch.getCoordinator() != null && patch.getCoordinator().getId() != null) {
            var coordinator = userRepository.findById(patch.getCoordinator().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Координатор не найден"));
            existing.setCoordinator(coordinator);
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
        var coordinator = userRepository.findById(coordinatorId).orElse(null);
        if (coordinator == null) return;
        List<VolunteerCampaign> campaigns = campaignRepository.findByCoordinator(coordinator);
        for (VolunteerCampaign campaign : campaigns) {
            taskService.deleteByCampaignId(campaign.getId());
            campaignRepository.deleteById(campaign.getId());
        }
    }
}
