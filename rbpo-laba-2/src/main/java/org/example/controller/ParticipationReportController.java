package org.example.controller;

import org.example.dto.ParticipationReportDto;
import org.example.dto.ParticipationReportRequest;
import org.example.model.ParticipationReport;
import org.example.service.ParticipationReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participation-reports")
public class ParticipationReportController {

    private final ParticipationReportService reportService;

    public ParticipationReportController(ParticipationReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public List<ParticipationReportDto> getAll() {
        return reportService.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ParticipationReportDto getById(@PathVariable Long id) {
        return toDto(reportService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationReportDto create(@RequestBody ParticipationReportRequest request) {
        ParticipationReport report = new ParticipationReport();
        report.setResponseId(request.getResponseId());
        report.setActualHours(request.getActualHours());
        report.setConfirmationStatus(request.getConfirmationStatus());

        ParticipationReport created = reportService.create(report);
        return toDto(created);
    }

    @PatchMapping("/{id}")
    public ParticipationReportDto update(@PathVariable Long id, @RequestBody ParticipationReportRequest request) {
        ParticipationReport patch = new ParticipationReport();
        patch.setResponseId(request.getResponseId());
        patch.setActualHours(request.getActualHours());
        patch.setConfirmationStatus(request.getConfirmationStatus());

        ParticipationReport updated = reportService.update(id, patch);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reportService.delete(id);
    }

    private ParticipationReportDto toDto(ParticipationReport report) {
        return new ParticipationReportDto(
                report.getId(),
                report.getResponseId(),
                report.getActualHours(),
                report.getConfirmationStatus(),
                report.getCreatedAt()
        );
    }
}
