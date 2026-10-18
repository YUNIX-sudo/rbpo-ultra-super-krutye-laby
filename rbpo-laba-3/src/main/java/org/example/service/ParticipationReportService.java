package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.ParticipationReport;
import org.example.repository.ParticipationReportRepository;
import org.example.repository.VolunteerResponseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParticipationReportService {

    private final ParticipationReportRepository reportRepository;
    private final VolunteerResponseRepository responseRepository;

    public ParticipationReportService(ParticipationReportRepository reportRepository,
                                      VolunteerResponseRepository responseRepository) {
        this.reportRepository = reportRepository;
        this.responseRepository = responseRepository;
    }

    public List<ParticipationReport> getAll() {
        return reportRepository.findAll();
    }

    public ParticipationReport getById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отчёт с id " + id + " не найден"));
    }

    public ParticipationReport create(ParticipationReport report) {
        if (report.getResponse() == null || report.getResponse().getId() == null) {
            throw new BadRequestException("Необходимо указать отклик волонтёра");
        }
        var response = responseRepository.findById(report.getResponse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Отклик не найден"));

        report.setResponse(response);
        
        if (report.getActualHours() != null && report.getActualHours() < 0) {
            throw new BadRequestException("Фактические часы не могут быть отрицательными");
        }
        if (report.getConfirmationStatus() == null || report.getConfirmationStatus().isBlank()) {
            report.setConfirmationStatus("PENDING");
        }

        report.setCreatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public ParticipationReport confirmParticipation(Long reportId, String status) {
        var report = getById(reportId);
        report.setConfirmationStatus(status);
        
        if ("CONFIRMED".equals(status)) {
            var response = report.getResponse();
            response.setStatus("COMPLETED");
            responseRepository.save(response);
        }
        
        return reportRepository.save(report);
    }

    public ParticipationReport update(Long id, ParticipationReport patch) {
        ParticipationReport existing = getById(id);

        if (patch.getResponse() != null && patch.getResponse().getId() != null) {
            var response = responseRepository.findById(patch.getResponse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Отклик не найден"));
            existing.setResponse(response);
        }
        if (patch.getActualHours() != null) {
            if (patch.getActualHours() < 0) {
                throw new BadRequestException("Фактические часы не могут быть отрицательными");
            }
            existing.setActualHours(patch.getActualHours());
        }
        if (patch.getConfirmationStatus() != null) {
            existing.setConfirmationStatus(patch.getConfirmationStatus());
        }

        return reportRepository.save(existing);
    }

    public void delete(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Отчёт с id " + id + " не найден");
        }
        reportRepository.deleteById(id);
    }

    public void deleteByResponseId(Long responseId) {
        var response = responseRepository.findById(responseId).orElse(null);
        if (response == null) return;
        reportRepository.findByResponse(response).ifPresent(report -> reportRepository.deleteById(report.getId()));
    }
}
