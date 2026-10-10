package org.example.dto;

public class VolunteerResponseRequest {
    private Long taskId;
    private Long volunteerId;
    private String status;

    public VolunteerResponseRequest() {
    }

    public VolunteerResponseRequest(Long taskId, Long volunteerId, String status) {
        this.taskId = taskId;
        this.volunteerId = volunteerId;
        this.status = status;
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
}
