package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_tasks")
public class VolunteerTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private VolunteerCampaign campaign;
    
    @Column(nullable = false)
    private String title;
    
    private Integer requiredParticipants;
    private LocalDateTime startDate;
    private Integer durationHours;
    private LocalDateTime createdAt;

    public VolunteerTask() {
    }

    public VolunteerTask(Long id, VolunteerCampaign campaign, String title, Integer requiredParticipants, LocalDateTime startDate, Integer durationHours, LocalDateTime createdAt) {
        this.id = id;
        this.campaign = campaign;
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

    public VolunteerCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(VolunteerCampaign campaign) {
        this.campaign = campaign;
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
