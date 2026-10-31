package org.example.dto;

import java.time.LocalDateTime;

public class ParticipationReportDto {
    private Long id;
    private Long responseId;
    private Integer actualHours;
    private String confirmationStatus;
    private LocalDateTime createdAt;

    public ParticipationReportDto() {
    }

    public ParticipationReportDto(Long id, Long responseId, Integer actualHours, String confirmationStatus, LocalDateTime createdAt) {
        this.id = id;
        this.responseId = responseId;
        this.actualHours = actualHours;
        this.confirmationStatus = confirmationStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public Integer getActualHours() {
        return actualHours;
    }

    public void setActualHours(Integer actualHours) {
        this.actualHours = actualHours;
    }

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
