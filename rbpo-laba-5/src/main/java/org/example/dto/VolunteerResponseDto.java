package org.example.dto;

import java.time.LocalDateTime;

public class VolunteerResponseDto {
    private Long id;
    private Long taskId;
    private Long volunteerId;
    private String status;
    private LocalDateTime createdAt;

    public VolunteerResponseDto() {
    }

    public VolunteerResponseDto(Long id, Long taskId, Long volunteerId, String status, LocalDateTime createdAt) {
        this.id = id;
        this.taskId = taskId;
        this.volunteerId = volunteerId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
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
