package org.example.dto;

public class ParticipationReportRequest {
    private Long responseId;
    private Integer actualHours;
    private String confirmationStatus;

    public ParticipationReportRequest() {
    }

    public ParticipationReportRequest(Long responseId, Integer actualHours, String confirmationStatus) {
        this.responseId = responseId;
        this.actualHours = actualHours;
        this.confirmationStatus = confirmationStatus;
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
}
