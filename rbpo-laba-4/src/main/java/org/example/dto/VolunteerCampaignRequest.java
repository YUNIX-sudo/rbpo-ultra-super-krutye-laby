package org.example.dto;

import java.time.LocalDate;

public class VolunteerCampaignRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long coordinatorId;
    private String status;

    public VolunteerCampaignRequest() {
    }

    public VolunteerCampaignRequest(String title, LocalDate startDate, LocalDate endDate, Long coordinatorId, String status) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coordinatorId = coordinatorId;
        this.status = status;
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
}
