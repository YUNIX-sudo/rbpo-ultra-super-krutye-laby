package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_reports")
public class ParticipationReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "response_id", nullable = false)
    private VolunteerResponse response;
    
    private Integer actualHours;
    private String confirmationStatus;
    private LocalDateTime createdAt;

    public ParticipationReport() {
    }

    public ParticipationReport(Long id, VolunteerResponse response, Integer actualHours, String confirmationStatus, LocalDateTime createdAt) {
        this.id = id;
        this.response = response;
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

    public VolunteerResponse getResponse() {
        return response;
    }

    public void setResponse(VolunteerResponse response) {
        this.response = response;
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
