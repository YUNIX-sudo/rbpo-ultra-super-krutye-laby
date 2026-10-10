package org.example.controller;

import org.example.dto.VolunteerCampaignDto;
import org.example.dto.VolunteerCampaignRequest;
import org.example.model.VolunteerCampaign;
import org.example.service.VolunteerCampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/volunteer-campaigns")
public class VolunteerCampaignController {

    private final VolunteerCampaignService campaignService;

    public VolunteerCampaignController(VolunteerCampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<VolunteerCampaignDto> getAll() {
        return campaignService.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VolunteerCampaignDto getById(@PathVariable Long id) {
        return toDto(campaignService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VolunteerCampaignDto create(@RequestBody VolunteerCampaignRequest request) {
        VolunteerCampaign campaign = new VolunteerCampaign();
        campaign.setTitle(request.getTitle());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setCoordinatorId(request.getCoordinatorId());
        campaign.setStatus(request.getStatus());

        VolunteerCampaign created = campaignService.create(campaign);
        return toDto(created);
    }

    @PatchMapping("/{id}")
    public VolunteerCampaignDto update(@PathVariable Long id, @RequestBody VolunteerCampaignRequest request) {
        VolunteerCampaign patch = new VolunteerCampaign();
        patch.setTitle(request.getTitle());
        patch.setStartDate(request.getStartDate());
        patch.setEndDate(request.getEndDate());
        patch.setCoordinatorId(request.getCoordinatorId());
        patch.setStatus(request.getStatus());

        VolunteerCampaign updated = campaignService.update(id, patch);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        campaignService.delete(id);
    }

    private VolunteerCampaignDto toDto(VolunteerCampaign campaign) {
        return new VolunteerCampaignDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getStartDate(),
                campaign.getEndDate(),
                campaign.getCoordinatorId(),
                campaign.getStatus(),
                campaign.getCreatedAt()
        );
    }
}
