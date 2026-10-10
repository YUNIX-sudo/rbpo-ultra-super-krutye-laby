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
        if (report.getResponseId() == null) {
            throw new BadRequestException("Необходимо указать id отклика волонтёра");
        }
        if (!responseRepository.existsById(report.getResponseId())) {
            throw new BadRequestException("Отклик волонтёра с id " + report.getResponseId() + " не существует");
        }
        if (report.getActualHours() != null && report.getActualHours() < 0) {
            throw new BadRequestException("Фактические часы не могут быть отрицательными");
        }
        if (report.getConfirmationStatus() == null || report.getConfirmationStatus().isBlank()) {
            report.setConfirmationStatus("PENDING");
        }

        report.setCreatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public ParticipationReport update(Long id, ParticipationReport patch) {
        ParticipationReport existing = getById(id);

        if (patch.getResponseId() != null) {
            if (!responseRepository.existsById(patch.getResponseId())) {
                throw new BadRequestException("Отклик волонтёра с id " + patch.getResponseId() + " не существует");
            }
            existing.setResponseId(patch.getResponseId());
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
        List<ParticipationReport> reports = reportRepository.findByResponseId(responseId);
        for (ParticipationReport report : reports) {
            reportRepository.deleteById(report.getId());
        }
    }
}
