package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_responses")
public class VolunteerResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private VolunteerTask task;
    
    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private User volunteer;
    
    private String status;
    private LocalDateTime createdAt;

    public VolunteerResponse() {
    }

    public VolunteerResponse(Long id, VolunteerTask task, User volunteer, String status, LocalDateTime createdAt) {
        this.id = id;
        this.task = task;
        this.volunteer = volunteer;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VolunteerTask getTask() {
        return task;
    }

    public void setTask(VolunteerTask task) {
        this.task = task;
    }

    public User getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
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
