package org.example.dto;

import java.time.LocalDateTime;

public class VolunteerTaskRequest {
    private Long campaignId;
    private String title;
    private Integer requiredParticipants;
    private LocalDateTime startDate;
    private Integer durationHours;

    public VolunteerTaskRequest() {
    }

    public VolunteerTaskRequest(Long campaignId, String title, Integer requiredParticipants, LocalDateTime startDate, Integer durationHours) {
        this.campaignId = campaignId;
        this.title = title;
        this.requiredParticipants = requiredParticipants;
        this.startDate = startDate;
        this.durationHours = durationHours;
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
}
