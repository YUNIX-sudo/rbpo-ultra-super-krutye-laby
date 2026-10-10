package org.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VolunteerCampaign {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long coordinatorId;
    private String status;
    private LocalDateTime createdAt;

    public VolunteerCampaign() {
    }

    public VolunteerCampaign(Long id, String title, LocalDate startDate, LocalDate endDate, Long coordinatorId, String status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coordinatorId = coordinatorId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Long coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
