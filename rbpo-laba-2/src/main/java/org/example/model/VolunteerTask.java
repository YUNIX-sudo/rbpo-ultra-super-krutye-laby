package org.example.model;

import java.time.LocalDateTime;

public class VolunteerTask {
    private Long id;
    private Long campaignId;
    private String title;
    private Integer requiredParticipants;
    private LocalDateTime startDate;
    private Integer durationHours;
    private LocalDateTime createdAt;

    public VolunteerTask() {
    }

    public VolunteerTask(Long id, Long campaignId, String title, Integer requiredParticipants, LocalDateTime startDate, Integer durationHours, LocalDateTime createdAt) {
        this.id = id;
        this.campaignId = campaignId;
        this.title = title;
        this.requiredParticipants = requiredParticipants;
        this.startDate = startDate;
        this.durationHours = durationHours;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRequiredParticipants() {
        return requiredParticipants;
    }

    public void setRequiredParticipants(Integer requiredParticipants) {
        this.requiredParticipants = requiredParticipants;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
